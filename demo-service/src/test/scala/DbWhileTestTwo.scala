import java.sql._
import java.util.Random
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.{Executors, TimeUnit}

import com.google.common.util.concurrent.ThreadFactoryBuilder
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource
import com.today.eventbus.EventStore
import javax.sql.DataSource
import org.slf4j.LoggerFactory
import wangzx.scala_commons.sql.{JdbcValue, ResultSetMapper, Row, SQLWithArgs}

import scala.collection.mutable.ListBuffer


class DbWhileTestTwo {

  val logger = LoggerFactory.getLogger(classOf[DbWhileTest])
  val logCount = new AtomicInteger(0)

  def createdMemberEvent(): Unit = {
    val eventType = "com.github.dapeng.user.scala.events.BlackedEvent"
    (1 to 1000).foreach(_ => {
      val id = new Random().nextInt(1000000000)
      val connection = dataSource.getConnection()
      executeUpdate(connection,s"""INSERT INTO dp_common_event SET id = ${id},event_type= '${eventType}' """)
      connection.close()
    })

  }


  val dataSource = {
    val ds = new MysqlDataSource
    ds.setURL(s"jdbc:mysql://10.10.10.37:3306/easy_demo?useUnicode=true&characterEncoding=utf8&zeroDateTimeBehavior=convertToNull")
    ds.setUser("root")
    ds.setPassword("today-36524")
    ds
  }


  /**
    * 基于jdk定时线程池,处理消息轮询发送....
    */
  def startScheduled(): Unit = {
    val schedulerPublisher = Executors.newScheduledThreadPool(1,
      new ThreadFactoryBuilder()
        .setDaemon(true)
        .setNameFormat("dapeng-eventbus--scheduler-%d")
        .build)

    schedulerPublisher.scheduleAtFixedRate(() => {
      try {
        doPublishMessages()
      } catch {
        case e: Exception => println(s"eventbus: 定时轮询线程内出现了异常，已捕获 msg:${e.getMessage}", e)
      }

    }, 1000, 100, TimeUnit.MILLISECONDS)
  }

  /**
    * fetch message from database , then send to kafka broker
    */
  def doPublishMessages(): Unit = {
    //    logger.info(Thread.currentThread().getName + " :begin to publish messages to kafka")

    val count = logCount.incrementAndGet()
    if (count == 20) {
      logger.info("begin to publish messages to kafka")
    }
    // 消息总条数计数器
    val counter = new AtomicInteger(0)
    // 批量处理, 每次从数据库取出消息的最大数量(window)
    val window = 100
    // 单轮处理的消息计数器, 用于控制循环退出.
    val resultSetCounter = new AtomicInteger(window)

    /**
      * id: 作用是不锁住全表，获取消息时不会影响插入
      *
      * uniqueId:
      */
    do {
      resultSetCounter.set(0)
      withTransaction(dataSource)(conn => {
        eachRow[Row](conn, s"SELECT * FROM dp_event_lock WHERE id = 1 FOR UPDATE") { lock_row =>
          if (count == 20) {
            logger.info(s"获得 dp_event_lock 锁,开始查询消息并发送 lock:")
          }
        }

        // 没有 for update
        eachRow[EventStore](conn, s"SELECT * FROM dp_common_event limit ${window}") { event =>
          val result: Int = executeUpdate(conn, s"DELETE FROM dp_common_event WHERE id = ${event.id}")

          if (result == 1) {
            Thread.sleep(250)
            println(Thread.currentThread().getName + " kafka send message !")
            counter.incrementAndGet()
          }
          resultSetCounter.incrementAndGet()

        }

        if (counter.get() > 0) {
          logger.info(s" This round : process and publish messages(${counter.get()}) rows to kafka \n")
        }
      })

    } while (resultSetCounter.get() == window)


    if (counter.get() > 0) {
      logger.info(s"end publish messages(${counter.get()}) to kafka")
    }

    if (count == 20) {
      logger.info("[MsgPublishTask] 结束一轮轮询，将计数器置为 0 ")
      logCount.set(0)
    }

  }


  def withTransaction[T](dataSource: DataSource)(f: Connection => T): T = {
    val conn = dataSource.getConnection
    try {
      conn.setAutoCommit(false)
      val result = f(conn)
      conn.commit
      result
    } catch {
      case ex: Throwable =>
        conn.rollback
        throw ex
    } finally {
      conn.close()
    }
  }

  def eachRow[T: ResultSetMapper](connection: Connection, sql: SQLWithArgs)(f: T => Unit) = {
    withPreparedStatement(sql.sql, connection) {
      prepared =>
        if (sql.args != null) setStatementArgs(prepared, connection, sql.args)

        logger.debug("SQL Preparing: {} args: {}", Seq(sql.sql, sql.args): _*)

        val mapper = implicitly[ResultSetMapper[T]]
        val rs = prepared.executeQuery()
        val rsMeta = rs.getMetaData
        while (rs.next()) {
          val mapped = mapper.from(rs)
          f(mapped)
        }
        logger.debug("SQL result: {}", rs.getRow)
    }

  }

  def executeUpdate(connection: Connection, stmt: SQLWithArgs): Int = executeUpdateWithGenerateKey(connection, stmt)(null)


  def rows[T: ResultSetMapper](conn: Connection, sql: SQLWithArgs): List[T] = withPreparedStatement(sql.sql, conn) { prepared =>
    val buffer = new ListBuffer[T]()
    if (sql.args != null) setStatementArgs(prepared, conn, sql.args)

    logger.debug("SQL Preparing: {} args: {}", Seq(sql.sql, sql.args): _*)

    val rs = prepared.executeQuery()
    val rsMeta = rs.getMetaData
    while (rs.next()) {
      val mapped = implicitly[ResultSetMapper[T]].from(rs)
      buffer += mapped

    }
    logger.debug("SQL result: {}", buffer.size)
    buffer.toList
  }


  private def withPreparedStatement[T](sql: String, conn: Connection)(f: PreparedStatement => T): T = {
    val stmt = conn.prepareStatement(sql)
    try {
      f(stmt)
    } finally {
      stmt.close()
    }
  }

  @inline private def setStatementArgs(stmt: PreparedStatement, conn: Connection, args: Seq[JdbcValue[_]]) =
    args.zipWithIndex.foreach {
      case (null, idx) => stmt.setNull(idx + 1, Types.VARCHAR)
      case (v, idx) => v.passIn(stmt, idx + 1)
    }


  def executeUpdateWithGenerateKey(conn: Connection, stmt: SQLWithArgs)(processGenerateKeys: ResultSet => Unit = null): Int = {
    val prepared = conn.prepareStatement(stmt.sql,
      if (processGenerateKeys != null) Statement.RETURN_GENERATED_KEYS
      else Statement.NO_GENERATED_KEYS)

    try {
      if (stmt.args != null) setStatementArgs(prepared, conn, stmt.args)

      logger.debug("SQL Preparing: {} args: {}", Seq(stmt.sql, stmt.args): _*)

      val result = prepared.executeUpdate()

      if (processGenerateKeys != null) {
        val keys = prepared.getGeneratedKeys
        processGenerateKeys(keys)
      }

      logger.debug("SQL result: {}", result)
      result
    }
    finally {
      prepared.close
    }
  }

}

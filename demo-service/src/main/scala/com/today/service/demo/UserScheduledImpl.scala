package com.today.service.demo

import com.github.dapeng.core.helper.MasterHelper
import com.github.dapeng.core.timer.{ScheduledTask, ScheduledTaskCron}
import com.github.dapeng.user.scala.service.UserScheduledService
import org.slf4j.LoggerFactory

/**
  *
  * 描述:
  *
  * @author hz.lei
  * @date 2018年04月25日 下午5:42
  */
@ScheduledTask
class UserScheduledImpl extends UserScheduledService {

  val logger = LoggerFactory.getLogger(classOf[UserScheduledImpl])

  override def testIsMaster(): Unit = {

  }

  @ScheduledTaskCron(cron = "*/5 * * * * ?")
  override def createdMember(): Unit = {
    val isMaster = MasterHelper.isMaster("com.today.api.demo.service.UserService", "1.0.0")
    //    logger.info(s"判断该服务是不是master service master status :  ${isMaster}")
  }
}

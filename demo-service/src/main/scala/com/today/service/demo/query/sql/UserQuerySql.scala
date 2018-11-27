package com.today.service.demo.query.sql

import com.today.api.scala.page.TPageResponse
import com.today.api.demo.scala.dto.TUser
import com.today.api.demo.scala.enums.UserStatusEnum
import com.today.api.demo.scala.request.{FindUserByPageRequest, LoginUserRequest}
import com.today.api.demo.scala.response.{FindUserByPageResponse, LoginUserResponse}
import com.today.service.commons.`implicit`.Implicits._
import com.today.service.demo.DemoDataSource
import com.today.service.demo.dto.User
import wangzx.scala_commons.sql._
import com.today.service.commons.exception.CommonException._
import com.today.service.commons.Assert._

object UserQuerySql {

  /**
    * 根据用户id查找用户
    *
    * @param userId
    * @return
    */
  def queryUserById(userId: Int): Option[TUser] = {
    DemoDataSource.mysqlData
      .row[TUser](sql"""SELECT * FROM user WHERE id  = ${userId}""")
  }


  /**
    * 根据手机号查找用户
    *
    * @param tel
    * @return
    */
  def queryUserByTel(tel: String): Option[TUser] = {
    DemoDataSource.mysqlData
      .row[TUser](sql"""SELECT * FROM user WHERE telephone = ${tel}""")
  }

  /**
    * 分页查找用户
    *
    * @return
    */
  def queryUserByPage(request: FindUserByPageRequest): FindUserByPageResponse = {

    val query =
      sql""" SELECT * FROM user WHERE 1=1 """
    val limitSql = sql""" limit ${request.pageRequest.start} , ${request.pageRequest.limit} """

    // 针对多个可选的查询条件
    val optionSql = List[SQLWithArgs](
      request.userStatus.optional(x => sql" AND  user_status = ${x.id} "),
      // 积分
      request.integral.optional(x => sql" AND integral > ${x} "),
      // 排序字段
      request.pageRequest.sortFields.optional(sort => sql" ORDER BY ${sort} ")
    ).reduceLeft(_ + _)

    val users = DemoDataSource.mysqlData.rows[User](query + optionSql + limitSql)
    val counts = DemoDataSource.mysqlData.queryInt(sql"SELECT count(1) FROM user WHERE 1=1 " + optionSql)
    BeanBuilder
      .build[FindUserByPageResponse](request.pageRequest)(
      "pageResponse" -> TPageResponse(request.pageRequest.start, request.pageRequest.limit, counts),
      "userList" -> (users.map(x => {
        BeanBuilder.build[TUser](x)()
      }): List[TUser])
    )
  }

  /**
    * 根据用户名和密码查询
    *
    * @param request
    * @return
    */
  def queryUserByNameAndPwd(request: LoginUserRequest): LoginUserResponse = {
    val user = DemoDataSource.mysqlData.row[User](
      sql""" select * from user
              where telephone = ${request.telephone}
              and password =${request.passWord}""")
    assert(user.isDefined, dataIsEmpty("登陆失败，账号或密码错误"))
    BeanBuilder.build[LoginUserResponse](user.get)("status" -> UserStatusEnum.findByValue(user.get.userStatus.toInt)): LoginUserResponse
  }
}

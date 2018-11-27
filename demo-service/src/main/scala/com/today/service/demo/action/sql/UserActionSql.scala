package com.today.service.demo.action.sql

import java.util.Date

import com.today.api.demo.scala.enums.{IntegralSourceEnum, IntegralTypeEnum}
import com.today.api.demo.scala.enums.UserStatusEnum
import wangzx.scala_commons.sql._
import com.today.api.demo.scala.request.{ModifyUserRequest, RegisterUserRequest}
import com.today.api.demo.scala.response.{ModifyUserResponse, RegisterUserResponse}
import com.today.service.demo.DemoDataSource
import com.today.service.demo.action.UserActivateAction
import com.today.service.demo.query.sql.UserQuerySql
import org.springframework.transaction.annotation.Transactional

object UserActionSql {

  /**
    * 插入一条用户数据
    *
    * @param request
    * @return
    */
  @Transactional
  def insertUser(request: RegisterUserRequest): Option[RegisterUserResponse] = {

    val userId = DemoDataSource.mysqlData.generateKey[Int](
      sql"""INSERT INTO user
              SET user_name = ${request.userName}, password = ${request.passWord},telephone = ${request.telephone},integral = 0,
              user_status = 0,is_deleted = 0,created_at = now(),updated_at = now(),created_by = 111,updated_by = 111""")

    UserQuerySql.queryUserById(userId) match {
      case None => None
      case Some(x) => Some(
        BeanBuilder
          .build[RegisterUserResponse](x)(
          "status" -> UserStatusEnum.findByValue(x.userStatus.toInt)
        ))
    }
  }

  /**
    * 更新用户资料,完善资料，成为权属会员
    *
    * @param request
    * @return
    */
  def updateUserProfile(request: ModifyUserRequest): Option[ModifyUserResponse] = {

    DemoDataSource.mysqlData.executeUpdate(
      sql"""update
              user set email = ${request.email} , qq = ${request.qq},user_status = ${UserStatusEnum.DATA_PERFECTED.id},
              updated_at = ${new Date},updated_by = ${request.userId} where id = ${request.userId}""")

    val info = UserQuerySql.queryUserById(request.userId)
    info match {
      case None => None
      case Some(x) => Some(
        BeanBuilder
          .build[ModifyUserResponse](x)(
          "status" -> UserStatusEnum.findByValue(x.userStatus.toInt)
        ))
    }
  }

  /**
    * 增加积分流水
    *
    * @param userId         用户id
    * @param increment      增加的积分值（可正可负）
    * @param integralType   积分流水类型
    * @param integralSource 积分来源
    * @param mark           可选状态变更备注,如果不写则是状态的描述
    */
  def InsertIntegralJournal(
                             userId: Int,
                             increment: Int,
                             integralType: IntegralTypeEnum,
                             integralSource: IntegralSourceEnum,
                             mark: String*): Boolean = {

    val remark = mark match {
      case Nil => integralSource.name
      case x => x.reduce(_ + _)
    }

    val currIntegral = UserQuerySql.queryUserById(userId).get.integral

    // 插入一条积分流水
    DemoDataSource.mysqlData.executeUpdate(
      sql"""INSERT INTO integral_journal
              SET user_id = ${userId},integral_type = ${integralType.id},integral_price = ${increment},integral_source =${integralSource.id} ,
              integral = ${currIntegral},created_at = now(),created_by = ${userId},updated_at = now(),updated_by = ${userId},remark = ${remark}"""
    ) != 0
  }

  /**
    * 修改用户积分
    *
    * @param userId    用户id
    * @param increment 增加的积分值（可正可负）
    */
  def changeUserIntegral(userId: Int, increment: Int): Boolean = {

    DemoDataSource.mysqlData.executeUpdate(
      sql"""UPDATE user SET
              integral = integral+${increment}, updated_at = now(),updated_by = ${userId}
              WHERE  id = ${userId} """) != 0
  }


  /**
    * 更新用户状态
    *
    * @param userId         用户id
    * @param userStatusEnum 用户状态
    * @param mark           可选状态变更备注,如果不写则是状态的描述
    * @return
    */
  def updateUserStatus(userId: Int, userStatusEnum: UserStatusEnum, mark: String*): Boolean = {

    val remark = mark match {
      case Nil => userStatusEnum.name
      case x => x.reduce(_ + _)
    }
    DemoDataSource.mysqlData.executeUpdate(
      sql"""update user
              set user_status = ${userStatusEnum.id} , remark = ${remark},updated_by = 111,updated_at = now()
              where id = ${userId}""") != 0
  }

}

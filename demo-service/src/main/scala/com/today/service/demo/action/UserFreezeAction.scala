package com.today.service.demo.action

import com.github.dapeng.core.SoaException
import com.today.api.demo.scala.enums.UserStatusEnum
import com.today.api.demo.scala.request.FreezeUserRequest
import com.today.api.demo.scala.response.FreezeUserResponse
import com.today.commons.UserUtil._
import com.today.service.commons.Assert._
import com.today.service.commons.Action
import com.today.service.commons.exception.CommonException._
import com.today.service.demo.action.sql.UserActionSql
import com.today.service.demo.query.sql.UserQuerySql

/**
  * 冻结账户
  *
  * @param request
  */
class UserFreezeAction(request: FreezeUserRequest) extends Action[FreezeUserResponse] {

  override def preCheck: Unit = {
    /*assert(
      notInStatus(
        UserQuerySql
          .queryUserById(request.userId)
          .get
          .userStatus
          .toInt),
      illegalArgumentException("冻结失败，此用户不在冻结操作范围内!"))*/
  }

  override def action: FreezeUserResponse = {
    UserActionSql
      .updateUserStatus(request.userId, UserStatusEnum.FREEZED, request.remark)
    val userInfo = UserQuerySql.queryUserById(request.userId)
    userInfo match {
      case None => throw new SoaException("100001", "冻结失败")
      case _ => FreezeUserResponse(userInfo.get.id.toString, UserStatusEnum.findByValue(userInfo.get.userStatus.toInt), userInfo.get.remark)
    }
  }
}

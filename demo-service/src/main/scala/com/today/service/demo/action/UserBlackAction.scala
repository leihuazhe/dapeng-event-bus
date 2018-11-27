package com.today.service.demo.action

import com.github.dapeng.core.SoaException
import com.today.api.demo.scala.enums.{IntegralSourceEnum, IntegralTypeEnum, UserStatusEnum}
import com.today.api.demo.scala.request.BlackUserRequest
import com.today.api.demo.scala.response.BlackUserResponse
import com.today.commons.{GenIdUtil, UserUtil}
import com.today.event.EventBus
import com.today.service.commons.Assert._
import com.today.service.commons.Action
import com.today.service.commons.exception.CommonException._
import com.today.service.demo.action.sql.UserActionSql
import com.today.service.demo.query.sql.UserQuerySql
import com.today.user.scala.events.BlackedEvent

/**
  * 拉黑账户
  *
  * @param request
  */
class UserBlackAction(request: BlackUserRequest) extends Action[BlackUserResponse] {

  override def preCheck: Unit = {
    /*assert(
      UserUtil.notInStatus(
        UserQuerySql
          .queryUserById(request.userId)
          .get
          .userStatus
          .toInt),
      illegalArgumentException("冻结失败，此用户不在拉黑操作范围内!"))*/
  }

  override def action: BlackUserResponse = {
    UserActionSql
      .updateUserStatus(request.userId, UserStatusEnum.BLACK, request.remark)

    // 清空积分，会减去当前用户的所有积分
    val integralTotal: Int = -UserQuerySql.queryUserById(request.userId).get.integral
    UserActionSql.changeUserIntegral(request.userId, integralTotal)
    UserActionSql.InsertIntegralJournal(request.userId, integralTotal, IntegralTypeEnum.MINUS, IntegralSourceEnum.BLACK, request.remark)

    val userInfo = UserQuerySql.queryUserById(request.userId)
    val response = userInfo match {
      case None => throw new SoaException("100001", "冻结失败")
      // 少量字段可不必使用BeanBuilder，但必须将字段名加上易知其意
      case Some(x) => BlackUserResponse(
        userId = x.id.toString,
        status = UserStatusEnum.findByValue(x.userStatus.toInt),
        remark = x.remark)
    }
    // fire event
    EventBus.fireEvent(BlackedEvent(GenIdUtil.getEventId(), response.userId.toLong, request.remark))
    response
  }
}

package com.today.service.demo.action

import com.github.dapeng.core.SoaException
import com.today.api.demo.scala.request.RegisterUserRequest
import com.today.api.demo.scala.response.RegisterUserResponse
import com.today.commons.{GenIdUtil, UserUtil}
import com.today.event.EventBus
import com.today.service.commons.Assert._
import com.today.service.commons.Action
import com.today.service.commons.exception.CommonException._
import com.today.service.demo.action.sql.UserActionSql
import com.today.service.demo.query.sql.UserQuerySql
import com.today.user.scala.events.{ActivedEvent, RegisteredEvent}

/**
  * 注册账户
  *
  * @param request
  */
class RegisterUserAction(request: RegisterUserRequest) extends Action[RegisterUserResponse] {

  override def preCheck: Unit = {
    /*assert(UserQuerySql.queryUserByTel(request.telephone).isEmpty, illegalArgumentException("手机号已被使用请检查!"))
    assert(UserUtil.checkName(request.userName), illegalArgumentException("用户名格式有误请检查!"))
    assert(UserUtil.checkPwd(request.passWord), illegalArgumentException("密码格式有误请检查!"))
    assert(UserUtil.checkTel(request.telephone), illegalArgumentException("手机号格式有误请检查!"))*/

    assert(UserUtil.checkName(request.userName), illegalArgumentException("用户名格式有误请检查!"))
  }

  override def action: RegisterUserResponse = {
    val response = UserActionSql.insertUser(request) match {
      case None => throw new SoaException("888", "注册失败,服务器错误")
      case Some(x) => x
    }
    EventBus.fireEventOrdered(request.userName, RegisteredEvent(GenIdUtil.getEventId().toInt, response.id))
    EventBus.fireEventOrdered(request.userName, ActivedEvent(GenIdUtil.getEventId().toInt, response.id))
    response
  }
}

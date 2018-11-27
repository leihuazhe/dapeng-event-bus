package com.today.service.demo.action

import com.today.api.demo.scala.request.LoginUserRequest
import com.today.api.demo.scala.response.LoginUserResponse
import com.today.commons.UserUtil
import com.today.service.commons.Assert._
import com.today.service.commons.Action
import com.today.service.commons.exception.CommonException._
import com.today.service.demo.query.sql.UserQuerySql

/**
  * 账户登录
  *
  * @param request
  */
class UserLoginAction(request: LoginUserRequest) extends Action[LoginUserResponse] {
  override def preCheck: Unit = {
    /* assert(UserUtil.checkTel(request.telephone),illegalArgumentException("账号格式有误!"))
    assert(UserUtil.checkPwd(request.passWord),illegalArgumentException("密码格式有误!"))*/
  }

  override def action: LoginUserResponse = {
    UserQuerySql.queryUserByNameAndPwd(request)
  }
}

package com.today.service.demo.action

import com.github.dapeng.core.SoaException
import com.today.api.demo.scala.enums.{IntegralSourceEnum, IntegralTypeEnum}
import com.today.api.demo.scala.request.ModifyUserRequest
import com.today.api.demo.scala.response.ModifyUserResponse
import com.today.commons.UserUtil
import com.today.service.commons.Assert._
import com.today.service.commons.Action
import com.today.service.commons.exception.CommonException._
import com.today.service.demo.action.sql.UserActionSql

/**
  * 修改账户资料
  *
  * @param request
  */
class UserModifyAction(request: ModifyUserRequest)
  extends Action[ModifyUserResponse] {
  override def preCheck: Unit = {
    /*assert(UserUtil.checkEmail(request.email), illegalArgumentException("邮箱格式有误!"))
    assert(UserUtil.checkQQ(request.qq), illegalArgumentException("QQ格式有误!"))*/
  }

  override def action: ModifyUserResponse = {
    UserActionSql.updateUserProfile(request) match {
      case None => throw new SoaException("888", "资料修改失败!")
      case Some(x) => {
        val profileIntegral: Int = 5
        UserActionSql.changeUserIntegral(request.userId, profileIntegral)
        UserActionSql.InsertIntegralJournal(
          request.userId,
          profileIntegral,
          IntegralTypeEnum.ADD,
          IntegralSourceEnum.PREFECT_INFORMATION)
        x
      }
    }
  }
}

package com.today.service.demo.action

import com.github.dapeng.core.SoaException
import com.today.api.demo.scala.enums.UserStatusEnum
import com.today.service.commons.Action
import com.today.service.demo.action.sql.UserActionSql
import com.today.service.demo.query.sql.UserQuerySql

/**
  * 激活账户
  *
  * @param userId
  */
class UserActivateAction(userId: Int) extends Action[Boolean] {

  override def preCheck: Unit = {}

  override def action: Boolean = {
    UserActionSql
      .updateUserStatus(userId, UserStatusEnum.ACTIVATED)

    val userInfo = UserQuerySql.queryUserById(userId)

    println("用户激活成功")

    userInfo match {
      case None => throw new SoaException("100001", "用户激活失败")
      case Some(x) => x.userStatus.toInt == UserStatusEnum.ACTIVATED.id
    }

  }
}

package com.today.service.demo.action

import com.today.api.demo.scala.request.ChangeIntegralRequest
import com.today.service.commons.Action
import com.today.service.demo.action.sql.UserActionSql
import com.today.service.demo.query.sql.UserQuerySql

/**
  *
  * 描述:
  *
  * @author maple.lei
  * @date 2018年02月08日 下午8:53
  */
class ChangeUserIntegralAction(request: ChangeIntegralRequest) extends Action[Int] {
  override def preCheck: Unit = {}

  override def action: Int = {
    UserActionSql.InsertIntegralJournal(
      request.userId,
      request.integralPrice,
      request.integralType,
      request.integralSource)

    UserQuerySql.queryUserById(request.userId).get.integral
  }
}

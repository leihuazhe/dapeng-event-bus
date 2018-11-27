package com.today.service.demo.query

import com.today.api.demo.scala.request.FindUserByPageRequest
import com.today.api.demo.scala.response.FindUserByPageResponse
import com.today.service.commons.Assert._
import com.today.service.commons.Query
import com.today.service.commons.exception.CommonException._
import com.today.service.demo.query.sql.UserQuerySql

class FindUserByPageQuery(request: FindUserByPageRequest) extends Query[FindUserByPageResponse] {
  override def preCheck: Unit = {
    assert(request.pageRequest.start >= 0, illegalArgumentException("开始页数不能小于0!"))
  }

  override def action: FindUserByPageResponse = {
    UserQuerySql.queryUserByPage(request)
  }
}

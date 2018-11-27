package com.today.service.demo

import javax.annotation.Resource
import javax.sql.DataSource

/**
  * dataSource
  * 可多数据源配置
  */
object DemoDataSource {
  var mysqlData: DataSource = _
}

class DemoDataSource {
  @Resource(name = "tx_demo_dataSource")
  def setDataSource(mysqlData: DataSource): Unit = {
    DemoDataSource.mysqlData = mysqlData
  }
}


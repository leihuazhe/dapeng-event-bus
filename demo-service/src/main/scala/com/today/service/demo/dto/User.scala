package com.today.service.demo.dto

/**
  * 用户dto
  **/
case class User(

                 /**
                   *
                   **
                 利用主键策略生成的唯一键
                   *
                   **/

                 id: Int,

                 /**
                   *
                   **
                 用户名
                   *
                   **/

                 userName: String,

                 /**
                   *
                   **
                 密码
                   *
                   **/

                 password: String,

                 /**
                   *
                   **
                 手机号码
                   *
                   **/

                 telephone: String,

                 /**
                   *
                   **
                 积分
                   *
                   **/

                 integral: Int,

                 /**
                   *
                   **
                 创建时间
                   *
                   **/

                 createdAt: java.sql.Timestamp,

                 /**
                   *
                   **
                 特指后台创建人(公司员工 id)
                   *
                   **/

                 createdBy: Int,

                 /**
                   *
                   **
                 更新时间
                   *
                   **/

                 updatedAt: java.sql.Timestamp,

                 /**
                   *
                   **
                 特指后台更新人(公司员工 id)
                   *
                   **/

                 updatedBy: Int,

                 /**
                   *
                   **
                 备注
                   *
                   **/

                 remark: String,

                 /**
                   *
                   **
                 用户状态
                   *
                   **/

                 userStatus: String,

                 /**
                   *
                   **
                 数据状态,是否删除
                   *
                   **/

                 isDeleted: String,

                 /**
                   *
                   **
                 邮箱
                   *
                   **/

                 email: Option[String] = None,

                 /**
                   *
                   **
                 qq
                   *
                   **/

                 qq: Option[String] = None
               )

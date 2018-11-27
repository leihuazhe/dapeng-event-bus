package com.today.commons

import com.today.api.demo.scala.enums.UserStatusEnum
import com.today.service.commons.`implicit`.Implicits._

object UserUtil {

  private val TEL_PATTERN = """1(([3,5,8]\d{9})|(4[5,7]\d{8})|(7[0,6-8]\d{8}))""".r
  private val EMAIL_PATTERN = """(?i)[a-z0-9._-]+@[a-z0-9._-]+(\.[a-z0-9._-]+)+""".r
  private val PWD_PATTERN = """^[0-9a-zA-Z]\w{5,16}$""".r
  private val QQ_PATTERN = """^\d{6,11}$""".r
  private val NAME_PATTERN = """^.{3,16}$""".r

  // 拉黑冻结排除状态列表
  private val outStatuses = List(UserStatusEnum.BLACK.id, UserStatusEnum.DELETE.id, UserStatusEnum.FREEZED.id)

  /**
    * 用户名验证
    *
    * @param name
    * @return
    */
  def checkName(name: String): Boolean = {
    NAME_PATTERN.findAllIn(name).nonEmpty
  }

  /**
    * 验证邮箱
    *
    * @param email
    * @return
    */
  def checkEmail(email: String): Boolean = {
    EMAIL_PATTERN.findAllIn(email).nonEmpty
  }

  /**
    * qq验证
    *
    * @param qq
    * @return
    */
  def checkQQ(qq: String): Boolean = {
    QQ_PATTERN.findAllIn(qq).nonEmpty
  }

  /**
    * 验证手机
    *
    * @param tel
    * @return
    */
  def checkTel(tel: String): Boolean = {
    TEL_PATTERN.findFirstIn(tel).nonEmpty
  }

  /**
    * 验证密码格式
    *
    * @param pwd
    * @return
    */
  def checkPwd(pwd: String): Boolean = {
    PWD_PATTERN.findAllIn(pwd).nonEmpty
  }

  /**
    * 排除用户状态 true 不在范围内，false 在范围内
    *
    * @param status
    * @return
    */
  def notInStatus(status: Int): Boolean = {
    status.notIn(outStatuses)
  }
}

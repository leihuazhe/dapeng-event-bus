package com.today.service.demo

import com.today.api.demo.scala.enums.{IntegralSourceEnum, IntegralTypeEnum}
import com.today.api.demo.scala.request._
import com.today.api.demo.scala.response._
import com.today.api.demo.scala.service.UserService
import com.today.service.demo.action._
import com.today.service.demo.query.FindUserByPageQuery
import org.slf4j.LoggerFactory
import org.springframework.transaction.annotation.Transactional

@Transactional(rollbackFor = Array(classOf[Exception]))
class UserServiceImpl extends UserService {
  val logger = LoggerFactory.getLogger(classOf[UserServiceImpl])

  /**
    * 注册用户
    *
    * @param request
    * @return
    */
  override def registerUser(request: RegisterUserRequest): RegisterUserResponse = {
    new RegisterUserAction(request).execute
  }

  /**
    * 用户登录
    *
    * @param request
    * @return
    */
  @Transactional(readOnly = true)
  override def login(request: LoginUserRequest): LoginUserResponse = {
    new UserLoginAction(request).execute
  }

  /**
    * 修改用户资料
    *
    * @param request
    * @return
    */
  override def modifyUser(request: ModifyUserRequest): ModifyUserResponse = {
    new UserModifyAction(request).execute
  }

  /**
    * 冻结用户
    *
    * @param request
    * @return
    */
  override def freezeUser(request: FreezeUserRequest): FreezeUserResponse = {
    new UserFreezeAction(request).execute
  }

  /**
    * 拉黑用户
    *
    * @param request
    * @return
    */
  override def blackUser(request: BlackUserRequest): BlackUserResponse = {
    new UserBlackAction(request).execute
  }

  /**
    * 修改积分事件
    */
  def registerUserIntegral(userId: Int): Unit = {
    changeUserIntegral(ChangeIntegralRequest(userId, 1000, IntegralTypeEnum.ADD, IntegralSourceEnum.PREFECT_INFORMATION))
  }


  /**
    * 修改用户积分
    *
    * @param request
    * @return
    */
  override def changeUserIntegral(request: ChangeIntegralRequest): Int = {

    new ChangeUserIntegralAction(request).execute
  }

  /**
    * 分页获取用户信息
    *
    * @param request
    * @return
    */
  @Transactional(readOnly = true)
  override def findUserByPage(request: FindUserByPageRequest): FindUserByPageResponse = {
    new FindUserByPageQuery(request).execute
  }

  /**
    *
    * *
    * 测试优雅关机
    *
    **/
  override def testGraceful(): String = {
    Thread.sleep(10000)

    "休眠10s,优雅关机 " + System.currentTimeMillis()
  }


}

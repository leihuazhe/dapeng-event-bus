namespace java com.today.api.demo.request

include 'demo_enum.thrift'
include 'demo_page.thrift'

/**
* 注册用户请求体
**/
struct RegisterUserRequest {
    /**
    * 用户名称
    **/
    1: string userName,
    /**
    * 密码
    **/
    2: string passWord,
    /**
    * 电话号码
    **/
    3: string telephone,
}

/**
* 修改用户请求体
**/
struct ModifyUserRequest {
  /**
    * 用户 id
    **/
    1: i32 userId,
    /**
    * 用户邮箱
    **/
    2: string email,
    /**
    * 用户 qq
    **/
    3: string qq,
}

/**
* 用户登录请求体
**/
struct LoginUserRequest {
    /**
    * 密码
    **/
    1: string passWord,
    /**
    * 电话号码
    **/
    2: string telephone,
}

/**
* 冻结操作请求体
**/
struct FreezeUserRequest {
    /**
    * 用户 id
    **/
    1: i32 userId,
    /**
    *  操作员冻结备注
    **/
    2: string remark,
}

/**
* 拉黑操作请求体
**/
struct BlackUserRequest {
    /**
    * 用户 id
    **/
    1: i32 userId,
    /**
    *  操作员拉黑备注
    **/
    2: string remark,
}

/**
* 记录请求流水接口
**/
struct ChangeIntegralRequest {
    /**
    * 用户 id
    **/
    1: i32 userId,
    /**
    *  该流水涉及到的积分数(可正可负)
    **/
    2: i32 integralPrice,
    /**
    *  积分流水类型
    **/
    3: demo_enum.IntegralTypeEnum integralType,
    /**
    *  积分改变的来源
    **/
    4: demo_enum.IntegralSourceEnum integralSource,
}


/**
* 分页查询用户接口
**/
struct FindUserByPageRequest {
    /**
    * 用户 id
    **/
    1: demo_page.TPageRequest pageRequest,
    /**
    *  积分数
    **/
    2: optional i32 integral,
    /**
    *  用户类型
    **/
    3: optional demo_enum.UserStatusEnum userStatus,
}

namespace java com.today.user.events

/**
* 注册成功事件,
**/
struct RegisteredEvent {
    /**
    * 事件Id
    **/
    1: i64 id,
    /**
    * 用户id
    **/
    2: i64 userId
}

/**
* 激活事件
**/
struct ActivedEvent {
    /**
    * 事件Id
    **/
    1: i64 id,
    /**
    * 用户ID
    **/
	2: i64 userId
}

/**
* 拉黑事件
**/
struct BlackedEvent {
    /**
    * 事件Id
    **/
    1: i64 id,
	/**
	* 用户ID
	**/
	2: i64 userId,
	/**
	* 备注
	**/
	3: string remark
}
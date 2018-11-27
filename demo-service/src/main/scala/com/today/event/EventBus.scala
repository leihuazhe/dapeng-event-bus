package com.today.event

import com.today.eventbus.AbstractEventBus
import org.slf4j.LoggerFactory

/**
  *
  * 描述: EventBus
  *
  * @author hz.lei
  * @date 2018年04月23日 下午6:27
  */
object EventBus extends AbstractEventBus {

  private val LOGGER = LoggerFactory.getLogger(getClass)

  /**
    * 本地事件订阅分发
    * 所有事件在触发后，可能存在本地的订阅者，以及跨领域的订阅者
    * 本地订阅者可以通过实现该方法来执行事件结果的业务逻辑
    * 同时,也会将事件发送到其他领域的事件消息订阅者
    *
    * @param event
    */
  override def dispatchEvent(event: Any): Unit = {
    event match {
      case _ =>
        LOGGER.info(" local  nothing to do ")
    }
  }

  override def getInstance: EventBus.this.type = this

}

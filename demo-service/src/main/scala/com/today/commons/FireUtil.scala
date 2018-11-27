package com.today.commons

import java.util.concurrent.CompletableFuture

import com.today.event.EventBus
import com.today.user.scala.events.{ActivedEvent, RegisteredEvent}
import javax.annotation.PostConstruct

/**
  *
  * @author <a href=mailto:leihuazhe@gmail.com>maple</a>
  * @since 2018-11-22 1:23 PM
  */
class FireUtil {
  val randomS = List("A", "A", "A", "B", "B")

//  @PostConstruct
  def test(): Unit = {
    CompletableFuture.runAsync(() ⇒ {
      for (i <- 0 to 10000) {
        randomS(i % randomS.size)
        EventBus.fireEvent(randomS(i % randomS.size), RegisteredEvent(GenIdUtil.getEventId().toInt, i))
        EventBus.fireEvent(randomS(i % randomS.size), ActivedEvent(GenIdUtil.getEventId().toInt, i))
        Thread.sleep(100)
      }
    })


  }

}

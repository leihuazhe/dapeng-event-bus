package com.today.commons

import com.today.soa.idgen.scala.IDServiceClient
import com.today.soa.idgen.scala.cache.IDCacheClient
import com.today.soa.idgen.scala.domain.GenIDRequest

/**
  *
  * 描述: GenIdUtil
  *
  * @author hz.lei
  * @date 2018年04月23日 下午8:18
  */
object GenIdUtil {

  val eventID = "event_id"
  val client = new IDServiceClient


  def getEventId(): Long = client.genId(GenIDRequest(eventID, 1))

}

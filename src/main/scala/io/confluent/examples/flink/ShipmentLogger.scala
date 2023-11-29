package io.confluent.examples.flink

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper
import org.apache.flink.streaming.api.functions.sink.SinkFunction

import java.util.logging.Logger

class ShipmentLogger extends SinkFunction[String] {

  val mapper = new ObjectMapper()
  // Note using a trait for getting the logger will throw an Exception when running on the Flink Cluster
  // Also note that using lazy val will throw an Exception on the Flink cluster.
  @transient
  private val log = Logger.getLogger(getClass.getName)

  override def invoke(shipmentString: String, context: SinkFunction.Context): Unit = {
    val shipment = mapper.readValue[Shipment](shipmentString, classOf[Shipment])
    log.info(shipmentString)
    log.info("time since order: " + (System.currentTimeMillis() - shipment.order.ordertime))
  }

}

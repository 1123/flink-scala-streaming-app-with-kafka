package io.confluent.examples.flink

import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper
import org.scalatest.funsuite.AnyFunSuite

class ShipmentLoggerTest extends AnyFunSuite:

  test( "Order.toString") {
    val shipment = Shipment(
      Order(0l, 0, "foo", Address("foo", "bar", 0)),
      0l)
    // logger.info(new ObjectMapper().writeValueAsString(shipment))
  }

  test("ShipmentLogger.invoke") {
    new ShipmentLogger().invoke("{\"order\":{\"ordertime\":0,\"orderid\":0,\"itemid\":\"foo\",\"address\":{\"city\":\"foo\",\"state\":\"bar\",\"zipcode\":0}},\"time\":0}", null)
  }

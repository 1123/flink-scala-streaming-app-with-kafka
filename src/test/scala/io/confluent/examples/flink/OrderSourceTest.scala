package io.confluent.examples.flink

import io.confluent.examples.flink.OrderSource
import org.apache.flink.streaming.api.functions.source.SourceFunction
import org.apache.flink.streaming.api.functions.source.SourceFunction.SourceContext
import org.apache.flink.streaming.api.watermark.Watermark
import org.scalatest.funsuite.AnyFunSuite

/**
 * This is just for testing the testing framework :-)
 */

class OrderSourceTest extends AnyFunSuite:
  test("OrderSource.run") {
    // logger.info("Test ist starting")
    val orderSource = new OrderSource()
    val thread = new Thread {
      override def run(): Unit = {
        orderSource.run(new MyOrderSourceContext())
      }
    }
    thread.start()
    Thread.sleep(2000)
    orderSource.cancel()
    thread.join()
    // logger.info("Test finished")
  }

class MyOrderSourceContext extends SourceFunction.SourceContext[Order] {
  override def collect(element: Order): Unit =
    val foo = "bar"
    // logger.info(element.toString)

  override def collectWithTimestamp(element: Order, timestamp: Long): Unit = ???

  override def emitWatermark(mark: Watermark): Unit = ???

  override def markAsTemporarilyIdle(): Unit = ???

  override def getCheckpointLock: AnyRef = ???

  override def close(): Unit = ???
}
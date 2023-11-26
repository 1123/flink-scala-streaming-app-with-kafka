package io.confluent.examples.flink

import scala.beans.BeanProperty

case class Order(
                  @BeanProperty ordertime: Long,
                  @BeanProperty orderid: Int,
                  @BeanProperty itemid: String,
                  @BeanProperty address: Address
                ) {
  def this() = this(0L, 0, null, null)
}

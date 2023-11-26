package io.confluent.examples.flink

import scala.beans.BeanProperty

case class Address(
                    @BeanProperty city: String,
                    @BeanProperty state: String,
                    @BeanProperty zipcode: Int
                  ) {
  def this() = this(null, null, 0)
}

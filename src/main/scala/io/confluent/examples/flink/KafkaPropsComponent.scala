package io.confluent.examples.flink

import java.util.Properties

trait KafkaPropsComponent {
  @transient
  val kafkaProps: Properties

}

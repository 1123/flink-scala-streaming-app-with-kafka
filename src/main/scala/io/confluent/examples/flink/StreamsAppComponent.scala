package io.confluent.examples.flink

import com.typesafe.scalalogging.LazyLogging
import org.apache.flink.api.common.eventtime.BoundedOutOfOrdernessWatermarks
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.connector.base.DeliveryGuarantee
import org.apache.flink.connector.kafka.sink.{KafkaRecordSerializationSchema, KafkaSink}
import org.apache.flink.connector.kafka.source.KafkaSource
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer
import org.apache.flink.connector.kafka.source.reader.deserializer.KafkaRecordDeserializationSchema
import org.apache.flink.shaded.jackson2.com.fasterxml.jackson.databind.ObjectMapper
import org.apache.flink.streaming.api.datastream.DataStream
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment
import org.apache.flink.streaming.api.functions.ProcessFunction
import org.apache.flink.util.Collector

import java.time.Duration

trait StreamsAppComponent {
  this: KafkaPropsComponent =>
  val streamsApp: StreamsApp

  // TODO: no idea, why this needs to implement Serializable. It seems like Flink somehow stores instances of the
  // StreamsApp for recovery purposes?
  // TODO: the following approach may help:
  //  https://stackoverflow.com/questions/61128734/the-implementation-of-the-mapfunction-is-not-serializable-flink
  class StreamsApp extends Serializable with LazyLogging {
    // TODO: topic names should be in the configuration
    val ordersTopic = "flink-orders"
    val shipmentTopic = "flink-shipments"

    def run() = {
      logger.info("Kafka Properties: \n {}",  kafkaProps)

      val env: StreamExecutionEnvironment = StreamExecutionEnvironment.getExecutionEnvironment

      val generatorSource: DataStream[Order] = env.addSource(new OrderSource()).name("order-generator")

      // order-generator -> process -> ordersSink -> "flink-orders" -> ordersSource ->
      // -> orders -> OrderDispatcher -> shipments -> shipmentLogger
      //                                    +--------> shipmentSink --> "flink-shipments"

      val ordersSink : KafkaSink[String] = KafkaSink.builder()
        .setKafkaProducerConfig(kafkaProps)
        .setRecordSerializer(KafkaRecordSerializationSchema.builder()
          .setTopic(ordersTopic)
          .setValueSerializationSchema(new SimpleStringSchema())
          .build()
        )
        .setDeliveryGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
        .build();

      val objectMapper = new ObjectMapper()

      generatorSource.process(
        (value: Order, _: ProcessFunction[Order, String]#Context, out: Collector[String]) => {
          out.collect(objectMapper.writeValueAsString(value))
        }
      ).sinkTo(ordersSink)

      val ordersSource: KafkaSource[Order] = KafkaSource.builder[Order]
        .setProperties(kafkaProps)
        .setTopics(ordersTopic)
        .setGroupId("order-reader")
        .setStartingOffsets(OffsetsInitializer.earliest)
        // TODO: we should use some kind of schemas
        .setDeserializer(KafkaRecordDeserializationSchema.valueOnly(classOf[OrderDeserializer]))
        .build

      val orders: DataStream[Order] = env.fromSource(
        ordersSource,
        _ =>
          new BoundedOutOfOrdernessWatermarks[Order](Duration.ofSeconds(10))
        , "KafkaSource"
      )
      val shipments: DataStream[Shipment] = orders
        .process(new OrderDispatcher)
        .name("ShipmentProcessor")

      val serialized: DataStream[String] = shipments.process((value: Shipment, _: ProcessFunction[Shipment, String]#Context, out: Collector[String]) => {
        out.collect(new ObjectMapper().writeValueAsString(value))
      })

      serialized.addSink(new ShipmentLogger)
        .name("ShipmentLogger")

      val shipmentSink = KafkaSink.builder()
        .setKafkaProducerConfig(kafkaProps)
        .setRecordSerializer(KafkaRecordSerializationSchema.builder()
          .setTopic(shipmentTopic)
          .setValueSerializationSchema(new SimpleStringSchema())
          .build()
        )
        .setDeliveryGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
        .build();

      serialized.sinkTo(shipmentSink)

      logger.info("Starting Streaming Job")
      env.execute("Kafka Streaming Job")
    }
  }
}

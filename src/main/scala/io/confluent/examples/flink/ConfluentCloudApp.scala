package io.confluent.examples.flink

import java.util.Properties
import java.util.logging.Logger

object ConfluentCloudApp extends KafkaPropsComponent with StreamsAppComponent with Serializable {

  val kafkaProps = new Properties()

  val streamsApp = new StreamsApp

  def main(args: Array[String]): Unit = {
    // TODO: could also use require.
    val log = Logger.getLogger(getClass.getName)
    assert(
      args.length == 3,
      "Usage: provide three arguments: 1) bootstrap server address; 2) api-key; 3) api-secret;  "
    )
    log.info("Arguments provided:")
    log.info(args.length + "")
    log.info(args(0) + " " + args(1) + " " + args(2))
    kafkaProps.put("bootstrap.servers", args(0))
    kafkaProps.put("ssl.endpoint.identification.algorithm", "https")
    kafkaProps.put("sasl.mechanism", "PLAIN")
    kafkaProps.put("security.protocol", "SASL_SSL")
    kafkaProps.put("sasl.jaas.config",
      String.format(
        "org.apache.kafka.common.security.plain.PlainLoginModule required username='%s' password='%s';",
        args(1),
        args(2)
      ))
    streamsApp.run()
    // "org.apache.kafka.common.security.plain.PlainLoginModule required username='' password='';"
  }

}

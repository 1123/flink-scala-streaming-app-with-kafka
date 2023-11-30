## Flink Kafka Scala Sample Stream

A simple Flink Streaming Scala application communicating via Kafka topics. 

### Features 

* This application can both run within an Embedded Flink Cluster as well as on a real Flink Cluster. 
* This repository provides a sample docker-compose setup to spin up a cluster and deployment scripts for the application on that local cluster. 
* The application can either connect to a local Kafka cluster or to a Confluent Cloud Kafka Cluster. 
* Native Scala Dependency Injection for better testablility and environment support. 
* Directly uses the Flink Java Streaming API. The Flink Scala API has been deprecated. 

### Overview Diagram

![App Overview](overview.png)

### Running the App

#### Prerequisites

* A recent version of the Scala Build Tool (SBT) installed
* Confluent Platform installed for running locally
* A Confluent Cloud Account for running on Confluent Cloud
* A recent version of Java
* Linux or Mac environment for running the start scripts. 

#### Running Within an Embedded Flink Cluster Against Local Kafka

For running with a local Kafka cluster and an embedded Flink Cluster, 
start the `LocalApp` class from within your IDE.
* This class expects a Kafka broker listening on `localhost:9092` without authentication and authorization
* Topics will be auto-created when the app is started. 
  * You may need to start the app twice.
* No need to set environment variables
* Alternatively, you can run the `./run-against-local-kafka.sh` on a Mac. 
  * You may need to set the `JAVA_HOME` environment variable appropriately. 

#### Running on an Embedded Flink Cluster against Confluent Cloud

For running with a Kafka Cluster in Confluent Cloud, follow these steps:
* Create the topics `flink-orders` and `flink-shipments`.
* Create an API Key and API Secret for Confluent Cloud
* Set the following environment variables: 
  * `export BOOTSTRAP_SERVERS=pkc-...confluent.cloud:9092`
  * `export API_KEY=...`
  * `export API_SECRET=...`
* Then run the class `ConfluentCloudApp` from within your IDE
  * You may need to add some Java21 JVM options as specified in `run-against-confluent-cloud.sh` 
* Alternatively, run the script `run-against-confluent-cloud.sh`. 

#### Running on Real Flink Cluster

You can spin up a flink cluster with docker-compose, or you may want to use a flink cluster deployed within 
the Cloud provider of your choice. 

To deploy the app on the Flink cluster, do the following: 
* Build the fat jar: `sbt clean assembly`
* Upload the jar: `upload-jar.sh`
* Run the job: `submit-job.sh`

You will need to adjust the REST API Endpoint of your Flink cluster in the above scripts, 
and possibly also provide flink authentication credentials. 
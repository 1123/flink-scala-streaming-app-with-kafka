sbt "runMain io.confluent.examples.flink.ConfluentCloudApp $BOOTSTRAP_SERVERS $API_KEY $API_SECRET" \
    -J--add-opens=java.base/java.lang=ALL-UNNAMED \
    -J--add-opens=java.base/java.util=ALL-UNNAMED

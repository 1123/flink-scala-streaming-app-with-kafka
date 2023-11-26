sbt "runMain io.confluent.examples.flink.LocalApp" \
    -J--add-opens=java.base/java.lang=ALL-UNNAMED \
    -J--add-opens=java.base/java.util=ALL-UNNAMED

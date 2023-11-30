#!/bin/bash

set -u -e 

echo "Uploading jar"

FILENAME=$(curl -X POST \
  -H "Expect:" \
  -F "jarfile=@target/scala-3.1.3/ScalaFlinkStreamingApp-assembly-0.1.0-SNAPSHOT.jar" http://localhost:8081/jars/upload | jq -r .filename)

JARNAME=${FILENAME#*upload/}

echo "Extracted Jar name: $JARNAME"


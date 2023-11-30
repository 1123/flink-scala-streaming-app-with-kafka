#!/bin/bash

set -u -e 

JARNAME=$1

generate_post_data()
{
  cat <<EOF
{  
  "entryClass":"$ENTRY_CLASS",   
  "programArgsList": [ 
    "$BOOTSTRAP_SERVERS",
    "$API_KEY",
    "$API_SECRET"
  ],
  "parallelism": 2
}
EOF
}

set -x 
curl -k -v -X POST \
  -H "Content-Type: application/json" \
  --data "$(generate_post_data)" http://localhost:8081/jars/$JARNAME/run 

set +x


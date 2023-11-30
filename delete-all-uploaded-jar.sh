curl localhost:8081/jars | jq -r '.files | .[] | .id' | while read jar; do curl -X DELETE http://localhost:8081/jars/$jar; done

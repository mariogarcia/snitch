#!/bin/sh

mirror=$(curl --stderr /dev/null https://www.apache.org/dyn/closer.cgi\?as_json\=1 | jq -r '.preferred')
echo "${mirror}"
url="${mirror}spark/spark-${SPARK_VERSION}/spark-${SPARK_VERSION}.tgz"
echo "${url}"
wget "${url}" -O "/tmp/spark_${SPARK_VERSION}.tgz"

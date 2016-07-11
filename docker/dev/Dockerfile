FROM mgg/jvm:8

USER root

ENV KAFKA_VERSION="0.10.0.0" SCALA_VERSION="2.11"

COPY download-kafka.sh /tmp/download-kafka.sh

RUN apt-get install -yq jq
RUN mkdir -p /home/dev/tools/ && \
    chmod +x /tmp/download-kafka.sh && \
    /tmp/download-kafka.sh && \
    tar xfz /tmp/kafka_${SCALA_VERSION}-${KAFKA_VERSION}.tgz -C /home/dev/tools/ && rm /tmp/kafka_${SCALA_VERSION}-${KAFKA_VERSION}.tgz

USER dev

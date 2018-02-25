FROM ubuntu
RUN apt-get update
RUN apt-get -y install apt-utils
RUN apt-get -y install net-tools #temp
RUN apt-get -y install openjdk-8-jdk
RUN apt-get -y install wget

RUN mkdir /usr/local/kafka

RUN wget http://apache.cp.if.ua/kafka/1.0.0/kafka_2.12-1.0.0.tgz && \
    tar -xzvf kafka_2.12-1.0.0.tgz && \
    mv kafka_2.12-1.0.0 /usr/local/kafka && \
    rm kafka_2.12-1.0.0.tgz

ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64

ARG NODEID
ARG ZOOKEEPER_CONNECT_STRING

EXPOSE 9092

RUN sed -i "/broker.id=0/c\broker.id=$NODEID" /usr/local/kafka/kafka_2.12-1.0.0/config/server.properties
RUN sed -i "/zookeeper.connect=localhost:2181/c\zookeeper.connect=$ZOOKEEPER_CONNECT_STRING" /usr/local/kafka/kafka_2.12-1.0.0/config/server.properties
RUN sed -i "/#listeners=PLAINTEXT:\/\/:9092/c\listeners=PLAINTEXT:\/\/:9092" /usr/local/kafka/kafka_2.12-1.0.0/config/server.properties

CMD /usr/local/kafka/kafka_2.12-1.0.0/bin/kafka-server-start.sh /usr/local/kafka/kafka_2.12-1.0.0/config/server.properties
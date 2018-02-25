FROM ubuntu
RUN apt-get update
RUN apt-get -y install apt-utils
RUN apt-get -y install net-tools #temp
RUN apt-get -y install openjdk-8-jdk
RUN apt-get -y install wget

RUN mkdir /usr/local/zookeeper

RUN wget http://apache.cp.if.ua/zookeeper/stable/zookeeper-3.4.10.tar.gz && \
    tar -xzvf zookeeper-3.4.10.tar.gz && \
    mv zookeeper-3.4.10 /usr/local/zookeeper && \
    rm zookeeper-3.4.10.tar.gz

ENV JAVA_HOME=/usr/lib/jvm/java-8-openjdk-amd64

COPY zoo.cfg /usr/local/zookeeper/zookeeper-3.4.10/conf

RUN mkdir /usr/local/zookeeper/zookeeper-3.4.10/bin/data

EXPOSE 2181

ARG NODEID

RUN echo $NODEID > /usr/local/zookeeper/zookeeper-3.4.10/bin/data/myid

CMD /usr/local/zookeeper/zookeeper-3.4.10/bin/zkServer.sh start-foreground
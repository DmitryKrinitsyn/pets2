FROM ubuntu
RUN apt-get update
RUN apt-get -y install net-tools #temp
RUN apt-get -y install openjdk-8-jdk
RUN echo "deb http://www.apache.org/dist/cassandra/debian 39x main" | tee -a /etc/apt/sources.list.d/cassandra.sources.list
RUN apt-get -y install curl
RUN curl https://www.apache.org/dist/cassandra/KEYS | apt-key add -
RUN apt-get update
RUN apt-key adv --keyserver pool.sks-keyservers.net --recv-key A278B781FE4B2BDA
RUN apt-get -y install cassandra
RUN apt-get -y install maven
RUN apt-get -y install git
RUN apt-get -y install iputils-ping #temp
RUN cd /tmp/ \
    && git clone https://github.com/Stratio/cassandra-lucene-index \
    && cd cassandra-lucene-index \
	&& git checkout 3.9.6 \
	&& mvn clean package \
	&& cp plugin/target/cassandra-lucene-index-plugin-3.9.6.jar /usr/share/cassandra/lib/

EXPOSE 7000 9042

COPY cassandra_not_seed2.yaml /etc/cassandra/cassandra.yaml

CMD cassandra -R \
    && sleep 10 && echo 10sec && sleep 10 && echo 20sec && sleep 10 && echo 30sec && sleep 10 && echo 40sec && sleep 10 && echo 50sec && sleep 10 && echo 60sec \
    && bash
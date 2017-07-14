FROM mapredbase

COPY slaves $HADOOP_HOME/etc/hadoop/slaves
COPY mapred-site.xml $HADOOP_HOME/etc/hadoop/mapred-site.xml
COPY mapred-site.xml $HADOOP_HOME/etc/hadoop/mapred-site.xml
COPY namenode_start.sh /tmp/namenode_start.sh
RUN chmod 777 /tmp/namenode_start.sh
COPY input.txt /tmp/input/input.txt
COPY *.jar /tmp/

RUN wget http://central.maven.org/maven2/org/apache/parquet/parquet-tools/1.8.1/parquet-tools-1.8.1.jar && \
    mv parquet-tools-1.8.1.jar /tmp/parquet-tools.jar

WORKDIR /usr/local/hadoop/hadoop-2.7.3/sbin

CMD service ssh start && ../bin/hdfs namenode -format &&  bash
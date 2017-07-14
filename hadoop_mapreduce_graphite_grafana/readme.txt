1. Build docker image "DockerfileBaseHdfs", which is common for both name- and data- nodes.
    docker build -t mapredbase --rm --no-cache -f DockerfileBaseHdfs .

2. Build java sources
    maven compiler:compile
    maven jar:jar

3. Put built jar into namenode docker context folder

4. Run containers with docker-compose (navigate to corresponding folder before)
    docker-compose up --build

5. Within namenode container, navigate into /tmp and run hadoop infrastructure
    ./namenode_start.sh

6. Put input data into hdfs
    hadoop fs -mkdir /input && hadoop fs -copyFromLocal /tmp/input/input.txt /input/input.txt

7. Run map-reduce job
    hadoop jar mapred-1.0-SNAPSHOT.jar SspMapReduceParquet /input /output1

8. Wait and observe the results (it is binary format, but text fields are still observable):
    hadoop fs -cat /output1/part-r-00000.parquet
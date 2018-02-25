package edu.dkrinitsyn.kafka;

import org.apache.kafka.clients.admin.*;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Arrays;
import java.util.Collection;
import java.util.Properties;
import java.util.concurrent.*;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class KafkaTest {

    @Test
    public void test1_create_topic() throws ExecutionException, InterruptedException {

        Properties props = new Properties();
        //props.put("metadata.broker.list","localhost:9092, localhost:9093, localhost:9094");
        props.put("bootstrap.servers","localhost:9092,localhost:9093,localhost:9094");
        props.put("serializer.class","kafka.serializer.StringEncoder");
        props.put("request.required.acks", "1");

        AdminClient client = AdminClient.create(props);

        NewTopic topic = new NewTopic("test", 1, (short)1);

        CreateTopicsResult createResult = client.createTopics(Arrays.asList(topic));

        client.close();
    }

    @Test
    public void test2_create_topic() throws ExecutionException, InterruptedException, TimeoutException {

        Properties props = new Properties();
        //props.put("metadata.broker.list","localhost:9092, localhost:9093, localhost:9094");
        props.put("bootstrap.servers","localhost:9092,localhost:9093,localhost:9094");
        //props.put("serializer.class","kafka.serializer.StringEncoder");
        //props.put("request.required.acks", "1");

        AdminClient client = AdminClient.create(props);

        ListTopicsOptions lto = new ListTopicsOptions();

        lto.listInternal(true);
        lto.timeoutMs(1000);

        client.describeCluster();

        Collection<TopicListing> listResult = client.listTopics(lto).listings().get(2, TimeUnit.MINUTES);

        System.out.println(listResult);
    }

    @Test
    public void test2_write_messages() throws ExecutionException, InterruptedException {
        Properties props = new Properties();
        //props.put("metadata.broker.list","localhost:9092, localhost:9093, localhost:9094");
        props.put("bootstrap.servers","localhost:9093");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        props.put("auto.commit.interval", 100);
        props.put("linger.ms",1);

        props.put("retries",2);


        props.put("batch.size", 1);

        //props.put("request.required.acks", "1");

        Producer<String, String> producer = new KafkaProducer<>(props);

        Future<RecordMetadata> f = producer.send(new ProducerRecord<String, String>("test-topic", "2134", "q11we"));

        producer.flush();

        System.out.println(f.get());

        producer.close();


    }

    @Test
    public void test3_read_messages(){

    }
}

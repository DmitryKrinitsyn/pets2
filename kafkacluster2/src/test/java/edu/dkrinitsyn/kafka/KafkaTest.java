package edu.dkrinitsyn.kafka;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.admin.CreateTopicsResult;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.*;

import static org.hamcrest.MatcherAssert.assertThat;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class KafkaTest {

    private static final String TOPIC_NAME = "test-topic";

    Properties props = new Properties();

    public KafkaTest(){
        props.put("bootstrap.servers","localhost:9092");
        props.put("serializer.class","kafka.serializer.StringEncoder");
        props.put("request.required.acks", "1");
    }

    @Test
    public void test1_create_topic() throws ExecutionException, InterruptedException {
        AdminClient client = AdminClient.create(props);

        NewTopic topic = new NewTopic(TOPIC_NAME, 1, (short)1);
        CreateTopicsResult createResult = client.createTopics(Arrays.asList(topic));
        createResult.all().get();
        client.close();

        assertThat("Topic created", topicExists(TOPIC_NAME));
    }

    @Test
    public void test2_write_messages() throws ExecutionException, InterruptedException, TimeoutException {
        Producer<String, String> producer = new KafkaProducer<>(props);

        producer.send(new ProducerRecord<String, String>(TOPIC_NAME,"key","msg")).get();
    }

    @Test
    public void test3_read_messages(){
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(props);

        ConsumerRecords<String, String> records = consumer.poll(1000);
        for (ConsumerRecord<String, String> record : records)
            System.out.println(record.offset() + ": " + record.value());
    }

    @Test
    public void test4_delete_topic() throws ExecutionException, InterruptedException {
        AdminClient client = AdminClient.create(props);

        client.deleteTopics(Arrays.asList(TOPIC_NAME));
        client.close();

        assertThat("Topic deleted", !topicExists(TOPIC_NAME));
    }

    private  boolean topicExists(String topic) throws ExecutionException, InterruptedException {
        AdminClient client = AdminClient.create(props);

        return client.listTopics().names().get().contains(TOPIC_NAME);
    }
}

package com.babel.common.core.util;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * User: joey
 * Date: 2018/2/2
 * Time: 11:52
 */
public class KafkaProducerUtil {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerUtil.class);
    private static KafkaTemplate kafkaTemplate = null;

    private static ExecutorService THREAD_POOL = new ThreadPoolExecutor(7, 20, 10, TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(100), new ThreadPoolExecutor.CallerRunsPolicy());


    private static void initKafka() {
        if (SpringContextUtil.containsBean("kafkaTemplate")) {
            kafkaTemplate = (KafkaTemplate) SpringContextUtil.getBean("kafkaTemplate");
        }
    }

    public static KafkaTemplate<String, String> getKafkaTemplate() {
        if (kafkaTemplate == null) {
            initKafka();
        }
        if (kafkaTemplate == null) {
            logger.warn("--> no kafka template!");
        }
        return kafkaTemplate;
    }

    public static void send(String topic, String data) {
        THREAD_POOL.execute(() -> {
            try {
                getKafkaTemplate().send(topic, data);
            } catch (Exception e) {
                logger.error("kf send error, topic : {}, data :{}", topic, data);
            }
        });
    }

    public static void send(String topic, String key, String data) {
        THREAD_POOL.execute(() -> {
            try {
                getKafkaTemplate().send(topic, key, data);
            } catch (Exception e) {
                logger.error("kf send error, topic : {}, key : {}, data :{}", topic, key, data);
            }
        });
    }

    public static void send(String topic, int partition, String data) {
        THREAD_POOL.execute(() -> {
            try {
                getKafkaTemplate().send(topic, partition, data);
            } catch (Exception e) {
                logger.error("kf send error, topic : {}, partition : {} , data :{}", topic, partition, data);
            }
        });
    }

    public static void send(String topic, int partition, String key, String data) {
        THREAD_POOL.execute(() -> {
            try {
                getKafkaTemplate().send(topic, partition, key, data);
            } catch (Exception e) {
                logger.error("kf send error, topic : {}, partition :{}, key:{},  data :{}", topic, partition, key, data);
            }
        });
    }

    public static ProducerFactory<String, String> producerFactory(String broker_host, String request_timeout) {
        return new DefaultKafkaProducerFactory<>(producerConfigs(broker_host, request_timeout));
    }

    private static Map<String, Object> producerConfigs(String broker_host, String request_timeout) {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, broker_host);
        props.put(ProducerConfig.RETRIES_CONFIG, 0);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, 16384);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
//        props.put(ProducerConfig.ACKS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
        props.put(ProducerConfig.REQUEST_TIMEOUT_MS_CONFIG, request_timeout);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

}

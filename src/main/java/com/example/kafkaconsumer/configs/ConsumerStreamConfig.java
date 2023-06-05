package com.example.kafkaconsumer.configs;

import com.example.kafkaconsumer.models.Invoice;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.state.StoreBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.annotation.KafkaStreamsDefaultConfiguration;
import org.springframework.kafka.config.KafkaStreamsConfiguration;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@Slf4j
public class ConsumerStreamConfig {
//    @Bean(name = KafkaStreamsDefaultConfiguration.DEFAULT_STREAMS_CONFIG_BEAN_NAME)
//    public KafkaStreamsConfiguration kafkaStreamsConfiguration() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("spring.kafka.streams.application-id", "kafkaconsumer-streams-id");
//        map.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafkaconsumer-streams-id");
//        map.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        map.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Long.class.getName());
//        map.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Invoice.class.getName());
//        map.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 1_000_000);
//        map.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 1);
//
//        return new KafkaStreamsConfiguration(map);
//    }
//    public Properties properties() {
//        Map<String, Object> map = new HashMap<>();
//        map.put("spring.kafka.streams.application-id", "kafkaconsumer-streams-id");
//        map.put(StreamsConfig.APPLICATION_ID_CONFIG, "kafkaconsumer-streams-id");
//        map.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
//        map.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Long.class.getName());
//        map.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Invoice.class.getName());
//        map.put(StreamsConfig.CACHE_MAX_BYTES_BUFFERING_CONFIG, 1_000_000);
//        map.put(StreamsConfig.NUM_STREAM_THREADS_CONFIG, 1);
//
//        KafkaStreamsConfiguration kafkaStreamsConfiguration = new KafkaStreamsConfiguration(map);
//
//        return kafkaStreamsConfiguration.asProperties();
//    }
//
//    @PostConstruct
//    public void kafkaStreams() {
//        new StreamsBuilder().stream("invoice")
//                .peek((k, v) -> log.info("Key: {}, Value: {}"));
//
//    }
}

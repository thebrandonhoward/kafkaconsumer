package com.example.kafkaconsumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.StringSerializer;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.SessionWindows;
import org.apache.kafka.streams.kstream.Suppressed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@SpringBootApplication
@EnableKafkaStreams
@EnableScheduling
@Slf4j
public class KafkaconsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaconsumerApplication.class, args);
    }

}

@Configuration
class KafkaProducerConfig {

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                "localhost:9092");
        configProps.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        configProps.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }
}

@Component
@Slf4j
class Schedule {
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendMessage() {
        kafkaTemplate.send("invoice", "dummy", "close");
    }
    @Autowired
    @Scheduled(fixedDelay = 300_000)
    public void scheduleFixedDelayTask() {
        System.out.println(
                "Forcing session closure - " + System.currentTimeMillis() / 1000);
        sendMessage();
    }
}

@Component
@RequiredArgsConstructor
@Slf4j
class Processor {
    @Autowired
    public void process(final StreamsBuilder streamsBuilder) {
        streamsBuilder.stream("invoice", Consumed.with(Serdes.String(), Serdes.String()))
        //streamsBuilder.stream("invoice")
            .peek((k,v) -> log.info("[IN] Key: {}, Value: {}", k, v))
                .groupByKey()
                .windowedBy(SessionWindows.ofInactivityGapAndGrace(Duration.ofMinutes(5), Duration.ofSeconds(30)))
                .aggregate( () -> ""
                           ,(k,v,agg) -> agg + "::" + v
                           ,(k,v,agg) -> agg
                           ,Materialized.with(Serdes.String(), Serdes.String()) )
                .suppress(Suppressed.untilWindowCloses(Suppressed.BufferConfig.unbounded()))
                //.suppress(Suppressed.untilTimeLimit(Duration.ofMinutes(5), Suppressed.BufferConfig.unbounded()))
                .toStream()
                .peek((k,v) -> log.info("[OUT] Key: {}, Value: {}", k, v))
                .to("out-invoice");
    }
}

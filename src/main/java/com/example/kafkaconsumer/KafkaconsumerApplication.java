package com.example.kafkaconsumer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.SessionWindows;
import org.apache.kafka.streams.kstream.Suppressed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.EnableKafkaStreams;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Properties;

@SpringBootApplication
@EnableKafkaStreams
@Slf4j
public class KafkaconsumerApplication {

    public static void main(String[] args) {
        SpringApplication.run(KafkaconsumerApplication.class, args);
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

package dev.vality.dudoser.configuration;

import dev.vality.damsel.payment_processing.EventPayload;
import dev.vality.dudoser.serde.SinkEventDeserializer;
import dev.vality.kafka.common.util.ExponentialBackOffDefaultErrorHandlerFactory;
import dev.vality.machinegun.eventsink.MachineEvent;
import dev.vality.sink.common.parser.impl.MachineEventParser;
import dev.vality.sink.common.parser.impl.PaymentEventPayloadMachineEventParser;
import dev.vality.sink.common.serialization.BinaryDeserializer;
import dev.vality.sink.common.serialization.impl.PaymentEventPayloadDeserializer;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;

import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaConfig {

    @Value("${kafka.consumer.concurrency}")
    private int concurrency;

    private final KafkaProperties kafkaProperties;

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = kafkaProperties.buildConsumerProperties();
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, SinkEventDeserializer.class);
        return props;
    }

    @Bean
    public KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, MachineEvent>> kafkaListenerContainerFactory(
            ConsumerFactory<String, MachineEvent> consumerFactory
    ) {
        ConcurrentKafkaListenerContainerFactory<String, MachineEvent> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.setCommonErrorHandler(kafkaErrorHandler());
        factory.setConcurrency(concurrency);
        return factory;
    }

    private DefaultErrorHandler kafkaErrorHandler() {
        return ExponentialBackOffDefaultErrorHandlerFactory.create();
    }

    @Bean
    public BinaryDeserializer<EventPayload> paymentEventPayloadDeserializer() {
        return new PaymentEventPayloadDeserializer();
    }

    @Bean
    public MachineEventParser<EventPayload> paymentEventPayloadMachineEventParser(
            BinaryDeserializer<EventPayload> paymentEventPayloadDeserializer
    ) {
        return new PaymentEventPayloadMachineEventParser(paymentEventPayloadDeserializer);
    }
}

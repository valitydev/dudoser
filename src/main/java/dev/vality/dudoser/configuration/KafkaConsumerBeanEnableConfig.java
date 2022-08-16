package dev.vality.dudoser.configuration;

import dev.vality.damsel.payment_processing.EventPayload;
import dev.vality.dudoser.listener.InvoicingKafkaListener;
import dev.vality.dudoser.service.HandlerManager;
import dev.vality.sink.common.parser.impl.MachineEventParser;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;

@Configuration
@EnableKafka
public class KafkaConsumerBeanEnableConfig {

    @Bean
    @ConditionalOnProperty(value = "kafka.topics.invoice.enabled", havingValue = "true")
    public InvoicingKafkaListener paymentEventsKafkaListener(HandlerManager handlerManager,
                                                             MachineEventParser<EventPayload> parser) {
        return new InvoicingKafkaListener(handlerManager, parser);
    }
}
package dev.vality.dudoser.config;

import dev.vality.dudoser.DudoserApplication;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@ContextConfiguration(classes = DudoserApplication.class,
        initializers = AbstractKafkaTestContainerConfig.Initializer.class)
@Testcontainers
public abstract class AbstractKafkaTestContainerConfig extends AbstractPostgreTestContainerConfig {

    private static final String CONFLUENT_IMAGE_NAME = "confluentinc/cp-kafka";
    private static final String CONFLUENT_PLATFORM_VERSION = "6.1.2";

    @Container
    protected static final KafkaContainer KAFKA = new KafkaContainer(DockerImageName
            .parse(CONFLUENT_IMAGE_NAME)
            .withTag(CONFLUENT_PLATFORM_VERSION))
            .withEmbeddedZookeeper();

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            TestPropertyValues.of(
                    "kafka.bootstrap-servers=" + KAFKA.getBootstrapServers(),
                    "kafka.topics.invoice.enabled=true",
                    "kafka.consumer.auto-offset-reset=earliest",
                    "bm.pollingEnabled=false",
                    "dmt.polling.enable=false"
            ).applyTo(configurableApplicationContext);
        }
    }

}

package dev.vality.dudoser.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

@Configuration
@EnableScheduling
@ConditionalOnProperty(prefix = "message.schedule", name = "enabled", havingValue = "true", matchIfMissing = true)
public class ScheduleConfig {
}

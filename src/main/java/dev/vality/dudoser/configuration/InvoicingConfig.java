package dev.vality.dudoser.configuration;

import dev.vality.damsel.payment_processing.InvoicingSrv;
import dev.vality.dudoser.configuration.properties.InvoicingServiceProperties;
import dev.vality.woody.thrift.impl.http.THSpawnClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

@Configuration
public class InvoicingConfig {

    @Bean
    public InvoicingSrv.Iface invoicingClient(InvoicingServiceProperties invoicingServiceProperties)
            throws IOException {
        return new THSpawnClientBuilder()
                .withAddress(invoicingServiceProperties.getUrl().getURI())
                .withNetworkTimeout(invoicingServiceProperties.getNetworkTimeout())
                .build(InvoicingSrv.Iface.class);
    }
}

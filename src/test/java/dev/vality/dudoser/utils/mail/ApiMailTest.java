package dev.vality.dudoser.utils.mail;

import dev.vality.damsel.message_sender.*;
import dev.vality.dudoser.config.AbstractPostgreTestContainerConfig;
import dev.vality.woody.api.event.ClientEventListener;
import dev.vality.woody.api.event.CompositeClientEventListener;
import dev.vality.woody.api.generator.IdGenerator;
import dev.vality.woody.api.generator.TimestampIdGenerator;
import dev.vality.woody.thrift.impl.http.THClientBuilder;
import dev.vality.woody.thrift.impl.http.event.ClientEventLogListener;
import dev.vality.woody.thrift.impl.http.event.HttpClientEventLogListener;
import org.apache.thrift.TException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.TestPropertySource;
import org.subethamail.wiser.Wiser;
import org.subethamail.wiser.WiserMessage;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@TestPropertySource(locations = "classpath:test.properties")
public class ApiMailTest extends AbstractPostgreTestContainerConfig {

    @Value("${mail.port}")
    private int mailPort;
    @Value("${server.port}")
    private String serverPort;
    @Value("${mail.from}")
    private String from;
    @Value("${test.mail.to}")
    private String to;

    protected static <T> T createThriftRpcClient(Class<T> iface, IdGenerator idGenerator,
                                                 ClientEventListener eventListener, String url) {
        try {
            THClientBuilder clientBuilder = new THClientBuilder();
            clientBuilder.withAddress(new URI(url));
            clientBuilder.withIdGenerator(idGenerator);
            clientBuilder.withEventListener(eventListener);
            return clientBuilder.build(iface);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    Wiser wiser;

    @BeforeEach
    public void init() {
        wiser = new Wiser();
        wiser.setPort(mailPort);
        wiser.start();
    }

    @Test
    @Disabled
    public void testApi() throws TException, IOException, URISyntaxException, MessagingException {
        sendMail();
    }

    private void sendMail() throws TException, URISyntaxException, IOException, MessagingException {

        List<String> listTo = new ArrayList<String>();
        listTo.add(to);
        String mailBodyText = "Тело письма";
        MessageMail messageMail = new MessageMail(new MailBody(mailBodyText), from, listTo);
        messageMail.setSubject("Тема письма");
        Path filePath = Paths.get(getClass().getClassLoader().getResource("sadpepe.jpeg").toURI());
        byte[] messageBuf = Files.readAllBytes(
                filePath);
        List<MessageAttachment> attachments = new ArrayList<>();
        MessageAttachment e = new MessageAttachment();
        e.setName("sadpepe.png");
        e.setData(messageBuf);
        attachments.add(e);
        messageMail.setAttachments(attachments);
        Message m = new Message();
        m.setMessageMail(messageMail);
        ClientEventListener clientEventLogListener = new CompositeClientEventListener(
                new ClientEventLogListener(),
                new HttpClientEventLogListener()
        );
        MessageSenderSrv.Iface c =
                createThriftRpcClient(MessageSenderSrv.Iface.class, new TimestampIdGenerator(), clientEventLogListener,
                        "http://localhost:" + serverPort + "/dudos");
        c.send(m);

        for (WiserMessage message : wiser.getMessages()) {
            String envelopeSender = message.getEnvelopeSender();
            String envelopeReceiver = message.getEnvelopeReceiver();
            assertEquals(envelopeReceiver, to);
            assertEquals(envelopeSender, from);
        }
    }
}

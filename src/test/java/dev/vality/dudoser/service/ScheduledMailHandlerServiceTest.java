package dev.vality.dudoser.service;

import com.sun.mail.smtp.SMTPAddressFailedException;
import dev.vality.dudoser.dao.MessageDao;
import dev.vality.dudoser.dao.model.MessageToSend;
import dev.vality.dudoser.exception.MailNotSendException;
import dev.vality.dudoser.exception.MessageStoreException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.hamcrest.MockitoHamcrest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailSendException;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.support.TransactionTemplate;

import javax.mail.SendFailedException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@TestPropertySource(properties = {
        "message.store.days=1",
        "notification.payment.paid.from=test",
        "message.schedule.send=2000",
        "message.schedule.clear.sent=2000",
        "message.schedule.clear.failed=2000",
        "message.fail.minutes=5"
})
public class ScheduledMailHandlerServiceTest {

    @Autowired
    MessageDao messageDao;

    @Autowired
    MailSenderService mailSenderService;

    @Autowired
    ScheduledMailHandlerService service;

    @AfterEach
    public void cleanUp() {
        clearInvocations(messageDao, mailSenderService);
    }

    @Test
    public void storeSuccess() {
        when(messageDao.store(any(), any(), any())).thenReturn(true);
        service.storeMessage("test", "test", "test");

        verify(messageDao, times(1)).store(any(), any(), any());
    }

    @Test
    public void storeFailed() {
        when(messageDao.store(any(), any(), any())).thenReturn(false);
        assertThrows(MessageStoreException.class, () -> service.storeMessage("test", "test", "test"));

        verify(messageDao, times(1)).store(any(), any(), any());
    }

    @Test
    public void sendSuccess() {
        MessageToSend msg1 = new MessageToSend();
        msg1.setSubject("1");
        MessageToSend msg2 = new MessageToSend();
        msg2.setSubject("2");
        List<MessageToSend> value = List.of(msg1, msg2);
        when(messageDao.getUnsentMessages()).thenReturn(value);

        service.send();

        verify(messageDao, times(1))
                .markAsSent((List<MessageToSend>) MockitoHamcrest.argThat(containsInAnyOrder(msg1, msg2)));
    }

    @Test
    public void sendException() throws Exception {
        MessageToSend msg1 = new MessageToSend();
        msg1.setSubject("1");
        MessageToSend msg2 = new MessageToSend();
        msg2.setSubject("2");
        MessageToSend msg3 = new MessageToSend();
        msg3.setSubject("3");
        MessageToSend msg4 = new MessageToSend();
        msg4.setSubject("4");
        List<MessageToSend> value = List.of(msg1, msg2, msg3, msg4);
        when(messageDao.getUnsentMessages()).thenReturn(value);

        SendFailedException exception =
                new SendFailedException("kek", new SMTPAddressFailedException(null, null, 0, "err"));
        Mockito.doThrow(new MailNotSendException(
                        "test", new MailSendException(
                        "test", null, Map.of("no comments...",
                        exception))))
                .doThrow(RuntimeException.class)
                .doThrow(MailNotSendException.class)
                .doNothing()
                .when(mailSenderService).send(any(), any(), any(), any(), any());

        service.send();

        verify(messageDao, atLeastOnce()).getUnsentMessages();
        verify(messageDao, atLeastOnce())
                .markAsSent((List<MessageToSend>) MockitoHamcrest.argThat(containsInAnyOrder(msg1, msg4)));
    }

    @TestConfiguration
    public static class MailHandlerConfig {

        @Bean
        public MessageDao messageDao() {
            return Mockito.mock(MessageDao.class);
        }

        @Bean
        public MailSenderService mailSenderService() {
            return Mockito.mock(MailSenderService.class);
        }

        @Bean
        public ScheduledMailHandlerService service(MessageDao messageDao, MailSenderService mailSenderService,
                                                   TransactionTemplate transactionTemplate) {
            return new ScheduledMailHandlerService(messageDao, mailSenderService, Executors.newSingleThreadExecutor(),
                    transactionTemplate);
        }

        @Bean
        public TransactionTemplate transactionTemplate() {
            TransactionTemplate transactionTemplate = new TransactionTemplate(mock(PlatformTransactionManager.class));
            transactionTemplate.setIsolationLevel(TransactionDefinition.ISOLATION_READ_COMMITTED);
            transactionTemplate.setTimeout(10);
            return transactionTemplate;
        }

    }
}
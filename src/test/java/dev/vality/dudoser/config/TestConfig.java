package dev.vality.dudoser.config;

import org.springframework.boot.test.context.TestConfiguration;

@TestConfiguration
public class TestConfig {

/*    @Bean
    public MessageDao messageDao() {
        return mock(MessageDao.class);
    }

    @Bean
    public MailSenderService mailSenderService() {
        return mock(MailSenderService.class);
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
    }*/

}

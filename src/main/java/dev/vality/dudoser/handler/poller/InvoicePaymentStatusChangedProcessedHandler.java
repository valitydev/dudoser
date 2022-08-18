package dev.vality.dudoser.handler.poller;

import dev.vality.damsel.payment_processing.Invoice;
import dev.vality.damsel.payment_processing.InvoiceChange;
import dev.vality.dudoser.dao.EventTypeCode;
import dev.vality.dudoser.dao.TemplateDao;
import dev.vality.dudoser.dao.model.PaymentPayer;
import dev.vality.dudoser.handler.ChangeType;
import dev.vality.dudoser.service.*;
import dev.vality.dudoser.utils.Converter;
import dev.vality.dudoser.utils.mail.MailSubject;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class InvoicePaymentStatusChangedProcessedHandler extends InvoicePaymentStatusChangedHandler {

    private final InvoicingService invoicingService;
    private final PaymentPayerService paymentPayerService;

    public InvoicePaymentStatusChangedProcessedHandler(
            TemplateDao templateDao,
            TemplateService templateService,
            MailingExclusionRuleService mailingExclusionRuleService,
            ScheduledMailHandlerService mailHandlerService,
            InvoicingService invoicingService,
            PaymentPayerService paymentPayerService) {
        super(templateDao, templateService, mailingExclusionRuleService, mailHandlerService);
        this.invoicingService = invoicingService;
        this.paymentPayerService = paymentPayerService;
    }

    @Override
    public ChangeType getChangeType() {
        return ChangeType.INVOICE_PAYMENT_STATUS_CHANGED_PROCESSED;
    }

    @Override
    protected String getFormattedAmount(PaymentPayer payment) {
        return Converter.getFormattedAmount(payment.getAmount(), payment.getCurrency());
    }

    @Override
    protected Optional<PaymentPayer> getPaymentPayer(
            InvoiceChange invoiceChange,
            String invoiceId,
            Long sequenceId) {
        String paymentId = invoiceChange.getInvoicePaymentChange().getId();
        Invoice invoice = invoicingService.get(invoiceId, sequenceId);
        PaymentPayer paymentPayer = paymentPayerService.convert(invoice, invoiceId, paymentId);

        return Optional.ofNullable(paymentPayer);
    }

    @Override
    protected String getMailSubject() {
        return MailSubject.PAYMENT_PAID.pattern;
    }

    @Override
    protected EventTypeCode getEventTypeCode() {
        return EventTypeCode.PAYMENT_STATUS_CHANGED_PROCESSED;
    }
}

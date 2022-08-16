package dev.vality.dudoser.service.impl;

import dev.vality.damsel.payment_processing.EventRange;
import dev.vality.damsel.payment_processing.Invoice;
import dev.vality.damsel.payment_processing.InvoiceNotFound;
import dev.vality.damsel.payment_processing.InvoicingSrv;
import dev.vality.dudoser.exception.InvoicingClientException;
import dev.vality.dudoser.exception.NotFoundException;
import dev.vality.dudoser.service.InvoicingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class InvoicingServiceImpl implements InvoicingService {

    private final InvoicingSrv.Iface invoicingClient;

    @Override
    public Invoice get(String invoiceId, Long sequenceId) {
        try {
            log.info("Trying to get invoice, invoiceId='{}'", invoiceId);
            Invoice invoice = invoicingClient.get(invoiceId, getEventRange(sequenceId));
            log.info("Shop has been found, invoiceId='{}'", invoiceId);
            return invoice;
        } catch (InvoiceNotFound invoiceNotFound) {
            throw new NotFoundException(
                    String.format("Invoice not found invoiceId=%s, sequenceId=%s", invoiceId, sequenceId),
                    invoiceNotFound);
        } catch (TException e) {
            throw new InvoicingClientException(
                    String.format("Error receiving the invoice invoiceId=%s, sequenceId=%s", invoiceId, sequenceId), e);
        }
    }

    private EventRange getEventRange(Long sequenceId) {
        return new EventRange().setLimit(sequenceId.intValue());
    }

}

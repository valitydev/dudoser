package dev.vality.dudoser.service;

import dev.vality.damsel.payment_processing.Invoice;

public interface InvoicingService {

    Invoice get(String invoiceId, Long sequenceId);

}

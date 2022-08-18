package dev.vality.dudoser.service;

import dev.vality.damsel.payment_processing.Invoice;
import dev.vality.dudoser.dao.model.PaymentPayer;

public interface PaymentPayerService {

    PaymentPayer convert(Invoice invoice, String invoiceId, String paymentId);

    PaymentPayer convert(Invoice invoice, String invoiceId, String paymentId, String refundId);
}

package dev.vality.dudoser.handler.poller;

import dev.vality.damsel.payment_processing.InvoiceChange;
import dev.vality.dudoser.handler.Handler;

public interface PollingEventHandler extends Handler<InvoiceChange> {
}

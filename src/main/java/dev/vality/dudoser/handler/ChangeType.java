package dev.vality.dudoser.handler;

import dev.vality.geck.filter.Condition;
import dev.vality.geck.filter.Filter;
import dev.vality.geck.filter.PathConditionFilter;
import dev.vality.geck.filter.condition.IsNullCondition;
import dev.vality.geck.filter.rule.PathConditionRule;

/**
 * Created by inalarsanukaev on 10.07.17.
 */
public enum ChangeType {
    INVOICE_CREATED("invoice_created", new IsNullCondition().not()),
    INVOICE_PAYMENT_STATUS_CHANGED_PROCESSED(
            "invoice_payment_change.payload.invoice_payment_status_changed.status.processed",
            new IsNullCondition().not()),
    INVOICE_PAYMENT_STARTED("invoice_payment_change.payload.invoice_payment_started", new IsNullCondition().not()),
    INVOICE_PAYMENT_REFUND_CREATED(
            "invoice_payment_change.payload.invoice_payment_refund_change.payload.invoice_payment_refund_created",
            new IsNullCondition().not()),
    INVOICE_PAYMENT_REFUND_STATUS_CHANGED_SUCCEEDED(
            "invoice_payment_change.payload.invoice_payment_refund_change.payload.invoice_payment_refund_status_changed.status.succeeded",
            new IsNullCondition().not());

    Filter filter;

    ChangeType(String path, Condition... conditions) {
        this.filter = new PathConditionFilter(new PathConditionRule(path, conditions));
    }

    public Filter getFilter() {
        return filter;
    }
}

package dev.vality.dudoser.utils.mail;

public enum MailSubject {

    /**
     * Fields: InvoiceId, Date, Amount
     * Сформирован счет № Dud124 от 12-12-2016 на сумму 100.12 RUB
     * <p>
     * Example:
     * String.format(MailSubject.FORMED_THROUGH.pattern, invoiceId, date, amount);
     */
    FORMED_THROUGH("Сформирован счет № %s от %s на сумму %s"),

    /**
     * Fields: InvoiceId, Date, Amount
     * Счет № Dud124 от 12-12-2016 на сумму 100.12 RUB. Успешно оплачен
     * <p>
     * Example:
     * String.format(MailSubject.PAYMENT_PAID.pattern, invoiceId, date, amount);
     */
    PAYMENT_PAID("Счет № %s от %s на сумму %s. Успешно оплачен"),

    /**
     * Fields: InvoiceId, Date, Amount
     * Возврат средств на счет № Dud124 от 12-12-2016 на сумму 100.12 RUB.
     * <p>
     * Example:
     * String.format(MailSubject.REFUNDED.pattern, invoiceId, date, amount);
     */
    REFUNDED("Возврат средств на счет № %s от %s на сумму %s.");

    MailSubject(String pattern) {
        this.pattern = pattern;
    }

    public final String pattern;

    @Override
    public String toString() {
        return pattern;
    }

}

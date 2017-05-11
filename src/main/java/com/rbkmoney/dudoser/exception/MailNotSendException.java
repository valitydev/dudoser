package com.rbkmoney.dudoser.exception;

import org.springframework.mail.MailException;

/**
 * Created by inalarsanukaev on 10.05.17.
 */
public class MailNotSendException extends Exception {
    public MailNotSendException(String msg) {
        super(msg);
    }

    public MailNotSendException(String msg, Throwable cause) {
        super(msg, cause);
    }
}

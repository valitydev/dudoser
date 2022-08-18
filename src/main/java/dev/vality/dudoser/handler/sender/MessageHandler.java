package dev.vality.dudoser.handler.sender;

import dev.vality.damsel.message_sender.Message;

interface MessageHandler {
    boolean accept(Message value);

    void handle(Message message) throws Exception;
}

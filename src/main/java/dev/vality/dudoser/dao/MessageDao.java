package dev.vality.dudoser.dao;

import dev.vality.dudoser.dao.model.MessageToSend;

import java.time.Instant;
import java.util.List;

public interface MessageDao {

    boolean store(String receiver, String subject, String text);

    List<MessageToSend> getUnsentMessages();

    void deleteMessages(Instant before, Boolean sent);

    void markAsSent(List<MessageToSend> messages);
}

package dev.vality.dudoser.handler;

import dev.vality.damsel.base.InvalidRequest;
import dev.vality.damsel.message_sender.*;
import dev.vality.dudoser.converter.MailingExclusionRuleConverter;
import dev.vality.dudoser.dao.MailingExclusionRule;
import dev.vality.dudoser.dao.MailingExclusionRuleType;
import dev.vality.dudoser.handler.sender.MessageSendHandler;
import dev.vality.dudoser.service.MailingExclusionRuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.thrift.TException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by inal on 18.11.2016.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class DudoserHandler implements MessageSenderSrv.Iface {

    private final MessageSendHandler messageSendHandler;
    private final MailingExclusionRuleService mailingExclusionRuleService;
    private final MailingExclusionRuleConverter mailingExclusionRuleConverter;

    @Override
    public void send(Message message) throws TException {
        try {
            log.info("Start mail sending from {} to {}", message.getMessageMail().getFromEmail(),
                    message.getMessageMail().getToEmails());
            messageSendHandler.handleEvent(message);
            log.info("Mail sending completed.");
        } catch (InvalidRequest e) {
            log.warn("Invalid request parameters for mail {}", message.getMessageMail().getToEmails(), e);
            throw e;
        } catch (Exception e) {
            log.warn("Mail not send to {}", message.getMessageMail().getToEmails(), e);
            throw new TException(e);
        }
    }

    @Override
    public MessageExclusionObject addExclusionRule(MessageExclusion messageExclusion) throws TException {
        try {
            MailingExclusionRule exclusionRule = new MailingExclusionRule();
            exclusionRule.setName(messageExclusion.name);
            mailingExclusionRuleConverter.fromThrift(messageExclusion.rule, exclusionRule);
            MailingExclusionRule result = mailingExclusionRuleService.createExclusionRule(exclusionRule);
            return buildMessageExclusionObject(result);
        } catch (Exception e) {
            throw new TException(e);
        }
    }

    @Override
    public MessageExclusionObject getExclusionRule(MessageExclusionRef messageExclusionRef)
            throws TException {
        try {
            Optional<MailingExclusionRule> result =
                    mailingExclusionRuleService.getExclusionRule(messageExclusionRef.id);
            if (result.isEmpty()) {
                throw new ExclusionNotFound();
            }
            return buildMessageExclusionObject(result.get());
        } catch (Exception e) {
            throw new TException(e);
        }
    }

    private MessageExclusionObject buildMessageExclusionObject(MailingExclusionRule rule) {
        MessageExclusionObject messageExclusionObject = new MessageExclusionObject();
        messageExclusionObject.setRef(new MessageExclusionRef(rule.getId()));

        MessageExclusion messageExclusion = new MessageExclusion();
        messageExclusion.setName(rule.getName());
        MessageExclusionRule messageExclusionRule = new MessageExclusionRule();
        mailingExclusionRuleConverter.toThrift(rule, messageExclusionRule);
        messageExclusion.setRule(messageExclusionRule);

        messageExclusionObject.setExclusion(messageExclusion);

        return messageExclusionObject;
    }

    @Override
    public List<MessageExclusionObject> getExclusionRules(ExclusionType exclusionType) throws TException {
        try {
            return mailingExclusionRuleService.getExclusionRules(MailingExclusionRuleType.fromDamsel(exclusionType))
                    .stream()
                    .map(this::buildMessageExclusionObject)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new TException(e);
        }
    }

    @Override
    public void removeExclusionRule(MessageExclusionRef messageExclusionRef) throws TException {
        try {
            Optional<MailingExclusionRule> result =
                    mailingExclusionRuleService.getExclusionRule(messageExclusionRef.id);
            if (result.isEmpty()) {
                throw new ExclusionNotFound();
            }
            mailingExclusionRuleService.removeExclusionRule(messageExclusionRef.id);
        } catch (Exception e) {
            throw new TException(e);
        }
    }
}

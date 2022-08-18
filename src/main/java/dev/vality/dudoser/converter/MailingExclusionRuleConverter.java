package dev.vality.dudoser.converter;

import dev.vality.damsel.message_sender.MessageExclusionRule;
import dev.vality.damsel.message_sender.ShopExclusionRule;
import dev.vality.dudoser.dao.MailingExclusionRule;
import dev.vality.dudoser.dao.MailingExclusionRuleType;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class MailingExclusionRuleConverter {

    private static final String separator = ",";

    public void fromThrift(MessageExclusionRule from, MailingExclusionRule to) {
        if (from.isSetShopRule()) {
            to.setType(MailingExclusionRuleType.SHOP);
            String value = String.join(separator, from.getShopRule().shop_ids);
            to.setValue(value);
        } else {
            throw new IllegalArgumentException("Rule type " + from + " has not supported yet");
        }
    }

    public void toThrift(MailingExclusionRule from, MessageExclusionRule to) {
        if (MailingExclusionRuleType.SHOP.equals(from.getType())) {
            to.setShopRule(new ShopExclusionRule(Arrays.asList(from.getValue().split(separator))));
        } else {
            throw new IllegalArgumentException("Rule type " + from.getType() + " has not supported yet");
        }
    }
}

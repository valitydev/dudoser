package dev.vality.dudoser.converter;

import dev.vality.damsel.message_sender.MessageExclusionRule;
import dev.vality.damsel.message_sender.ShopExclusionRule;
import dev.vality.dudoser.dao.MailingExclusionRule;
import dev.vality.dudoser.dao.MailingExclusionRuleType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MailingExclusionRuleConverterTest {

    private final MailingExclusionRuleConverter converter = new MailingExclusionRuleConverter();

    private MessageExclusionRule testDamselExclusionRule;
    private MailingExclusionRule testDtoExclusionRule;

    @BeforeEach
    void setup() {
        testDamselExclusionRule = new MessageExclusionRule();
        testDtoExclusionRule = new MailingExclusionRule();
        testDamselExclusionRule.setShopRule(new ShopExclusionRule(List.of("1", "2", "3")));
        testDtoExclusionRule.setType(MailingExclusionRuleType.SHOP);
        testDtoExclusionRule.setValue("1,2,3");
    }


    @Test
    void shouldSuccessfullyConvertDamselToDto() {
        MailingExclusionRule actual = new MailingExclusionRule();
        converter.fromThrift(testDamselExclusionRule, actual);
        assertEquals(testDtoExclusionRule, actual);
    }

    @Test
    void shouldSuccessfullyConvertDtoToDamsel() {
        MessageExclusionRule actual = new MessageExclusionRule();
        converter.toThrift(testDtoExclusionRule, actual);
        assertEquals(testDamselExclusionRule, actual);
    }

    @Test
    void shouldFailBecauseOfUnsupportedDamselRuleType() {
        assertThrows(IllegalArgumentException.class, () -> {
            converter.fromThrift(new MessageExclusionRule(), new MailingExclusionRule());
        });
    }

    @Test
    void shouldFailBecauseOfUnsupportedDtoRuleType() {
        assertThrows(IllegalArgumentException.class, () -> {
            converter.toThrift(new MailingExclusionRule(), new MessageExclusionRule());
        });
    }
}

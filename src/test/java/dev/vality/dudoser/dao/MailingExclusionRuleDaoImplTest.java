package dev.vality.dudoser.dao;

import dev.vality.dudoser.config.AbstractPostgreTestContainerConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MailingExclusionRuleDaoImplTest extends AbstractPostgreTestContainerConfig {

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    MailingExclusionRuleDao dao;

    private MailingExclusionRule testDtoExclusionRule;

    @BeforeEach
    void setup() {
        jdbcTemplate.update("truncate table dudos.mailing_exclusion_rules;");

        testDtoExclusionRule = new MailingExclusionRule();
        testDtoExclusionRule.setName("test");
        testDtoExclusionRule.setType(MailingExclusionRuleType.SHOP);
        testDtoExclusionRule.setValue("1,2,3");
    }

    @Test
    void createExclusionRule() {
        Long id = dao.create(testDtoExclusionRule);

        Optional<MailingExclusionRule> rule = dao.get(id);
        assertTrue(rule.isPresent());
        MailingExclusionRule actual = rule.get();
        assertEquals(id, actual.getId());
        assertEquals(testDtoExclusionRule.getName(), actual.getName());
        assertEquals(testDtoExclusionRule.getType(), actual.getType());
        assertEquals(testDtoExclusionRule.getValue(), actual.getValue());
    }

    @Test
    void getExclusionRule() {
        Long id = dao.create(testDtoExclusionRule);

        Optional<MailingExclusionRule> rule = dao.get(id);
        assertTrue(rule.isPresent());
        MailingExclusionRule actual = rule.get();
        assertEquals(id, actual.getId());
        assertEquals(testDtoExclusionRule.getName(), actual.getName());
        assertEquals(testDtoExclusionRule.getType(), actual.getType());
        assertEquals(testDtoExclusionRule.getValue(), actual.getValue());
    }

    @Test
    void getExclusionRulesByType() {
        Long id = dao.create(testDtoExclusionRule);

        List<MailingExclusionRule> actual = dao.getByType(testDtoExclusionRule.getType());
        assertEquals(1, actual.size());
        assertEquals(id, actual.get(0).getId());
        assertEquals(testDtoExclusionRule.getName(), actual.get(0).getName());
        assertEquals(testDtoExclusionRule.getType(), actual.get(0).getType());
        assertEquals(testDtoExclusionRule.getValue(), actual.get(0).getValue());
    }

    @Test
    void getExclusionRulesByShopId() {
        Long id = dao.create(testDtoExclusionRule);

        List<MailingExclusionRule> actual = dao.getByShopId("1");
        assertEquals(1, actual.size());
        assertEquals(id, actual.get(0).getId());
        assertEquals(testDtoExclusionRule.getName(), actual.get(0).getName());
        assertEquals(testDtoExclusionRule.getType(), actual.get(0).getType());
        assertEquals(testDtoExclusionRule.getValue(), actual.get(0).getValue());
    }

    @Test
    void removeExclusionRule() {
        Long id = dao.create(testDtoExclusionRule);

        dao.remove(id);

        Optional<MailingExclusionRule> actual = dao.get(id);
        assertTrue(actual.isEmpty());
    }
}

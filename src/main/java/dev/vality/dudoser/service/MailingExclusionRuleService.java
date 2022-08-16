package dev.vality.dudoser.service;

import dev.vality.dudoser.dao.MailingExclusionRule;
import dev.vality.dudoser.dao.MailingExclusionRuleDao;
import dev.vality.dudoser.dao.MailingExclusionRuleType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class MailingExclusionRuleService {

    private final MailingExclusionRuleDao dao;

    public MailingExclusionRule createExclusionRule(MailingExclusionRule messageExclusionRule) {
        if (dao.getByType(messageExclusionRule.getType()).isEmpty()) {
            Long id = dao.create(messageExclusionRule);
            messageExclusionRule.setId(id);
            return messageExclusionRule;
        } else {
            throw new IllegalStateException(
                    "Exclusion rule with type " + messageExclusionRule.getType() + "already exists"
            );
        }
    }

    public Optional<MailingExclusionRule> getExclusionRule(Long id) {
        return dao.get(id);
    }

    public List<MailingExclusionRule> getExclusionRules(MailingExclusionRuleType type) {
        return dao.getByType(type);
    }

    public List<MailingExclusionRule> getExclusionRulesByShopId(String shopId) {
        return dao.getByShopId(shopId);
    }

    public void removeExclusionRule(Long id) {
        dao.remove(id);
    }
}

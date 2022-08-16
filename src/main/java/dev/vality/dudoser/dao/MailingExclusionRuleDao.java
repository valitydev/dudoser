package dev.vality.dudoser.dao;

import java.util.List;
import java.util.Optional;

public interface MailingExclusionRuleDao {

    Long create(MailingExclusionRule messageExclusionRule);

    Optional<MailingExclusionRule> get(Long id);

    List<MailingExclusionRule> getByType(MailingExclusionRuleType type);

    List<MailingExclusionRule> getByShopId(String shopId);

    void remove(Long id);
}

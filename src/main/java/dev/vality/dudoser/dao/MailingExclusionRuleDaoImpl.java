package dev.vality.dudoser.dao;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
public class MailingExclusionRuleDaoImpl extends NamedParameterJdbcDaoSupport implements MailingExclusionRuleDao {

    public MailingExclusionRuleDaoImpl(DataSource dataSource) {
        setDataSource(dataSource);
    }

    private final RowMapper<MailingExclusionRule> mapper = (rs, rowNum) -> {
        MailingExclusionRule result = new MailingExclusionRule();
        result.setId(rs.getLong("id"));
        result.setName(rs.getString("name"));
        result.setType(MailingExclusionRuleType.fromCode(rs.getString("type")));
        result.setValue(rs.getString("value"));
        return result;
    };

    @Override
    public Long create(MailingExclusionRule messageExclusionRule) {
        log.debug("Saving exclusion rule: {}", messageExclusionRule);
        final String sql = """
                    INSERT INTO dudos.mailing_exclusion_rules(name, type, value)
                    VALUES (:name, :type, :value)
                    RETURNING id;
                """;
        final MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("name", messageExclusionRule.getName())
                .addValue("type", messageExclusionRule.getType().getCode(), Types.OTHER)
                .addValue("value", messageExclusionRule.getValue());
        Long id;
        try {
            id = Objects.requireNonNull(getNamedParameterJdbcTemplate()).queryForObject(sql, params, Long.class);
        } catch (Exception e) {
            throw new DaoException("Error occurred during saving exclusion rule " + messageExclusionRule, e);
        }
        log.debug("Exclusion rule has been saved with id = {}", id);
        return id;
    }

    @Override
    public Optional<MailingExclusionRule> get(Long id) {
        log.debug("Getting exclusion rule by id = {}", id);
        final String sql = """
                    SELECT id, name, type, value
                    FROM dudos.mailing_exclusion_rules
                    WHERE id = :id;
                """;
        final MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        List<MailingExclusionRule> exclusionRules;
        try {
            exclusionRules = Objects.requireNonNull(getNamedParameterJdbcTemplate()).query(sql, params, mapper);
        } catch (Exception e) {
            throw new DaoException("Error occurred during getting exclusion rule id = " + id, e);
        }
        Optional<MailingExclusionRule> exclusionRule =  exclusionRules.isEmpty() ? Optional.empty() : Optional.of(exclusionRules.get(0));
        log.debug("Got exclusion rule {}", exclusionRule);
        return exclusionRule;
    }

    @Override
    public List<MailingExclusionRule> getByType(MailingExclusionRuleType type) {
        log.debug("Getting exclusion rules by type = {}", type);
        final String sql = """
                    SELECT id, name, type, value
                    FROM dudos.mailing_exclusion_rules
                    WHERE type = :type;
                """;
        final MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("type", type.getCode(), Types.OTHER, "exclusion_rule_type");
        List<MailingExclusionRule> exclusionRules;
        try {
            exclusionRules = Objects.requireNonNull(getNamedParameterJdbcTemplate()).query(sql, params, mapper);
        } catch (Exception e) {
            throw new DaoException("Error occurred during getting exclusion rules by type = " + type, e);
        }
        log.debug("Got exclusion rules {}", exclusionRules);
        return exclusionRules;
    }

    @Override
    public List<MailingExclusionRule> getByShopId(String shopId) {
        log.debug("Getting exclusion rules by shop id = {}", shopId);
        final String sql = """
                    SELECT id, name, type, value
                    FROM dudos.mailing_exclusion_rules
                    WHERE type = 'shop'
                    AND value LIKE :value;
                """;
        final MapSqlParameterSource params = new MapSqlParameterSource("value", "%" + shopId + "%");
        List<MailingExclusionRule> exclusionRules;
        try {
            exclusionRules = Objects.requireNonNull(getNamedParameterJdbcTemplate()).query(sql, params, mapper);
        } catch (Exception e) {
            throw new DaoException("Error occurred during getting exclusion rules by shop id = " + shopId, e);
        }
        log.debug("Got exclusion rules {}", exclusionRules);
        return exclusionRules;
    }

    @Override
    public void remove(Long id) {
        log.debug("Deleting exclusion rules by id = {}", id);
        final String sql = "DELETE FROM dudos.mailing_exclusion_rules WHERE id = :id;";
        final MapSqlParameterSource params = new MapSqlParameterSource("id", id);
        try {
            Objects.requireNonNull(getNamedParameterJdbcTemplate()).update(sql, params);
        } catch (Exception e) {
            throw new DaoException("Error occurred during deleting exclusion rules by id = " + id, e);
        }
        log.debug("Exclusion rules by id = {} has been deleted", id);
    }
}

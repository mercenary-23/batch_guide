package com.apress.batch.chapter10.config.step.processor;

import com.apress.batch.chapter10.domain.statement.Account;
import com.apress.batch.chapter10.domain.statement.Statement;
import com.apress.batch.chapter10.domain.transaction.Transaction;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@RequiredArgsConstructor
public class AccountItemProcessor implements ItemProcessor<Statement, Statement> {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Statement process(Statement statement) throws Exception {
        final String sql = "SELECT a.account_id, a.balance, a.last_statement_date, "
            + "t.transaction_id, t.description, t.credit, t.debit, t.timestamp "
            + "FROM account AS a LEFT JOIN transaction AS t "
            + "  ON a.account_id = t.account_account_id "
            + "WHERE a.account_id IN "
            + "  (SELECT account_account_id "
            + "   FROM customer_account "
            + "   WHERE customer_customer_id = ?) "
            + "ORDER BY t.timestamp";

        statement.setAccounts(jdbcTemplate.query(sql, new AccountResultSetExtractor(),
            statement.getCustomer().getId()));
        return statement;
    }

    private static class AccountResultSetExtractor implements ResultSetExtractor<List<Account>> {

        private final List<Account> accounts = new ArrayList<>();

        @Nullable
        @Override
        public List<Account> extractData(ResultSet rs) throws SQLException, DataAccessException {
            Account curAccount = null;
            while (rs.next()) {
                if (curAccount == null) {
                    curAccount = new Account(rs.getLong("account_id"), rs.getBigDecimal("balance"),
                        rs.getTimestamp("last_statement_date").toLocalDateTime());
                } else if (rs.getLong("account_id") != curAccount.getId()) {
                    accounts.add(curAccount);

                    curAccount = new Account(rs.getLong("account_id"), rs.getBigDecimal("balance"),
                        rs.getTimestamp("last_statement_date").toLocalDateTime());
                }

                if (StringUtils.hasText(rs.getString("description"))) {
                    curAccount.addTransaction(Transaction
                        .builder()
                            .transactionId(rs.getLong("transaction_id"))
                            .accountId(rs.getLong("account_id"))
                            .description(rs.getString("description"))
                            .credit(rs.getBigDecimal("credit"))
                            .debit(rs.getBigDecimal("debit"))
                            .timestamp(rs.getTimestamp("timestamp").toLocalDateTime())
                        .build());
                }
            }

            if (curAccount != null) {
                accounts.add(curAccount);
            }

            return accounts;
        }
    }

}

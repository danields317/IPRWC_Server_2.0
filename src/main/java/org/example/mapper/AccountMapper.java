package org.example.mapper;

import org.example.model.Account;
import org.jdbi.v3.core.mapper.RowMapper;
import org.jdbi.v3.core.statement.StatementContext;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountMapper implements RowMapper<Account> {
    @Override
    public Account map(ResultSet rs, StatementContext ctx) throws SQLException {
        return new Account(rs.getInt("account_id"), rs.getString("hash"), rs.getString("account_role"),
                rs.getString("first_name"), rs.getString("last_name"), rs.getString("email_address"),
                rs.getString("city"), rs.getString("street"), rs.getString("house_number")
        );
    }
}

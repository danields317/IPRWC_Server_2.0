package org.example.db;

import org.example.mapper.AccountMapper;
import org.example.model.Account;
import org.jdbi.v3.sqlobject.GenerateSqlObject;
import org.jdbi.v3.sqlobject.config.RegisterRowMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.GetGeneratedKeys;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;
import org.postgresql.util.PSQLException;

import java.util.List;
import java.util.Optional;

@RegisterRowMapper(AccountMapper.class)
public interface AccountDAO {

    @SqlQuery("SELECT * FROM account LIMIT :pagesize OFFSET :offset")
    List<Account> getAccountList(@Bind("pagesize") int pageSize, @Bind("offset") int offset);

    @SqlQuery("SELECT (COUNT(*)::DECIMAL / :pagesize) FROM account")
    double getMaxPages(@Bind("pagesize") int pageSize);

    @SqlQuery("SELECT * FROM account WHERE account_id = :account_id")
    Account readAccountById(@Bind("account_id") int id);

    @SqlQuery("SELECT * FROM account WHERE email_address = :email_address")
    Account readAccountByEmail(@Bind("email_address") String email);

    @SqlQuery("SELECT account_role FROM account WHERE account_id = :account_id")
    String readAccountRoleById(@Bind("account_id") int id);

    @SqlUpdate("INSERT INTO account(hash, account_role, first_name, last_name, email_address, city, street, house_number) " +
            "VALUES (?, ?, ?, ?, ?, ?, ?, ?)")
    Boolean createAccount(String passwordHash, String accountRole, String firstName, String lastName, String email, String city, String street, String number) throws PSQLException;

    @SqlUpdate("UPDATE account SET first_name = :first_name, last_name = :last_name, email_address = :email_address, city = :city," +
            " street = :street, house_number = :number WHERE account_id = :account_id")
    Boolean updateAccount(@Bind("account_id") int id, @Bind("first_name") String firstName, @Bind("last_name") String lastName, @Bind("email_address") String email,
                          @Bind("city") String city, @Bind("street") String street, @Bind("number") String number);

    @SqlUpdate("DELETE FROM account WHERE account_id = :account_id")
    Boolean deleteAccount(@Bind("account_id") int id);

}

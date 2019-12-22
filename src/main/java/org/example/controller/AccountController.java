package org.example.controller;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.example.core.JWTManager;
import org.example.core.PasswordManager;
import org.example.db.AccountDAO;
import org.example.model.Account;
import org.example.model.AccountList;
import org.jdbi.v3.core.Jdbi;

import java.sql.SQLException;
import java.util.List;
import java.util.NoSuchElementException;

public class AccountController {

    private final AccountDAO accountDAO;

    public AccountController(Jdbi jdbi){
        accountDAO = jdbi.onDemand(AccountDAO.class);
    }

    public Account getPersonalAccount(String token) throws JWTDecodeException{
        int personalId = getId(token);
        return accountDAO.readAccountById(personalId);
    }

    public void updatePersonalAccount(String token, Account account) throws JWTDecodeException, SQLException{
        int personalId = getId(token);
        try {
            accountDAO.updateAccount(personalId, account.getFirstName(), account.getLastName(), account.getEmailAddress(), account.getCity(), account.getStreet(),
                    account.getHouseNumber());
        }catch (Exception e){
            throw new SQLException();
        }
    }

    public void deletePersonalAccount(String token) throws JWTDecodeException, SQLException{
        int personalId = getId(token);
        try {
            accountDAO.deleteAccount(personalId);
        }catch (Exception e){
            throw new SQLException();
        }
    }

    public void createAccount(Account account) throws SQLException {
        PasswordManager passwordManager = new PasswordManager();
        account.setHash(passwordManager.hashPassword(account.getHash()));
        try {
            accountDAO.createAccount(account.getHash(), account.getAccountRole(), account.getFirstName(), account.getLastName(), account.getEmailAddress(),
                    account.getCity(), account.getStreet(), account.getHouseNumber()
            );
        }catch (Exception e){
            throw new SQLException();
        }
    }

    public Account getAccountWithId(int id) throws SQLException {
        Account account = accountDAO.readAccountById(id);
        if (account != null){
            return account;
        }else {
            throw new SQLException();
        }
    }

    public void updateAccountWithId(int id, Account account) throws SQLException, NoSuchElementException {
        try {
            Boolean succes = accountDAO.updateAccount(id, account.getFirstName(), account.getLastName(), account.getEmailAddress(),
                    account.getCity(), account.getStreet(), account.getHouseNumber());
            if (!succes){
                throw new NoSuchElementException();
            }
        } catch (Exception e){
            throw e;
        }
    }

    public void deleteAccountWithId(int id){
        try {
            Boolean succes = accountDAO.deleteAccount(id);
            if (!succes){
                throw new NoSuchElementException();
            }
        } catch (Exception e){
            throw e;
        }
    }

    public AccountList getAccounts(int pageSize, int page) throws SQLException {
        int offset = ((page -1) * pageSize);
        AccountList accountList = new AccountList();
        try {
            accountList.setAccounts(accountDAO.getAccountList(pageSize, offset));
        } catch (Exception e){
            throw new SQLException();
        }
        try {
            accountList.setTotalPages(accountDAO.getMaxPages(pageSize));
        } catch (Exception e){
            throw new SQLException();
        }
        return accountList;
    }

    private int getId(String token) throws JWTDecodeException{
        token = stripToken(token);
        JWTManager jwtManager = JWTManager.getJwtManager();
        DecodedJWT jwt = jwtManager.decodeJwt(token);
        return jwt.getClaim("accountId").asInt();
    }

    private String stripToken(String token){
        return token.split("Bearer ")[1];
    }
}

package org.example.controller;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.example.core.JWTManager;
import org.example.core.PasswordManager;
import org.example.db.AccountDAO;
import org.example.model.Account;
import org.example.model.AccountList;
import org.hibernate.validator.internal.constraintvalidators.bv.EmailValidator;
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
        Account account = accountDAO.readAccountById(personalId);
        account.setHash(null);
        return account;
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

    public void createCustomerAccount(Account account) throws Exception {
        try {
            account.setAccountRole("Customer");
            createAccount(account);
        } catch (Exception e) {
            throw e;
        }

    }

    public void createAccount(Account account) throws Exception {
        if (checkEmpty(account)) {
            throw new NullPointerException();
        }
        PasswordManager passwordManager = new PasswordManager();
        account.setHash(passwordManager.hashPassword(account.getHash()));
        try {
            accountDAO.createAccount(account.getHash(), account.getAccountRole(), account.getFirstName(), account.getLastName(), account.getEmailAddress(),
                    account.getCity(), account.getStreet(), account.getHouseNumber()
            );
        }catch (Exception e){
            throw e;
        }
    }

    public Account getAccountWithId(int id) throws SQLException {
        Account account = accountDAO.readAccountById(id);
        if (account != null){
            account.setHash(null);
            return account;
        }else {
            throw new SQLException();
        }
    }

    public void updateAccountWithId(int id, Account account) {
        if (checkEmptyUpdate(account)) {
            throw new NullPointerException();
        }
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

    private boolean checkEmpty(Account account) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        if (account.getFirstName().replaceAll(" ","").isEmpty() ||
                account.getLastName().replaceAll(" ","").isEmpty() ||
                !account.getEmailAddress().matches(regex) ||
                account.getCity().replaceAll(" ","").isEmpty() ||
                account.getStreet().replaceAll(" ","").isEmpty() ||
                account.getHouseNumber().replaceAll(" ","").isEmpty() ||
                account.getHash().replaceAll(" ","").length() < 8 ||
                account.getAccountRole().replaceAll(" ","").isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    private boolean checkEmptyUpdate(Account account) {
        String regex = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
        if (account.getFirstName().replaceAll(" ","").isEmpty() ||
                account.getLastName().replaceAll(" ","").isEmpty() ||
                !account.getEmailAddress().matches(regex) ||
                account.getCity().replaceAll(" ","").isEmpty() ||
                account.getStreet().replaceAll(" ","").isEmpty() ||
                account.getHouseNumber().replaceAll(" ","").isEmpty()) {
            return true;
        } else {
            return false;
        }
    }
}

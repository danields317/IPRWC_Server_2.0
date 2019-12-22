package org.example.controller;

import org.example.core.JWTManager;
import org.example.core.PasswordManager;
import org.example.db.AccountDAO;
import org.example.model.Account;
import org.example.model.Authentication;
import org.jdbi.v3.core.Jdbi;

import javax.naming.AuthenticationException;

public class AuthenticationController {

    private AccountDAO accountDAO;
    private PasswordManager passwordManager;
    private JWTManager jwtManager;

    public AuthenticationController(Jdbi jdbi){
        this.accountDAO = jdbi.onDemand(AccountDAO.class);
        this.passwordManager = new PasswordManager();
        this.jwtManager = JWTManager.getJwtManager();
    }

    public String authenticate(Authentication authentication) throws AuthenticationException {
        Account dbAccount = accountDAO.readAccountByEmail(authentication.getEmailAddress());
        if (dbAccount == null){
            throw new AuthenticationException();
        }
        else if (passwordManager.validatePassword(authentication.getPassword(), dbAccount.getHash())){
            return jwtManager.createJwtToken(dbAccount.getAccountId());
        }
        else {
            throw new AuthenticationException();
        }
    }

}

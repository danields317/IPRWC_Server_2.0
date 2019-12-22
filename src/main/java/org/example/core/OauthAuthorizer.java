package org.example.core;

import io.dropwizard.auth.Authorizer;
import org.example.db.AccountDAO;
import org.jdbi.v3.core.Jdbi;

public class OauthAuthorizer implements Authorizer<JWTTokenPrincipal> {

    AccountDAO accountDAO;

    public OauthAuthorizer(Jdbi jdbi){
        this.accountDAO = jdbi.onDemand(AccountDAO.class);
    }

    @Override
    public boolean authorize(JWTTokenPrincipal jwtTokenPrincipal, String s) {
        String role = accountDAO.readAccountRoleById(Integer.parseInt((jwtTokenPrincipal.getName())));
        return role.equals(s);
    }
}

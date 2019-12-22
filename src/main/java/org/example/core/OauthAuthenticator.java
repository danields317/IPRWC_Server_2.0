package org.example.core;

import com.auth0.jwt.interfaces.DecodedJWT;
import io.dropwizard.auth.Authenticator;
import org.example.db.AccountDAO;
import org.jdbi.v3.core.Jdbi;

import javax.ws.rs.WebApplicationException;
import java.util.Optional;

public class OauthAuthenticator implements Authenticator<String, JWTTokenPrincipal> {

    private final JWTManager jwtManager;
    private final AccountDAO accountDAO;

    public OauthAuthenticator(Jdbi jdbi){
        this.jwtManager = JWTManager.getJwtManager();
        this.accountDAO = jdbi.onDemand(AccountDAO.class);
    }

    @Override
    public Optional<JWTTokenPrincipal> authenticate(String token) throws WebApplicationException {
        try{
            DecodedJWT jwt = jwtManager.verifyJwtToken(token);
            if (accountDAO.readAccountById(jwt.getClaim("accountId").asInt()) == null){
                throw new WebApplicationException(org.eclipse.jetty.server.Response.SC_UNAUTHORIZED);
            }
            return Optional.of(new JWTTokenPrincipal(jwt));
        }catch (Exception e){
            throw new WebApplicationException(org.eclipse.jetty.server.Response.SC_UNAUTHORIZED);
        }
    }
}

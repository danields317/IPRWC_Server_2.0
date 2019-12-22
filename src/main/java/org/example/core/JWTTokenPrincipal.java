package org.example.core;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.security.Principal;

public class JWTTokenPrincipal implements Principal {

    private final DecodedJWT token;

    public JWTTokenPrincipal(DecodedJWT jwtToken){
        this.token = jwtToken;
    }

    @Override
    public String getName() {
        Claim claim = token.getClaim("accountId");
        return String.valueOf(claim.asInt());
    }

}

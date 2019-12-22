package org.example.core;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.joda.time.DateTime;
import java.util.Date;

public class JWTManager {

    private static JWTManager jwtManager;
    private final String secret;
    private final String issuerName;

    public static synchronized JWTManager getInstance(String secret, String issuerName){
        if (jwtManager == null){
            jwtManager = new JWTManager(secret, issuerName);
        }
        return jwtManager;
    }

    public static synchronized JWTManager getJwtManager(){
        return jwtManager;
    }

    private JWTManager(String secret, String issuerName){
        this.secret = secret;
        this.issuerName = issuerName;
    }

    public String createJwtToken(int accountId){
        String token = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret);
             token = JWT.create()
                    .withIssuer(issuerName)
                    .withNotBefore(new Date())
                    .withIssuedAt(new Date())
                    .withExpiresAt(new DateTime().plusDays(1).toDate())
                    .withClaim("accountId", accountId)
                    .sign(algorithm);
        }catch (JWTCreationException e){
            e.printStackTrace();
        }
        return token;
    }

    public DecodedJWT verifyJwtToken(String token) throws JWTVerificationException{
        try {
            Algorithm algorithm = Algorithm.HMAC256(this.secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withIssuer(this.issuerName)
                    .build();
            DecodedJWT jwt = verifier.verify(token);
            return jwt;
        }catch (JWTVerificationException e){
            throw new JWTVerificationException("Invalid token");
        }
    }

    public DecodedJWT decodeJwt(String token) throws JWTDecodeException {
        return JWT.decode(token);
    }

}

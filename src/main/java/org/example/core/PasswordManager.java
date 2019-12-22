package org.example.core;
import org.mindrot.jbcrypt.BCrypt;

public class PasswordManager {

    private final int rounds = 14;

    public String hashPassword(String password){
        String salt = BCrypt.gensalt(rounds);
        String hash = BCrypt.hashpw(password, salt);
        return hash;
    }

    public boolean validatePassword(String inputPassword, String hash){
        return BCrypt.checkpw(inputPassword, hash);
    }

}

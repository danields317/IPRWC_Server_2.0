package org.example.model;

public class Authentication {

    private String emailAddress;

    private String password;

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getPassword() {
        return password;
    }

    public Authentication() {

    }

    public Authentication(String emailAddress, String password) {
        this.emailAddress = emailAddress;
        this.password = password;
    }
}

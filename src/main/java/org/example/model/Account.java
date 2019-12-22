package org.example.model;

public class Account {

    private int accountId;
    private String hash;
    private String accountRole;
    private String firstName;
    private String lastName;
    private String emailAddress;
    private String city;
    private String street;
    private String houseNumber;

    public Account(){

    }

    public Account(int accountId, String hash, String accountRole, String firstName, String lastName, String emailAddress, String city, String street, String houseNumber) {
        this.accountId = accountId;
        this.hash = hash;
        this.accountRole = accountRole;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.city = city;
        this.street = street;
        this.houseNumber = houseNumber;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getAccountRole() {
        return accountRole;
    }

    public void setAccountRole(String accountRole) {
        this.accountRole = accountRole;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public int getId() {
        return accountId;
    }

    public void setId(int id) {
        this.accountId = id;
    }
}

package org.example.model;

import java.util.List;

public class AccountList {

    private List<Account> accounts;
    private int totalPages;

    public AccountList(){

    }

    public List<Account> getAccounts() {
        return accounts;
    }

    public void setAccounts(List<Account> accounts) {
        this.accounts = accounts;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }
}

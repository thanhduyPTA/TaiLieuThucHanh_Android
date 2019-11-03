package com.e.myapplication;

import java.util.Objects;

public class Account {
    private String accountid;
    private String credential;
    private String role;

    public String getAccountid() {
        return accountid;
    }

    public void setAccountid(String accountid) {
        this.accountid = accountid;
    }

    public String getCredential() {
        return credential;
    }

    public void setCredential(String credential) {
        this.credential = credential;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return Objects.equals(accountid, account.accountid);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountid);
    }

    public Account(String accountid, String credential, String role) {
        this.accountid = accountid;
        this.credential = credential;
        this.role = role;
    }

    public Account(String accountid) {
        this.accountid = accountid;
    }

    @Override
    public String toString() {
        return accountid + " - " + credential + " - " + role;
    }
}

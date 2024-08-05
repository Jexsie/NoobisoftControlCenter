package com.noobisoftcontrolcenter.tokemon;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    private String email;
    private String accountId;

    // Getters and Setters

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }
}

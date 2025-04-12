package com.trabalho.devweb.domain;

public class Account {
    private String name;
    private String email;

    public Account(String name, String email) {
        this.name = name;
        this.email = email;
    }
    
    // Getters e Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "Account{" +
               ", name='" + name + '\'' +
               ", email='" + email + '\'' +
               '}';
    }
}

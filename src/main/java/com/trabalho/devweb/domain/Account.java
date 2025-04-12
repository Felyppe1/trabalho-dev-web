package com.trabalho.devweb.domain;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Account {
    private String cpf;
    private String name;
    private String email;
    private String password;
    private LocalDate birthDate;
    private String address;
    private String cellphoneNumber;
    private BigDecimal balance = BigDecimal.ZERO;
    private String status = "active";

    public Account() {}

    public Account(String cpf, String name, String email, String password,
                   LocalDate birthDate, String address, String cellphoneNumber) {
        this.cpf = cpf;
        this.name = name;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.address = address;
        this.cellphoneNumber = cellphoneNumber;
    }

    // Getters e Setters
    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCellphoneNumber() {
        return cellphoneNumber;
    }

    public void setCellphoneNumber(String cellphoneNumber) {
        this.cellphoneNumber = cellphoneNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

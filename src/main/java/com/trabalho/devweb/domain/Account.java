package com.trabalho.devweb.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class Account {
    private String id;
    private String number;
    private String digit;
    private String cpf;
    private String name;
    private String email;
    private String password;
    private LocalDate birthDate;
    private String address;
    private String cellphoneNumber;
    private BigDecimal balance = BigDecimal.ZERO;
    private String status = "active";
    private LocalDateTime createdAt;

    public Account() {}

    public Account(String cpf, String name, String email, String password,
                   LocalDate birthDate, String address, String cellphoneNumber) {
        this.id = UUID.randomUUID().toString();
        this.number = generateAccountNumber();
        this.digit = generateAccountDigit();
        this.cpf = cpf;
        this.name = name;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.address = address;
        this.cellphoneNumber = cellphoneNumber;
        this.createdAt = LocalDateTime.now();
    }

    // Gera um número de conta de 10 dígitos
    private String generateAccountNumber() {
        return String.format("%010d", (int)(Math.random() * 10000000000L));
    }

    // Gera um dígito verificador de 2 dígitos
    private String generateAccountDigit() {
        return String.format("%02d", (int)(Math.random() * 100));
    }

    // Getters e Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDigit() {
        return digit;
    }

    public void setDigit(String digit) {
        this.digit = digit;
    }
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}

package com.trabalho.devweb.domain;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import java.text.NumberFormat;
import java.util.Locale;

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

    public Account(String id, String number, String digit, String cpf, String name, 
                   String email, String password, LocalDate birthDate, String address, 
                   String cellphoneNumber, BigDecimal balance, String status, 
                   LocalDateTime createdAt) {
        this.id = id;
        this.number = number;
        this.digit = digit;
        this.cpf = cpf;
        this.name = name;
        this.email = email;
        this.password = password;
        this.birthDate = birthDate;
        this.address = address;
        this.cellphoneNumber = cellphoneNumber;
        this.balance = balance;
        this.status = status;
        this.createdAt = createdAt;
    }

    public static Account create(String cpf, String name, String email, String password,
                             LocalDate birthDate, String address, String cellphoneNumber) {
    String id = UUID.randomUUID().toString();
    String number = generateAccountNumber();
    String digit = generateAccountDigit();
    BigDecimal balance = BigDecimal.ZERO;
    String status = "active";
    LocalDateTime createdAt = LocalDateTime.now();

    return new Account(id, number, digit, cpf, name, email, password,
                       birthDate, address, cellphoneNumber, balance, status, createdAt);
    }

    private static String generateAccountNumber() {
        return String.format("%010d", (long)(Math.random() * 10000000000L));
    }

    private static String generateAccountDigit() {
        return String.valueOf((int)(Math.random() * 10));
    }

    public String getAccountNumber() {
        return number + "-" + digit;
    }
    
    public String getId() {
        return id;
    }

    public String getNumber() {
        return number;
    }

    public String getDigit() {
        return digit;
    }

    public String getCpf() {
        return cpf;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public String getAddress() {
        return address;
    }

    public String getCellphoneNumber() {
        return cellphoneNumber;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getFormattedBalance() {
        NumberFormat nf = NumberFormat.getCurrencyInstance(Locale.of("pt", "BR"));
        return nf.format(balance);
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}

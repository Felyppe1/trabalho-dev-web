/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.devbank;

/**
 *
 * @author mayar_a8ahxqn
 */


public class DestinatarioFrequente {
    private String nome;
    private String banco;
    private String conta;

    public DestinatarioFrequente(String nome, String banco, String conta) {
        this.nome = nome;
        this.banco = banco;
        this.conta = conta;
    }

    public String getNome() {
        return nome;
    }

    public String getBanco() {
        return banco;
    }

    // Getter simples para conta, caso queira no JSP
    public String getConta() {
        return conta;
    }

    public String getContaMascarada() {
        if (conta == null || conta.length() < 4) return "****";
        return "****" + conta.substring(conta.length() - 4);
    }

    public String getBancoFormatado() {
        return banco + " - " + getContaMascarada();
    }
}

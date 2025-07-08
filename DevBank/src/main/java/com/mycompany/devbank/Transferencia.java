package com.mycompany.devbank;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class Transferencia {
    private int id;
    private String remetenteNome;
    private String remetenteBanco;
    private String remetenteConta;
    private String destinatarioNome;
    private String destinatarioBanco;
    private String destinatarioConta;
    private BigDecimal valor;
    private String status;
    private Timestamp dataTransferencia;

    public Transferencia() {}

    public Transferencia(int id, String remetenteNome, String remetenteBanco, String remetenteConta,
                         String destinatarioNome, String destinatarioBanco, String destinatarioConta,
                         BigDecimal valor, String status, Timestamp dataTransferencia) {
        this.id = id;
        this.remetenteNome = remetenteNome;
        this.remetenteBanco = remetenteBanco;
        this.remetenteConta = remetenteConta;
        this.destinatarioNome = destinatarioNome;
        this.destinatarioBanco = destinatarioBanco;
        this.destinatarioConta = destinatarioConta;
        this.valor = valor;
        this.status = status;
        this.dataTransferencia = dataTransferencia;
    }

    // Getters e Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getRemetenteNome() { return remetenteNome; }
    public void setRemetenteNome(String remetenteNome) { this.remetenteNome = remetenteNome; }

    public String getRemetenteBanco() { return remetenteBanco; }
    public void setRemetenteBanco(String remetenteBanco) { this.remetenteBanco = remetenteBanco; }

    public String getRemetenteConta() { return remetenteConta; }
    public void setRemetenteConta(String remetenteConta) { this.remetenteConta = remetenteConta; }

    public String getDestinatarioNome() { return destinatarioNome; }
    public void setDestinatarioNome(String destinatarioNome) { this.destinatarioNome = destinatarioNome; }

    public String getDestinatarioBanco() { return destinatarioBanco; }
    public void setDestinatarioBanco(String destinatarioBanco) { this.destinatarioBanco = destinatarioBanco; }

    public String getDestinatarioConta() { return destinatarioConta; }
    public void setDestinatarioConta(String destinatarioConta) { this.destinatarioConta = destinatarioConta; }

    public BigDecimal getValor() { return valor; }
    public void setValor(BigDecimal valor) { this.valor = valor; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public Timestamp getDataTransferencia() { return dataTransferencia; }
    public void setDataTransferencia(Timestamp dataTransferencia) { this.dataTransferencia = dataTransferencia; }
}

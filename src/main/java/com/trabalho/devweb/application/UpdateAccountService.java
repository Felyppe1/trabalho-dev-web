package com.trabalho.devweb.application;

import com.trabalho.devweb.application.interfaces.IAccountsRepository;
import com.trabalho.devweb.domain.Account;
import java.sql.SQLException;

public class UpdateAccountService {
    private IAccountsRepository accountsRepository;

    public UpdateAccountService(IAccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    public UpdateAccountResult execute(String accountId, String name, String email, String address, String cellphoneNumber) throws SQLException {
        // Validar se os dados obrigatórios estão presentes
        if (name == null || name.trim().isEmpty()) {
            return new UpdateAccountResult(false, "Nome é obrigatório");
        }
        
        if (email == null || email.trim().isEmpty()) {
            return new UpdateAccountResult(false, "Email é obrigatório");
        }
        
        if (cellphoneNumber == null || cellphoneNumber.trim().isEmpty()) {
            return new UpdateAccountResult(false, "Telefone é obrigatório");
        }
        
        if (address == null || address.trim().isEmpty()) {
            return new UpdateAccountResult(false, "Endereço é obrigatório");
        }

        // Verificar se o email ou telefone já existem para outro usuário
        if (accountsRepository.checkIfAlreadyExistsForUpdate(accountId, email, cellphoneNumber)) {
            return new UpdateAccountResult(false, "Email ou telefone já estão em uso por outro usuário");
        }

        // Buscar a conta atual
        Account currentAccount = accountsRepository.findById(accountId);
        if (currentAccount == null) {
            return new UpdateAccountResult(false, "Conta não encontrada");
        }

        // Criar nova instância com dados atualizados (mantendo dados imutáveis)
        Account updatedAccount = new Account(
            currentAccount.getId(),
            currentAccount.getNumber(),
            currentAccount.getDigit(),
            currentAccount.getCpf(),
            name.trim(),
            email.trim(),
            currentAccount.getPassword(),
            currentAccount.getBirthDate(),
            address.trim(),
            cellphoneNumber.trim(),
            currentAccount.getBalance(),
            currentAccount.getStatus(),
            currentAccount.getCreatedAt()
        );

        // Atualizar no banco
        accountsRepository.update(updatedAccount);

        return new UpdateAccountResult(true, "Dados atualizados com sucesso", updatedAccount);
    }

    public static class UpdateAccountResult {
        private boolean success;
        private String message;
        private Account account;

        public UpdateAccountResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public UpdateAccountResult(boolean success, String message, Account account) {
            this.success = success;
            this.message = message;
            this.account = account;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }

        public Account getAccount() {
            return account;
        }
    }
}

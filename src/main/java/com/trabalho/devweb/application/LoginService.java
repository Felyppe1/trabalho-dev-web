package com.trabalho.devweb.application;

import com.trabalho.devweb.application.interfaces.IAccountsRepository;
import com.trabalho.devweb.domain.Account;
import org.mindrot.jbcrypt.BCrypt;

public class LoginService {
    private final IAccountsRepository accountsRepository;

    public LoginService(IAccountsRepository accountsRepository) {
        this.accountsRepository = accountsRepository;
    }

    public Account execute(String email, String password) throws Exception {
        Account account = accountsRepository.findOneByEmail(email);
        if (account == null || !BCrypt.checkpw(password, account.getPassword())) {
            throw new Exception("Usuário ou senha inválidos.");
        }
        return account;
    }
}

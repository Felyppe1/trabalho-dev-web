package com.trabalho.devweb.application.interfaces;

import com.trabalho.devweb.domain.Transfer;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;

public interface ITransfersRepository {
   // Buscar apenas transferências válidas (origin_id e target_id não nulos)
    List<Transfer> findTransfersByAccount(String accountId, int offset, int limit, String direction, String nameFilter) throws SQLException;

    int countTransfersByAccount(String accountId, String direction, String nameFilter) throws SQLException;

    List<Transfer> findTopFrequentRecipients(String accountId, int limit) throws SQLException;

    BigDecimal getTotalSent(String accountId) throws SQLException;

    BigDecimal getTotalReceived(String accountId) throws SQLException;

    
       // Salvar uma transferência (inserir no banco)
    void save(Transfer transfer) throws SQLException;
}

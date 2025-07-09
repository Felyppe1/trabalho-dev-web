package com.trabalho.devweb.infrastructure.repositories;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.trabalho.devweb.infrastructure.databaseconnection.PostgresConnection;
import com.trabalho.devweb.domain.Transaction;

public class TransactionRepository{
    

    public List<Transaction> findByAccountId(String accountId){
        List<Transaction> list = new ArrayList<>();

        String searchTrasaction = "SELECT * FROM transaction WHERE origin_id = ? OR target_id = ? ORDER BY created_at DESC";
        try(Connection conn = PostgresConnection.getConnection()){
            PreparedStatement statement = conn.prepareStatement(searchTrasaction);
            statement.setString(1, accountId);
            statement.setString(2, accountId);
            
            ResultSet result = statement.executeQuery();  
            
            while (result.next()) {
                Transaction transactions = new Transaction();
                transactions.setOriginId(result.getString("oringin_id"));
                transactions.setTargetId(result.getString("target_id"));
                transactions.setCreatedAt(result.getTimestamp("created_at").toLocalDateTime());
                transactions.setType(result.getString("type"));
                transactions.setAmount(result.getBigDecimal("amount"));
                transactions.setBalanceAfter(result.getBigDecimal("balance_after"));
                transactions.setDescription(result.getString("description"));
                list.add(transactions);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
    
}

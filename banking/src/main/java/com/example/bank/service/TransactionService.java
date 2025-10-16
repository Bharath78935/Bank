package com.example.bank.service;

import com.example.bank.model.Transaction;
import java.util.List;

public interface TransactionService {
    String createTransaction(Transaction transaction);
    Transaction getTransaction(int transactionId);
    List<Transaction> getTransactionsByAccountNumber(String accountNumber);
}

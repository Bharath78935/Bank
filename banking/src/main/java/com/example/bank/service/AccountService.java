package com.example.bank.service;

import java.sql.SQLException;

import com.example.bank.model.Account;

public interface AccountService {
    void createAccount(Account account);
    Account getAccount(long accountNumber);
    Account getAccountById(int accountId);
    void updateAccount(Account account);
    void deleteAccount(int account_id);
    Account getAccountByCustomerId(int customerId) throws SQLException;

}

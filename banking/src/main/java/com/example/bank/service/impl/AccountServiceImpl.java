package com.example.bank.service.impl;

import com.example.bank.DAO.AccountDAO;
import com.example.bank.model.Account;
import com.example.bank.service.AccountService;
//import com.example.bank.util.ValidationUtil;

import java.sql.SQLException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AccountServiceImpl implements AccountService {
    private static final Logger logger = LogManager.getLogger(AccountServiceImpl.class);

    private final AccountDAO DAO;

     public AccountServiceImpl() {
        this.DAO = new AccountDAO();
    }
    public AccountServiceImpl(AccountDAO accountDAO) {
        this.DAO = accountDAO;
    }

    @Override
    public void createAccount(Account account) {
        try {
            //ValidationUtil.validateAccount(account,);
            DAO.createAccount(account);
            logger.info("Account created: " + account.getAccountNumber());
        } catch (IllegalArgumentException e) {
            logger.warn("Validation failed: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error creating account", e);
        }
    }

    @Override
    public Account getAccount(long accountNumber) {
        try {
            return DAO.getAccount(accountNumber);
        } catch (Exception e) {
            logger.error("Error fetching account", e);
            return null;
        }
    }

    @Override
    public void updateAccount(Account account) {
        try {
            // ValidationUtil.validateAccount(account,);
            DAO.updateAccount(account);
            logger.info("Account updated: " + account.getAccountNumber());
        } catch (IllegalArgumentException e) {
            logger.warn("Validation failed: " + e.getMessage());
            throw e;
        } catch (Exception e) {
            logger.error("Error updating account", e);
        }
    }

    public Account getAccountById(int accountId) {
    try {
        return DAO.getAccountById(accountId);
    } catch (Exception e) {
        logger.error("Error fetching account by ID: " + accountId, e);
        return null;
    }
}

    @Override
    public void deleteAccount(int accountId) {
    try {
        DAO.deleteAccount(accountId); // DAO method now expects account_id
        logger.info("Account deleted: " + accountId);
    } catch (Exception e) {
        logger.error("Error deleting account", e);
    }
}

    @Override
    public Account getAccountByCustomerId(int customerId) throws SQLException {
        return DAO.getAccountByCustomerId(customerId);
    }

}

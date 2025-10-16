package com.example.bank.service.impl;

import com.example.bank.DAO.TransactionDAO;
import com.example.bank.DAO.AccountDAO;
import com.example.bank.DAO.CustomerDAO;
import com.example.bank.model.Transaction;
import com.example.bank.model.Account;
import com.example.bank.model.Customer;
import com.example.bank.service.TransactionService;
import com.example.bank.service.EmailService;
import com.example.bank.storage.DBConnect;

import java.sql.Connection;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TransactionServiceImpl implements TransactionService {

    private static final Logger logger = LogManager.getLogger(TransactionServiceImpl.class);

     private final TransactionDAO transactionDAO;
    private final AccountDAO accountDAO;
    private final CustomerDAO customerDAO;
    
    public TransactionServiceImpl(TransactionDAO transactionDAO, AccountDAO accountDAO, CustomerDAO customerDAO) {
        this.transactionDAO = transactionDAO;
        this.accountDAO = accountDAO;
        this.customerDAO = customerDAO;
    }

    // Default constructor for production
    public TransactionServiceImpl() {
        this.transactionDAO = new TransactionDAO();
        this.accountDAO = new AccountDAO();
        this.customerDAO = new CustomerDAO();
    }
    
    @Override
    public String createTransaction(Transaction transaction) {
        String result = transactionDAO.createTransaction(transaction);
        if (result == null) { 
            try (Connection conn = DBConnect.getConnection()) {
                Account senderAccount = accountDAO.getAccount(transaction.getAccountNumber());
                if (senderAccount != null) {
                    sendTransactionEmails(transaction, senderAccount);
                    logger.info("Emails are sent.");
                }
            } catch (Exception e) {
                logger.error("Failed to fetch sender account for email notification", e);
            }
        }
        return result;
    }

    @Override
    public Transaction getTransaction(int transactionId) {
        try {
            return transactionDAO.getTransaction(transactionId);
        } catch (Exception e) {
            logger.error("Error fetching transaction {}", transactionId, e);
            return null;
        }
    }

    @Override
    public List<Transaction> getTransactionsByAccountNumber(String accountNumber) {
        try {
            return transactionDAO.getTransactionsByAccountNumber(accountNumber);
        } catch (Exception e) {
            logger.error("Error fetching transactions for account {}", accountNumber, e);
            return List.of();
        }
    }

    private void sendTransactionEmails(Transaction transaction, Account senderAccount) {
    try {
        Customer senderCustomer = customerDAO.getCustomerById(senderAccount.getCustomerId());

        final EmailService emailService =
                new EmailService("banking.offical.bank@gmail.com", "lyog ggaf cous ihyd");

        String type = transaction.getTransactionType().toUpperCase();

        switch (type) {
            case "TRANSFER":
                // Debit email to sender
                emailService.sendEmail(
                        "banking.offical.bank@gmail.com",
                        senderCustomer.getEmail(),
                        "Debit Alert",
                        "Hi " + senderCustomer.getCustomerName() + ",\n\n"
                                + "An amount of ₹" + transaction.getTransactionAmount()
                                + " has been debited from your account " + senderAccount.getAccountNumber()
                                + " to " + transaction.getReceiverAccount() + "."
                );

                // Credit email to receiver
                Account receiverAccount = accountDAO.getAccount(transaction.getReceiverAccount());
                if (receiverAccount != null) {
                    Customer receiverCustomer = customerDAO.getCustomerById(receiverAccount.getCustomerId());

                    emailService.sendEmail(
                            "banking.offical.bank@gmail.com",
                            receiverCustomer.getEmail(),
                            "Credit Alert",
                            "Hi " + receiverCustomer.getCustomerName() + ",\n\n"
                                    + "An amount of ₹" + transaction.getTransactionAmount()
                                    + " has been credited into your account " + receiverAccount.getAccountNumber()
                                    + " from " + senderAccount.getAccountNumber() + "."
                    );
                }
                break;

            case "DEPOSIT":
                emailService.sendEmail(
                        "banking.offical.bank@gmail.com",
                        senderCustomer.getEmail(),
                        "Deposit Alert",
                        "Hi " + senderCustomer.getCustomerName() + ",\n\n"
                                + "An amount of ₹" + transaction.getTransactionAmount()
                                + " has been deposited into your account " + senderAccount.getAccountNumber() + "."
                );
                break;

            case "WITHDRAWAL":
                emailService.sendEmail(
                        "banking.offical.bank@gmail.com",
                        senderCustomer.getEmail(),
                        "Withdrawal Alert",
                        "Hi " + senderCustomer.getCustomerName() + ",\n\n"
                                + "An amount of ₹" + transaction.getTransactionAmount()
                                + " has been withdrawn from your account " + senderAccount.getAccountNumber() + "."
                );
                break;

            default:
                logger.warn("Unknown transaction type: {}", transaction.getTransactionType());
        }

        logger.info("Transaction emails sent successfully for transaction {}", transaction.getTransactionId());

    } catch (Exception e) {
        logger.error("Error sending transaction emails", e);
    }
}


}




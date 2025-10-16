package com.example.bank.model;

import java.sql.Timestamp;

public class Transaction {
    private int transactionId;
    private String utrNumber;
    private Timestamp transactionDate;
    private double transactionAmount;
    private Timestamp debitedDate;
    private int accountId;
    private long accountNumber;
    private double balanceAmount;
    private String description;
    private String modifiedBy;
    private long receiverAccount;
    private String transactionType; 
    private String mode; 
    private String bankBranch;

    // --- Getters and Setters ---
    public int getTransactionId() { return transactionId; }
    public void setTransactionId(int transactionId) { this.transactionId = transactionId; }

    public String getUtrNumber() { return utrNumber; }
    public void setUtrNumber(String utrNumber) { this.utrNumber = utrNumber; }

    public Timestamp getTransactionDate() { return transactionDate; }
    public void setTransactionDate(Timestamp transactionDate) { this.transactionDate = transactionDate; }

    public double getTransactionAmount() { return transactionAmount; }
    public void setTransactionAmount(double transactionAmount) { this.transactionAmount = transactionAmount; }

    public Timestamp getDebitedDate() { return debitedDate; }
    public void setDebitedDate(Timestamp debitedDate) { this.debitedDate = debitedDate; }

    public int getAccountId() { return accountId; }
    public void setAccountId(int accountId) { this.accountId = accountId; }

    public double getBalanceAmount() { return balanceAmount; }
    public void setBalanceAmount(double balanceAmount) { this.balanceAmount = balanceAmount; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getModifiedBy() { return modifiedBy; }
    public void setModifiedBy(String modifiedBy) { this.modifiedBy = modifiedBy; }

    public long getReceiverAccount() { return receiverAccount; }
    public void setReceiverAccount(long receiverAccount) { this.receiverAccount = receiverAccount; }

    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }

    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }

    public String getBankBranch() { return bankBranch; }
    public void setBankBranch(String bankBranch) { this.bankBranch = bankBranch; }

    public long getAccountNumber() {
        return accountNumber;
    }
    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }
}

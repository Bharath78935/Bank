package com.example.bank.util;

import com.example.bank.model.Account;
import com.example.bank.model.Customer;
import com.example.bank.model.Transaction;

public class ValidationUtil {

    public static String validateAccount(Account account,Customer customer) {
        if (customer == null) {
            return "No customer found with the ID: "+ account.getCustomerId();
        }
        if (account == null) return "Account cannot be null.";
        if (account.getAccountNumber() <= 0) return "Account number must be positive.";
        if (account.getBalance() < 0) return "Balance cannot be negative.";
        // if (account.getNameOnAccount() == null || account.getNameOnAccount().isEmpty())
        //     return "Name on account cannot be empty.";
        return null;
    }

    public static String validateTransaction(Transaction transaction, Account account) {
        if (transaction == null) return "Transaction cannot be null.";
        if (transaction.getTransactionAmount() <= 0) return "Transaction amount must be greater than zero.";
        if (account != null && transaction.getTransactionAmount() > account.getBalance()) return "Insufficent Balance";
        if (transaction.getTransactionType() == null || transaction.getTransactionType().isEmpty())
            return "Transaction type is required.";
        if (transaction.getMode() == null || transaction.getMode().isEmpty())
            return "Transaction mode is required.";
        if (transaction.getBankBranch() == null || transaction.getBankBranch().isEmpty())
            return "Bank branch is required.";
        if ("TRANSFER".equalsIgnoreCase(transaction.getTransactionType())) {
            if (transaction.getReceiverAccount() == 0)
                return "Receiver account is required for transfer.";
            if (transaction.getAccountNumber() == (transaction.getReceiverAccount()))
                return "Sender and receiver accounts cannot be the same.";
        }
        return null;
    }

     public static String validateCustomer(Customer customer) {
        if (customer == null) return "Customer cannot be null.";
        if (customer.getCustomerName() == null || customer.getCustomerName().isEmpty())
            return "Customer name cannot be empty.";
        if (customer.getEmail() == null || customer.getEmail().isEmpty())
            return "Email cannot be empty.";
        if (!customer.getEmail().matches("^[\\w.-]+@[\\w.-]+\\.\\w+$"))
            return "Invalid email format.";
        if (customer.getAadharNumber() <= 0)
            return "Aadhar number must be positive.";
        if (customer.getPhoneNumber() != 0 && !String.valueOf(customer.getPhoneNumber()).matches("\\d{10}"))
            return "Phone number must be 10 digits.";
        if (customer.getAge() != 0 && customer.getAge() < 0)
            return "Age cannot be negative.";
        if (customer.getDob() != null && customer.getDob().isEmpty())
            return "DOB cannot be empty if provided.";
        if (String.valueOf(customer.getPin()).length() != 4) {
            return "PIN must be exactly 4 digits.";
        }
        if (customer.getGender() != null &&
                !customer.getGender().equalsIgnoreCase("Male") &&
                !customer.getGender().equalsIgnoreCase("Female") &&
                !customer.getGender().equalsIgnoreCase("Other"))
            return "Gender must be Male, Female, or Other.";
        return null;
    }
}

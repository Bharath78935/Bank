package com.example.bank.controller;

import com.example.bank.service.*;
import com.example.bank.model.Account;
import com.example.bank.model.Transaction;
import com.example.bank.service.impl.AccountServiceImpl;
import com.example.bank.service.impl.TransactionServiceImpl;
import com.example.bank.util.ValidationUtil;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.text.SimpleDateFormat;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Path("/transactions")
public class TransactionController {

     private static final Logger logger = LogManager.getLogger(TransactionController.class);

    private final TransactionService service = new TransactionServiceImpl();
    AccountService accountService = new AccountServiceImpl();

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(Transaction transaction) {
        if (transaction == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Transaction cannot be null").build();
        }

        Account account = accountService.getAccount(transaction.getAccountId());
        // if (account == null) {
        //     return Response.status(Response.Status.NOT_FOUND)
        //             .entity(Map.of("error", "Account not found with ID: " + transaction.getAccountId()))
        //             .build();
        // }
        String validationError = ValidationUtil.validateTransaction(transaction, account);
        if (validationError != null) {
            logger.warn("Validation failed: " + validationError);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", validationError))
                    .build();
        }
        String result = service.createTransaction(transaction);
        if (result != null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(Map.of("error", result))
                    .build();
        }

        Map<String, Object> response = new HashMap<>();
        logger.info("Transaction processed successfully");
        response.put("message", "Transaction processed successfully");
        response.put("accountId", transaction.getAccountId());
        return Response.status(Response.Status.CREATED)
                .entity(response)
                .build();
    }

    @GET
    @Path("/{transactionId}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("transactionId") int transactionId) {
        Transaction txn = service.getTransaction(transactionId);
        if (txn == null) {
            logger.warn("No transaction fount with ID :" + transactionId);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity(Map.of("error", "Transaction not found with ID: " + transactionId))
                    .build();
        }
        return Response.ok(txn).build();
    }

    @GET
    @Path("/transaction/account/{accountNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getTransactionsJson(@PathParam("accountNumber") String accountNumber) {
        List<Transaction> txns = service.getTransactionsByAccountNumber(accountNumber);
        if (txns == null || txns.isEmpty()) {
            return Response.status(Response.Status.NOT_FOUND)
                           .entity("No transactions found for account number: " + accountNumber)
                           .build();
        }
        return Response.ok(txns).build();
    }

    @GET
    @Path("/account/{accountNumber}")
    @Produces("text/csv")
    public Response getByAccount(@PathParam("accountNumber") String accountNumber) {
        logger.info("Request for all transactions with account number :" + accountNumber);
        List<Transaction> txns = service.getTransactionsByAccountNumber(accountNumber);

        if (txns == null || txns.isEmpty()) {
            logger.warn("No transactions found for account number: " + accountNumber);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No transactions found for account number: " + accountNumber)
                    .build();
        }

        StringBuilder csvBuilder = new StringBuilder();
        csvBuilder.append("Transaction ID,UTR Number,Transaction Date,Amount,Debited Date,Description,Type,Mode,Balance,Receiver Account,Bank Branch\n");

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (Transaction txn : txns) {
            csvBuilder.append(txn.getTransactionId()).append(",");
            csvBuilder.append(txn.getUtrNumber()).append(",");
            csvBuilder.append(sdf.format(txn.getTransactionDate())).append(",");
            csvBuilder.append(txn.getTransactionAmount()).append(",");
            csvBuilder.append(sdf.format(txn.getDebitedDate())).append(",");
            csvBuilder.append("\"").append(txn.getDescription() != null ? txn.getDescription().replace("\"", "\"\"") : "").append("\",");
            csvBuilder.append(txn.getTransactionType()).append(",");
            csvBuilder.append(txn.getMode()).append(",");
            csvBuilder.append(txn.getBalanceAmount()).append(",");
            csvBuilder.append(txn.getReceiverAccount()).append(",");
            csvBuilder.append(txn.getBankBranch()).append("\n");
        }
        String csvContent = csvBuilder.toString();

        return Response.ok(csvContent)
                .header("Content-Disposition", "attachment; filename=\"transactions_" + accountNumber + ".csv\"")
                .header("Content-Type", "text/csv; charset=UTF-8")
                .header("Cache-Control", "no-cache, no-store, must-revalidate")
                .header("Pragma", "no-cache")
                .header("Expires", "0")
                .build();
    }

}

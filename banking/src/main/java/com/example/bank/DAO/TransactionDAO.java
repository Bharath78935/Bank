package com.example.bank.DAO;

import com.example.bank.model.Transaction;
import com.example.bank.model.Account;
import com.example.bank.storage.DBConnect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// Utility to generate Unique Transaction Reference (UTR)
class UTRGenerator {
    public static String generateUTR() {
        return "UTR" + UUID.randomUUID().toString().replace("-", "").substring(0, 16).toUpperCase();
    }
}

public class TransactionDAO {

    private static final Logger logger = LogManager.getLogger(TransactionDAO.class);

    //Account Retrieval
    public Account getAccountById(Connection conn, int accountId) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE account_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAccount(rs);
                }
            }
        }
        return null;
    }

    public Account getAccountByNumber(Connection conn, long accountNumber) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE account_number = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, accountNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToAccount(rs);
                }
            }
        }
        return null;
    }
    public String createTransaction(Transaction transaction) {
        String insertTxnSQL =
                "INSERT INTO transactions (utr_number, transaction_amount, " +
                "account_id, accountNumber, balance_amount, description, modified_by, receiverAccount, transaction_type, mode, bank_branch) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        String updateAccountSQL = "UPDATE accounts SET balance=? WHERE account_number=?";

        try (Connection conn = DBConnect.getConnection()) {
            conn.setAutoCommit(false);

            try (
                PreparedStatement insertTxn = conn.prepareStatement(insertTxnSQL, Statement.RETURN_GENERATED_KEYS);
                PreparedStatement updateAccount = conn.prepareStatement(updateAccountSQL)
            ) {
                // Get sender account
                Account senderAcc = getAccountByNumber(conn, transaction.getAccountNumber());
                if (senderAcc == null) {
                    return "Sender account not found with Account Number " + transaction.getAccountNumber();
                }
                transaction.setAccountId(senderAcc.getAccountId());
                // Generate UTR
                String utr = UTRGenerator.generateUTR();
                transaction.setUtrNumber(utr);

                double newBalance = senderAcc.getBalance();

                switch (transaction.getTransactionType().toUpperCase()) {
                    case "DEPOSIT":
                        newBalance += transaction.getTransactionAmount();
                        break;

                    case "WITHDRAWAL":
                        if (newBalance < transaction.getTransactionAmount()) {
                            return "Insufficient balance for withdrawal.";
                        }
                        newBalance -= transaction.getTransactionAmount();
                        break;

                    case "TRANSFER":
                        if (newBalance < transaction.getTransactionAmount()) {
                            return "Insufficient balance for transfer.";
                        }
                        newBalance -= transaction.getTransactionAmount();

                        Account receiverAcc = getAccountByNumber(conn, transaction.getReceiverAccount());
                        if (receiverAcc == null) {
                            return "Receiver account not found with number " + transaction.getReceiverAccount();
                        }

                        double receiverNewBalance = receiverAcc.getBalance() + transaction.getTransactionAmount();

                        // Update receiver balance
                        try (PreparedStatement updateReceiver = conn.prepareStatement(updateAccountSQL)) {
                            updateReceiver.setDouble(1, receiverNewBalance);
                            updateReceiver.setLong(2, receiverAcc.getAccountNumber());
                            updateReceiver.executeUpdate();
                        }

                        // Insert receiver's CREDIT transaction
                        try (PreparedStatement insertReceiverTxn = conn.prepareStatement(insertTxnSQL)) {
                            insertReceiverTxn.setString(1, utr);
                            insertReceiverTxn.setDouble(2, transaction.getTransactionAmount());
                            insertReceiverTxn.setInt(3, receiverAcc.getAccountId());
                            insertReceiverTxn.setString(4, String.valueOf(receiverAcc.getAccountNumber()));
                            insertReceiverTxn.setDouble(5, receiverNewBalance);
                            insertReceiverTxn.setString(6, "Transfer from Account " + senderAcc.getAccountNumber());
                            insertReceiverTxn.setString(7, transaction.getModifiedBy());
                            insertReceiverTxn.setString(8, String.valueOf(senderAcc.getAccountNumber()));
                            insertReceiverTxn.setString(9, "CREDIT");
                            insertReceiverTxn.setString(10, transaction.getMode());
                            insertReceiverTxn.setString(11, transaction.getBankBranch());
                            insertReceiverTxn.executeUpdate();
                        }
                        break;

                    default:
                        return "Unknown transaction type: " + transaction.getTransactionType();
                }

                // Insert sender's transaction
                // acc.setCreatedAt(rs.getTimestamp("created_at"));
                //acc.setModifiedAt(rs.getTimestamp("modified_at"));
                 insertTxn.setString(1, transaction.getUtrNumber());
                insertTxn.setDouble(2, transaction.getTransactionAmount());
                insertTxn.setInt(3, transaction.getAccountId());
                insertTxn.setString(4, String.valueOf(senderAcc.getAccountNumber()));
                insertTxn.setDouble(5, newBalance);
                insertTxn.setString(6, transaction.getDescription());
                insertTxn.setString(7, transaction.getModifiedBy());
                insertTxn.setLong(8, transaction.getReceiverAccount());
                insertTxn.setString(9, transaction.getTransactionType());
                insertTxn.setString(10, transaction.getMode());
                insertTxn.setString(11, transaction.getBankBranch());
                insertTxn.executeUpdate();

                // Update sender balance
                updateAccount.setDouble(1, newBalance);
                updateAccount.setLong(2, transaction.getAccountNumber());
                updateAccount.executeUpdate();

                conn.commit();
                logger.info("Transaction processed successfully for Account Number {}", senderAcc.getAccountNumber());
                return null; // success

            } catch (SQLException ex) {
                conn.rollback();
                logger.error("Transaction failed: {}", ex.getMessage());
                return "Database error: " + ex.getMessage();
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (SQLException e) {
            return "Database connection error: " + e.getMessage();
        }
    }

    private Account mapResultSetToAccount(ResultSet rs) throws SQLException {
        Account acc = new Account();
        acc.setAccountId(rs.getInt("account_id"));
        acc.setAccountNumber(rs.getLong("account_number"));
        acc.setBalance(rs.getDouble("balance"));
        return acc;
    }

    private Transaction mapResultSetToTransaction(ResultSet rs) throws SQLException {
        Transaction txn = new Transaction();
        txn.setTransactionId(rs.getInt("transaction_id"));
        txn.setUtrNumber(rs.getString("utr_number"));
        txn.setTransactionDate(rs.getTimestamp("transaction_date"));
        txn.setTransactionAmount(rs.getDouble("transaction_amount"));
        txn.setDebitedDate(rs.getTimestamp("debited_date"));
        txn.setAccountId(rs.getInt("account_id"));
        txn.setAccountNumber(rs.getLong("accountNumber"));
        txn.setBalanceAmount(rs.getDouble("balance_amount"));
        txn.setDescription(rs.getString("description"));
        txn.setModifiedBy(rs.getString("modified_by"));
        txn.setReceiverAccount(rs.getLong("receiverAccount"));
        txn.setTransactionType(rs.getString("transaction_type"));
        txn.setMode(rs.getString("mode"));
        txn.setBankBranch(rs.getString("bank_branch"));
        return txn;
    }

    // Queries 
    public List<Transaction> getTransactionsByAccountNumber(String accountNumber) throws SQLException {
        String sql = "SELECT t.* FROM transactions t " +
                     "JOIN accounts a ON a.account_id = t.account_id " +
                     "WHERE a.account_number = ?";
        List<Transaction> transactions = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, accountNumber);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    transactions.add(mapResultSetToTransaction(rs));
                }
            }
        }
        return transactions;
    }

    public Transaction getTransaction(int transactionId) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE transaction_id = ?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, transactionId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToTransaction(rs);
                }
            }
        }
        return null;
    }
}

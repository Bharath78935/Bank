package com.example.bank.DAO;

import com.example.bank.model.Account;
import com.example.bank.model.Customer;
import com.example.bank.storage.DBConnect;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
public class AccountDAO {
    private static final Logger logger = LogManager.getLogger(AccountDAO.class);

    public Customer getCustomerById(Connection conn, int customerId) throws SQLException {
    String sql = "SELECT * FROM customers WHERE customer_id = ?";
    try (PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, customerId);
        try (ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                Customer c = new Customer();
                c.setCustomerId(rs.getInt("customer_id"));
                c.setCustomerName(rs.getString("customer_name"));
                c.setAadharNumber(rs.getLong("aadhar_number"));
                c.setPermanentAddress(rs.getString("permanent_address"));
                c.setState(rs.getString("state"));
                c.setCountry(rs.getString("country"));
                c.setCity(rs.getString("city"));
                c.setEmail(rs.getString("email"));
                c.setPhoneNumber(rs.getLong("phone_number"));
                c.setStatus(rs.getString("status"));
                c.setDob(rs.getString("dob"));
                c.setAge(rs.getInt("age"));
                c.setCreatedOn(rs.getTimestamp("created_on"));
                c.setModifiedOn(rs.getTimestamp("modified_on"));
                c.setGender(rs.getString("gender"));
                c.setFatherName(rs.getString("father_name"));
                c.setMotherName(rs.getString("mother_name"));
                return c;
            }
        }
    }
    return null;
}


    public void createAccount(Account account) throws SQLException {

    String sql = "INSERT INTO accounts (customer_id, account_type, account_number, bank_name, branch, balance, status, " +
                 "ifsc_code, name_on_account, phone_linked, saving_amount) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = DBConnect.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

        Customer acc = getCustomerById(conn, account.getCustomerId());
        if(acc == null){
            logger.warn("Customer not found: {}", account.getCustomerId());
            return;
        }
        account.setNameOnAccount(acc.getCustomerName());
        account.setPhoneLinked(acc.getPhoneNumber());
        stmt.setInt(1, account.getCustomerId());
        stmt.setString(2, account.getAccountType());
        stmt.setLong(3, account.getAccountNumber());
        stmt.setString(4, account.getBankName());
        stmt.setString(5, account.getBranch());
        stmt.setDouble(6, account.getBalance());
        stmt.setString(7, account.getStatus());
        stmt.setString(8, account.getIfscCode());
        stmt.setString(9, account.getNameOnAccount());
        stmt.setLong(10, account.getPhoneLinked());
        stmt.setDouble(11, account.getSavingAmount());
        stmt.executeUpdate();

        logger.info("Account created: {}", account.getAccountNumber());
    }
}

    public Account getAccount(long accountNumber) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE account_number=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, accountNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Account acc = new Account();
                acc.setAccountId(rs.getInt("account_id"));
                acc.setCustomerId(rs.getInt("customer_id"));
                acc.setAccountType(rs.getString("account_type"));
                acc.setBankName(rs.getString("bank_name"));
                acc.setBranch(rs.getString("branch"));
                acc.setBalance(rs.getDouble("balance"));
                acc.setStatus(rs.getString("status"));
                acc.setCreatedAt(rs.getTimestamp("created_at"));
                acc.setModifiedAt(rs.getTimestamp("modified_at"));
                acc.setAccountNumber(rs.getLong("account_number"));
                acc.setIfscCode(rs.getString("ifsc_code"));
                acc.setNameOnAccount(rs.getString("name_on_account"));
                acc.setPhoneLinked(rs.getLong("phone_linked"));
                acc.setSavingAmount(rs.getDouble("saving_amount"));
                return acc;
            }
        }
        return null;
    }
    public void updateAccount(Account account) throws SQLException {
        String sql = "UPDATE accounts SET account_type=?, bank_name=?, branch=?, balance=?, status=?, " +
                     "ifsc_code=?, name_on_account=?, phone_linked=?, saving_amount=? " +
                     "WHERE account_number=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, account.getAccountType());
            stmt.setString(2, account.getBankName());
            stmt.setString(3, account.getBranch());
            stmt.setDouble(4, account.getBalance());
            stmt.setString(5, account.getStatus());
            stmt.setString(6, account.getIfscCode());
            stmt.setString(7, account.getNameOnAccount());
            stmt.setLong(8, account.getPhoneLinked());
            stmt.setDouble(9, account.getSavingAmount());
            stmt.setLong(10, account.getAccountNumber());

            stmt.executeUpdate();
            logger.info("Account updated: {}", account.getAccountNumber());
        }
    }

    public void deleteAccount(int accountId) throws SQLException {
    String sql = "DELETE FROM accounts WHERE account_id=?";
    try (Connection conn = DBConnect.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, accountId);
        stmt.executeUpdate();
        logger.info("Account deleted: {}", accountId);
    }
}


    public Account getAccountByCustomerId(int customerId) throws SQLException {
    String sql = "SELECT * FROM accounts WHERE customer_id = ?";
    try (Connection conn = DBConnect.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setInt(1, customerId);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            Account account = new Account();
            account.setAccountId(rs.getInt("account_id"));
            account.setCustomerId(rs.getInt("customer_id"));
            account.setAccountNumber(rs.getLong("account_number"));
            account.setAccountType(rs.getString("account_type"));
            account.setBankName(rs.getString("bank_name"));
            account.setBranch(rs.getString("branch"));
            account.setBalance(rs.getDouble("balance"));
            account.setStatus(rs.getString("status"));
            account.setIfscCode(rs.getString("ifsc_code"));
            account.setNameOnAccount(rs.getString("name_on_account"));
            account.setPhoneLinked(rs.getLong("phone_linked"));
            account.setSavingAmount(rs.getDouble("saving_amount"));
            account.setCreatedAt(rs.getTimestamp("created_at"));
            account.setModifiedAt(rs.getTimestamp("modified_at"));
            return account;
        }
        return null;
    }
}


        public Account getAccountById(int accountId) throws SQLException {
        String sql = "SELECT * FROM accounts WHERE account_id = ?";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, accountId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Account account = new Account();
                account.setAccountId(rs.getInt("account_id"));
                account.setAccountNumber(rs.getLong("account_number"));
                account.setCustomerId(rs.getInt("customer_id"));
                account.setAccountType(rs.getString("account_type"));
                account.setBankName(rs.getString("bank_name"));
                account.setBranch(rs.getString("branch"));
                account.setBalance(rs.getDouble("balance"));
                account.setStatus(rs.getString("status"));
                account.setIfscCode(rs.getString("ifsc_code"));
                account.setNameOnAccount(rs.getString("name_on_account"));
                account.setSavingAmount(rs.getDouble("saving_amount"));
                return account;
            } else {
                return null;
            }
        }
    }

}

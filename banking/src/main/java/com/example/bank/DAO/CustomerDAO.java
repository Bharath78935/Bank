package com.example.bank.DAO;

import com.example.bank.model.Customer;
import com.example.bank.storage.DBConnect;
import com.example.bank.util.PasswordUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAO {

    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
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

        c.setUsername(rs.getString("username"));
        c.setPassword(rs.getString("password")); 
        //c.setPin(rs.getInt("pin"));
        return c;
    }

    public void createCustomer(Customer customer) throws SQLException {
    String sql = "INSERT INTO customers (customer_name, aadhar_number, permanent_address, state, country, city, " +
                 "email, phone_number, status, dob, age, created_on, modified_on, gender, father_name, mother_name, " +
                 "username, password, pin) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, NOW(), NOW(), ?, ?, ?, ?, ?, ?)";

    try (Connection conn = DBConnect.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setString(1, customer.getCustomerName());
        stmt.setLong(2, customer.getAadharNumber());
        stmt.setString(3, customer.getPermanentAddress());
        stmt.setString(4, customer.getState());
        stmt.setString(5, customer.getCountry());
        stmt.setString(6, customer.getCity());
        stmt.setString(7, customer.getEmail());
        stmt.setLong(8, customer.getPhoneNumber());
        stmt.setString(9, customer.getStatus());
        stmt.setString(10, customer.getDob());
        stmt.setInt(11, customer.getAge());
        stmt.setString(12, customer.getGender());
        stmt.setString(13, customer.getFatherName());
        stmt.setString(14, customer.getMotherName());

        stmt.setString(15, customer.getUsername());
        stmt.setString(16, PasswordUtil.hash(customer.getPassword())); // hash password
        stmt.setString(17, PasswordUtil.hash(String.valueOf(customer.getPin())));      // hash pin

        stmt.executeUpdate();
    }
}

    public Customer getCustomerById(int customerId) throws SQLException {
        String sql = "SELECT customer_id, customer_name, aadhar_number, permanent_address, state, country, city, " +
                 "email, phone_number, status, dob, age, created_on, modified_on, gender, father_name, mother_name, username " +
                 "FROM customers WHERE customer_id = ?";

        try (Connection conn = DBConnect.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, customerId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Customer customer = new Customer();
                    customer.setCustomerId(rs.getInt("customer_id"));
                    customer.setCustomerName(rs.getString("customer_name"));
                    customer.setAadharNumber(rs.getLong("aadhar_number"));
                    customer.setPermanentAddress(rs.getString("permanent_address"));
                    customer.setState(rs.getString("state"));
                    customer.setCountry(rs.getString("country"));
                    customer.setCity(rs.getString("city"));
                    customer.setEmail(rs.getString("email"));
                    customer.setPhoneNumber(rs.getLong("phone_number"));
                    customer.setStatus(rs.getString("status"));
                    customer.setDob(rs.getString("dob"));
                    customer.setAge(rs.getInt("age"));
                    customer.setCreatedOn(rs.getTimestamp("created_on"));
                    customer.setModifiedOn(rs.getTimestamp("modified_on"));
                    customer.setGender(rs.getString("gender"));
                    customer.setFatherName(rs.getString("father_name"));
                    customer.setMotherName(rs.getString("mother_name"));
                    customer.setUsername(rs.getString("username"));

                    return customer;
                }
            }
        }   
            return null;
    }

    public Customer getCustomerByUsername(String username) throws SQLException {
        String sql = "SELECT * FROM customers WHERE username = ?";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return mapResultSetToCustomer(rs);
            }
        }
        return null;
    }


    public Customer getCustomerByAadhar(long aadharNumber) throws SQLException {
        String sql = "SELECT * FROM customers WHERE aadhar_number = ?";
        try (Connection conn = DBConnect.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, aadharNumber);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToCustomer(rs);
                }
            }
        }
        return null; // returns null if no customer found
    }

    public List<Customer> getAllCustomers() throws SQLException {
        String sql = "SELECT * FROM customers";
        List<Customer> customers = new ArrayList<>();
        try (Connection conn = DBConnect.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
        }
        return customers;
    }

    public void updateCustomer(Customer customer) throws SQLException {
    String sql = "UPDATE customers SET " +
                 "customer_name=?, aadhar_number=?, permanent_address=?, state=?, country=?, city=?, " +
                 "email=?, phone_number=?, status=?, dob=?, age=?, modified_on=NOW(), gender=?, father_name=?, mother_name=?, " +
                 "username=?, password=?, pin=? WHERE customer_id=?";

    try (Connection conn = DBConnect.getConnection();
        PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, customer.getCustomerName());
            stmt.setLong(2, customer.getAadharNumber());
            stmt.setString(3, customer.getPermanentAddress());
            stmt.setString(4, customer.getState());
            stmt.setString(5, customer.getCountry());
            stmt.setString(6, customer.getCity());
            stmt.setString(7, customer.getEmail());
            stmt.setLong(8, customer.getPhoneNumber());
            stmt.setString(9, customer.getStatus());
            stmt.setString(10, customer.getDob());
            stmt.setInt(11, customer.getAge());
            stmt.setString(12, customer.getGender());
            stmt.setString(13, customer.getFatherName());
            stmt.setString(14, customer.getMotherName());
            stmt.setString(15, customer.getUsername());
            stmt.setString(16, PasswordUtil.hash(customer.getPassword())); // hash password
            stmt.setString(17, PasswordUtil.hash(String.valueOf(customer.getPin()))); // hash pin
            stmt.setInt(18, customer.getCustomerId()); // ✅ where condition (required)

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Customer updated successfully: " + customer.getCustomerId());
            } else {
                System.out.println("No customer found with ID: " + customer.getCustomerId());
            }
        }
    }


    public void deleteCustomer(int customerId) throws SQLException {
        String sql = "DELETE FROM customers WHERE customer_id=?";
        try (Connection conn = DBConnect.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, customerId);
            stmt.executeUpdate();
        }
    }
}

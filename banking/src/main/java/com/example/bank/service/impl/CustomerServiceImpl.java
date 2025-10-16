package com.example.bank.service.impl;

import com.example.bank.DAO.CustomerDAO;
import com.example.bank.model.Customer;
import com.example.bank.service.CustomerService;
import com.example.bank.util.ValidationUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class CustomerServiceImpl implements CustomerService {
    private static final Logger logger = LogManager.getLogger(CustomerServiceImpl.class);
    private final CustomerDAO DAO = new CustomerDAO();

    @Override
    public void createCustomer(Customer customer) {
        String validationError = ValidationUtil.validateCustomer(customer);
        if (validationError != null) {
            logger.warn("Validation failed: " + validationError);
            return;
        }
        try {
            DAO.createCustomer(customer);
            logger.info("Customer created: " + customer.getEmail());
        } catch (Exception e) {
            logger.error("Error creating customer", e);
        }
    }

    @Override
    public Customer getCustomerByAadhar(long aadharNumber) {
        if (aadharNumber <= 0) {
            logger.warn("Invalid Aadhar number: " + aadharNumber);
            return null;
        }
        try {
            Customer customer = DAO.getCustomerByAadhar(aadharNumber);
            if (customer != null) {
                logger.info("Customer found with Aadhar: " + aadharNumber);
            } else {
                logger.warn("Customer not found with Aadhar: " + aadharNumber);
            }
            return customer;
        } catch (Exception e) {
            logger.error("Error fetching customer by Aadhar: " + aadharNumber, e);
            return null;
        }
    }

    @Override
    public Customer getCustomerByUsername(String username) {
        if (username == null || username.isEmpty()) {
            logger.warn("Username is null or empty");
            return null;
        }
        try {
            return DAO.getCustomerByUsername(username);
        } catch (Exception e) {
            logger.error("Error fetching customer by username: " + username, e);
            return null;
        }
    }

    @Override
    public Customer getCustomerById(int customerId) {
        if (customerId <= 0) {
            logger.warn("Invalid customer ID: " + customerId);
            return null;
        }
        try {
            return DAO.getCustomerById(customerId);
        } catch (Exception e) {
            logger.error("Error fetching customer by ID: " + customerId, e);
            return null;
        }
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        if (email == null || email.isEmpty()) {
            logger.warn("Email is null or empty");
            return null;
        }
        try {
            List<Customer> customers = DAO.getAllCustomers();
            for (Customer c : customers) {
                if (c.getEmail().equalsIgnoreCase(email)) {
                    logger.info("Customer found: " + email);
                    return c;
                }
            }
            logger.warn("Customer not found: " + email);
            return null;
        } catch (Exception e) {
            logger.error("Error fetching customer by email: " + email, e);
            return null;
        }
    }

    @Override
    public List<Customer> getAllCustomers() {
        try {
            return DAO.getAllCustomers();
        } catch (Exception e) {
            logger.error("Error fetching all customers", e);
            return null;
        }
    }

    @Override
    public void updateCustomer(Customer customer) {
        String validationError = ValidationUtil.validateCustomer(customer);
        if (validationError != null) {
            logger.warn("Validation failed: " + validationError);
            return;
        }
        try {
            DAO.updateCustomer(customer);
            logger.info("Customer updated: " + customer.getEmail());
        } catch (Exception e) {
            logger.error("Error updating customer: " + customer.getEmail(), e);
        }
    }

    @Override
    public void deleteCustomer(int customerId) {
        if (customerId <= 0) {
            logger.warn("Invalid customer ID: " + customerId);
            return;
        }
        try {
            DAO.deleteCustomer(customerId);
            logger.info("Customer deleted: " + customerId);
        } catch (Exception e) {
            logger.error("Error deleting customer: " + customerId, e);
        }
    }
}

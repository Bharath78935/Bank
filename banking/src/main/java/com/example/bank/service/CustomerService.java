package com.example.bank.service;

import com.example.bank.model.Customer;
import java.util.List;

public interface CustomerService {

    void createCustomer(Customer customer);
    Customer getCustomerById(int customerId);
    Customer getCustomerByEmail(String email);
    Customer getCustomerByAadhar(long aadhar_number);
    Customer getCustomerByUsername(String username);
    List<Customer> getAllCustomers();
    void updateCustomer(Customer customer);
    void deleteCustomer(int customerId);
}

package com.example.bank.controller;

import com.example.bank.model.Account;
import com.example.bank.model.Customer;
import com.example.bank.service.AccountService;
import com.example.bank.service.CustomerService;
import jakarta.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountControllerTest {

    private static final Logger logger = LogManager.getLogger(AccountControllerTest.class);

    @Mock
    private AccountService accountService;

    @Mock
    private CustomerService customerService;

    @InjectMocks
    private AccountController controller;

    private Account sampleAccount;
    private Customer sampleCustomer;

    @BeforeEach
    void setUp() {
        sampleAccount = new Account();
        sampleAccount.setAccountNumber(1234567890L);
        sampleAccount.setCustomerId(1);
        sampleAccount.setBankName("Test Bank");
        sampleAccount.setBranch("Hyderabad");
        sampleAccount.setAccountType("Savings");
        sampleAccount.setBalance(1000.0);
        sampleAccount.setIfscCode("TEST0001");

        sampleCustomer = new Customer();
        sampleCustomer.setCustomerId(1);
        sampleCustomer.setCustomerName("John Doe");

        controller.customerService = customerService;
    }

    // Success cases
    @Test
    void testCreateAccount_Success() {
        when(customerService.getCustomerById(1)).thenReturn(sampleCustomer);
        when(accountService.getAccount(1234567890L)).thenReturn(null);

        Response response = controller.create(sampleAccount);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("successfully"));
        verify(accountService, times(1)).createAccount(sampleAccount);
        logger.info("testCreateAccount_Success completed successfully");
    }

    @Test
    void testGetAccount_Success() {
        when(accountService.getAccount(1234567890L)).thenReturn(sampleAccount);

        Response response = controller.get(1234567890L);

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        Account result = (Account) response.getEntity();
        assertEquals("Test Bank", result.getBankName());
        logger.info("testGetAccount_Success completed successfully for account: {}", result.getAccountNumber());
    }

    // Failure test cases
    @Test
    void testCreateAccount_Failure_NullAccount() {
        Response response = controller.create(null);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("cannot be null"));
        verify(accountService, never()).createAccount(any());
        logger.warn("testCreateAccount_Failure_NullAccount completed; null account correctly rejected");
    }

    @Test
    void testGetAccount_Failure_NotFound() {
        when(accountService.getAccount(111111L)).thenReturn(null);

        Response response = controller.get(111111L);

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
        assertTrue(response.getEntity().toString().contains("No Account found"));
        logger.warn("testGetAccount_Failure_NotFound completed; account not found as expected");
    }
}

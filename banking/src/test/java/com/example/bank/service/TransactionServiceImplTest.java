package com.example.bank.service;

import com.example.bank.DAO.TransactionDAO;
import com.example.bank.DAO.AccountDAO;
import com.example.bank.DAO.CustomerDAO;
import com.example.bank.model.Transaction;
import com.example.bank.model.Account;
import com.example.bank.model.Customer;
import com.example.bank.service.impl.TransactionServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.SQLException;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    private static final Logger logger = LogManager.getLogger(TransactionServiceImplTest.class);

    @Mock
    private TransactionDAO transactionDAO;

    @Mock
    private AccountDAO accountDAO;

    @Mock
    private CustomerDAO customerDAO;

    @InjectMocks
    private TransactionServiceImpl service;

    private Transaction transaction;
    private Account account;
    private Customer customer;

    @BeforeEach
    void setup() {
        transaction = new Transaction();
        transaction.setTransactionId(1);
        transaction.setAccountNumber(123456L);
        transaction.setTransactionType("DEPOSIT");
        transaction.setTransactionAmount(1000.0);
        transaction.setModifiedBy("testUser");

        account = new Account();
        account.setAccountId(1);
        account.setAccountNumber(123456L);
        account.setCustomerId(1);
        account.setBalance(5000.0);

        customer = new Customer();
        customer.setCustomerId(1);
        customer.setCustomerName("John Doe");
        customer.setEmail("john@example.com");
    }

    // success test cases
    @Test
    void testCreateTransaction_Success() throws SQLException {
        when(transactionDAO.createTransaction(transaction)).thenReturn(null);
        when(accountDAO.getAccount(transaction.getAccountNumber())).thenReturn(account);
        when(customerDAO.getCustomerById(account.getCustomerId())).thenReturn(customer);

        String result = service.createTransaction(transaction);

        logger.info("testCreateTransaction_Success executed");
        assertNull(result, "Transaction should succeed and return null");
        verify(transactionDAO, times(1)).createTransaction(transaction);
    }

    @Test
    void testGetTransaction_Success() throws SQLException {
        when(transactionDAO.getTransaction(1)).thenReturn(transaction);

        Transaction result = service.getTransaction(1);

        logger.info("testGetTransaction_Success executed");
        assertNotNull(result);
        assertEquals(1, result.getTransactionId());
        verify(transactionDAO, times(1)).getTransaction(1);
    }

    @Test
    void testGetTransactionsByAccountNumber_Success() throws SQLException {
        when(transactionDAO.getTransactionsByAccountNumber("123456")).thenReturn(List.of(transaction));

        List<Transaction> result = service.getTransactionsByAccountNumber("123456");

        logger.info("testGetTransactionsByAccountNumber_Success executed");
        assertEquals(1, result.size());
        verify(transactionDAO, times(1)).getTransactionsByAccountNumber("123456");
    }

    // Failure test cases
    @Test
    void testCreateTransaction_Failure_DAOError() throws SQLException {
        when(transactionDAO.createTransaction(transaction)).thenReturn("DB error occurred");

        String result = service.createTransaction(transaction);

        logger.warn("testCreateTransaction_Failure_DAOError executed");
        assertEquals("DB error occurred", result);
        verify(transactionDAO, times(1)).createTransaction(transaction);
    }

    @Test
    void testGetTransaction_Failure_NotFound() throws SQLException {
        when(transactionDAO.getTransaction(99)).thenReturn(null);

        Transaction result = service.getTransaction(99);

        logger.warn("testGetTransaction_Failure_NotFound executed");
        assertNull(result);
        verify(transactionDAO, times(1)).getTransaction(99);
    }

    @Test
    void testGetTransactionsByAccountNumber_Failure_Empty() throws SQLException {
        when(transactionDAO.getTransactionsByAccountNumber("999999")).thenReturn(List.of());

        List<Transaction> result = service.getTransactionsByAccountNumber("999999");

        logger.warn("testGetTransactionsByAccountNumber_Failure_Empty executed");
        assertTrue(result.isEmpty());
        verify(transactionDAO, times(1)).getTransactionsByAccountNumber("999999");
    }
}

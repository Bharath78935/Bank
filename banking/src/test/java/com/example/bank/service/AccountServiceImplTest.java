package com.example.bank.service;

import com.example.bank.DAO.AccountDAO;
import com.example.bank.model.Account;
import com.example.bank.service.impl.AccountServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AccountServiceImplTest {

    private static final Logger logger = LogManager.getLogger(AccountServiceImplTest.class);

    @Mock
    private AccountDAO accountDAO;

    private AccountServiceImpl service;
    private Account account;

    @BeforeEach
    void setup() {
        account = new Account();
        account.setAccountNumber(1234567890L);
        account.setBankName("Test Bank");
        account.setBalance(1000.0);

        service = new AccountServiceImpl(accountDAO);
    }

    // sucess testcases
    @Test
    void testCreateAccount_Success() throws Exception {
        doNothing().when(accountDAO).createAccount(any(Account.class));

        service.createAccount(account);

        verify(accountDAO, times(1)).createAccount(any(Account.class));
        logger.info("testCreateAccount_Success completed successfully");
    }

    @Test
    void testGetAccount_Success() throws Exception {
        when(accountDAO.getAccount(1234567890L)).thenReturn(account);

        Account result = service.getAccount(1234567890L);

        assertEquals("Test Bank", result.getBankName());
        verify(accountDAO, times(1)).getAccount(1234567890L);
        logger.info("testGetAccount_Success completed successfully with account: {}", result.getAccountNumber());
    }

    //Failure test cases
    @Test
    void testGetAccount_Failure_NotFound() throws Exception {
        when(accountDAO.getAccount(999999L)).thenReturn(null);

        Account result = service.getAccount(999999L);

        assertNull(result);
        verify(accountDAO, times(1)).getAccount(999999L);
        logger.warn("testGetAccount_Failure_NotFound completed; account not found as expected");
    }

    @Test
    void testCreateAccount_Failure_SQLException() throws Exception {
        doThrow(new SQLException("DB error")).when(accountDAO).createAccount(any(Account.class));

        assertDoesNotThrow(() -> service.createAccount(account));
        verify(accountDAO, times(1)).createAccount(any(Account.class));
        logger.warn("testCreateAccount_Failure_SQLException completed; SQLException handled gracefully");
    }
}

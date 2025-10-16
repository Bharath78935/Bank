package com.example.bank.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.example.bank.model.Account;
import com.example.bank.model.Customer;
import com.example.bank.service.AccountService;
import com.example.bank.service.CustomerService;
import com.example.bank.service.impl.AccountServiceImpl;
import com.example.bank.service.impl.CustomerServiceImpl;
import com.example.bank.util.ValidationUtil;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class AccountController {

    private static final Logger logger = LogManager.getLogger(AccountController.class);

    protected AccountService service;
    public AccountController() {
        this.service = new AccountServiceImpl();
    }
    public AccountController(AccountService service) {
        this.service = service;
    }
    public void setService(AccountService service) {
        this.service = service;
    }
    CustomerService customerService = new CustomerServiceImpl();

    @POST
    @Path("/create")
    public Response create(Account account) {
        if (account == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Account cannot be null").build();
        }
        Customer customer = customerService.getCustomerById(account.getCustomerId());
        String validationError = ValidationUtil.validateAccount(account,customer);
        if (validationError != null) {
            logger.warn("Validation failed: " + validationError);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(validationError).build();
        }
        if (service.getAccount(account.getAccountNumber()) != null) {
            logger.warn("An account with this account number already exists: " + account.getAccountNumber());
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("An Account already exists with this account number.").build();
        }
        service.createAccount(account);
        logger.info("Account created Successfully.");
        return Response.ok("Account created successfully.").build();
    }

    @GET
    @Path("/customer/{customerId}")
    public Response getByCustomerId(@PathParam("customerId") int customerId) {
        if (customerId <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Customer ID must be positive.").build();
        }

        Account account;
        try {
            account = service.getAccountByCustomerId(customerId);
        } catch (Exception e) {
            logger.error("Error fetching account for customerId: " + customerId, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error fetching account").build();
        }

        logger.info("Fetching Account with Customer ID: " + customerId);

        if (account == null) {
            logger.warn("No Account found with Customer ID: " + customerId);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No Account found for this customer").build();
        }

        return Response.ok(account).build();
    }


    @GET
    @Path("/{accountNumber}")
    public Response get(@PathParam("accountNumber") long accountNumber) {
        if (accountNumber <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Account number must be positive.").build();
        }
        Account account = service.getAccount(accountNumber);
        logger.info("Fetching Account with Account number: " + accountNumber);
        if (account == null) {
            logger.warn("No Account found with account number:" + accountNumber);
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("No Account found with this account number").build();
        }
        return Response.ok(account).build();
    }

    @PUT
    @Path("/update")
    public Response update(Account account) {
        logger.info("Received request to update account details: " + account);
        if (account == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Account cannot be null").build();
        }
        Customer customer = customerService.getCustomerById(account.getCustomerId());
        String validationError = ValidationUtil.validateAccount(account,customer);
        if (validationError != null) {
            logger.warn("Validation failed: " + validationError);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(validationError).build();
        }
        service.updateAccount(account);
        logger.info("Account updated succesfully.");
        return Response.ok("Account updated successfully").build();
    }

    @DELETE
@Path("/{accountId}")
public Response delete(@PathParam("accountId") int accountId) {
    if (accountId <= 0) {
        return Response.status(Response.Status.BAD_REQUEST)
                .entity("Account ID must be positive.").build();
    }

    // Fetch account by primary key 'account_id' now
    Account account = service.getAccountById(accountId);
    if (account == null) {
        logger.warn("No Account found with account ID: " + accountId);
        return Response.status(Response.Status.NOT_FOUND)
                .entity("No Account found with this account ID").build();
    }

    service.deleteAccount(accountId);
    logger.info("Account deleted with account ID: " + accountId);
    return Response.ok("Account deleted successfully!").build();
}

}

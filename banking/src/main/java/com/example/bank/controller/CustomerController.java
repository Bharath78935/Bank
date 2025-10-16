package com.example.bank.controller;

import com.example.bank.model.Customer;
import com.example.bank.service.CustomerService;
import com.example.bank.service.impl.CustomerServiceImpl;
import com.example.bank.util.PasswordUtil;
import com.example.bank.util.ValidationUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;

@Path("/customer")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CustomerController {

    private static final Logger logger = LogManager.getLogger(CustomerController.class);
    protected CustomerService service;

    public CustomerController() {
        this.service = new CustomerServiceImpl();
    }

    public CustomerController(CustomerService service) {
        this.service = service;
    }

    public void setService(CustomerService service) {
        this.service = service;
    }

    @GET
    @Path("/username/{username}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getCustomerByUsername(@PathParam("username") String username) {
        try {
            Customer customer = service.getCustomerByUsername(username);
            if (customer == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("Customer not found").build();
            }
            return Response.ok(customer).build();
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("Error fetching customer").build();
        }
    }

   @POST
    @Path("/login")
    public Response login(Customer customer) {
        logger.info("Login attempt for username: " + customer.getUsername());

        // Validate input
        if (customer.getUsername() == null || customer.getUsername().isEmpty() ||
            customer.getPassword() == null || customer.getPassword().isEmpty()) {
            logger.warn("Username or password is empty");
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Username and password are required").build();
        }

        try {
            // Fetch user by username
            Customer existingCustomer = service.getCustomerByUsername(customer.getUsername());
            if (existingCustomer == null) {
                logger.warn("No customer found with username: " + customer.getUsername());
                return Response.status(Response.Status.UNAUTHORIZED)
                        .entity("Invalid username").build();
            }

            // Check password (hash incoming password before comparing)
            //String hashedInputPassword = PasswordUtil.hash(customer.getPassword());
            if (!PasswordUtil.verify(customer.getPassword(), existingCustomer.getPassword())) {
                    return Response.status(Response.Status.UNAUTHORIZED)
                   .entity("Invalid username or password").build();
}


            logger.info("Login successful for username: " + customer.getUsername());
            return Response.ok("Login successful!").build();
        } catch (Exception e) {
            logger.error("Error during login", e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("An error occurred during login").build();
        }
    }

    @POST
    @Path("/create")
    public Response create(Customer customer) {
        logger.info("Received request to create customer: " + customer);
        String validationError = ValidationUtil.validateCustomer(customer);
        if (validationError != null) {
            logger.warn("Validation failed: " + validationError);
            return Response.status(Response.Status.BAD_REQUEST).entity(validationError).build();
        }

        if (service.getCustomerByEmail(customer.getEmail()) != null) {
            logger.warn("Customer with this email already exists: " + customer.getEmail());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Customer with this email already exists.").build();
        }

        if(service.getCustomerByAadhar(customer.getAadharNumber()) != null){
            logger.warn("Customer with this Aadhar already exists:" + customer.getAadharNumber());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Customer with this Aadhar already exists.").build();
        }

        if (service.getCustomerByUsername(customer.getUsername()) != null) {
            logger.warn("Customer with this username already exists: " + customer.getUsername());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Customer with this username already exists.").build();
        }
        service.createCustomer(customer);
        logger.info("Customer created successfully: " + customer.getEmail());
        return Response.ok("Customer created successfully!").build();
    }

    @GET
    @Path("/{customerId}")
    public Response get(@PathParam("customerId") int customerId) {
        logger.info("Fetching customer with ID: " + customerId);
        if (customerId <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Customer ID must be positive.").build();
        }

        Customer customer = service.getCustomerById(customerId);
        if (customer == null) {
            logger.warn("Customer not found with ID: " + customerId);
            return Response.status(Response.Status.NOT_FOUND).entity("Customer not found").build();
        }

        return Response.ok(customer).build();
    }

    @GET
    @Path("/all")
    public Response getAll() {
        logger.info("Fetching all customers");
        List<Customer> customers = service.getAllCustomers();
        return Response.ok(customers).build();
    }

    @PUT
    @Path("/update")
    public Response update(Customer customer) {
        logger.info("Received request to update customer: " + customer);
        String validationError = ValidationUtil.validateCustomer(customer);
        if (validationError != null) {
            logger.warn("Validation failed: " + validationError);
            return Response.status(Response.Status.BAD_REQUEST).entity(validationError).build();
        }

    Customer existingEmail = service.getCustomerByEmail(customer.getEmail());
    if (existingEmail != null && existingEmail.getCustomerId() != customer.getCustomerId()) {
        return Response.status(Response.Status.BAD_REQUEST)
            .entity("Customer with this email already exists.").build();
    }

    Customer existingAadhar = service.getCustomerByAadhar(customer.getAadharNumber());
    if (existingAadhar != null && existingAadhar.getCustomerId() != customer.getCustomerId()) {
        return Response.status(Response.Status.BAD_REQUEST)
            .entity("Customer with this Aadhar already exists.").build();
    }

    Customer existingUsername = service.getCustomerByUsername(customer.getUsername());
    if (existingUsername != null && existingUsername.getCustomerId() != customer.getCustomerId()) {
        return Response.status(Response.Status.BAD_REQUEST)
            .entity("Customer with this username already exists.").build();
    }

        service.updateCustomer(customer);
        logger.info("Customer updated successfully: " + customer.getEmail());
        return Response.ok("Customer updated successfully!").build();
    }

    @DELETE
    @Path("/{customerId}")
    public Response delete(@PathParam("customerId") int customerId) {
        logger.info("Received request to delete customer with ID: " + customerId);
        if (customerId <= 0) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Customer ID must be positive.").build();
        }

        service.deleteCustomer(customerId);
        logger.info("Customer deleted successfully with ID: " + customerId);
        return Response.ok("Customer deleted successfully!").build();
    }
}

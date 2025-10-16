package com.example.bank;

import jakarta.ws.rs.ApplicationPath;
import org.glassfish.jersey.server.ResourceConfig;
import com.example.bank.filter.CORSFilter; // import your CORS filter

@ApplicationPath("/api")
public class ApplicationConfig extends ResourceConfig {
    public ApplicationConfig() {
        // Scan your controller package
        packages("com.example.bank.controller");

        // Register CORS filter
        register(CORSFilter.class);
    }
}

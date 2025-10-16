package com.example.bank.model;

import java.sql.Timestamp;

public class Customer {
    private int customerId;
    private String customerName;
    private String username;
    private String password;
    private int pin; 
    private long aadharNumber; 
    private String permanentAddress;
    private String state;
    private String country;
    private String city;
    private String email;
    private long phoneNumber;
    private String status;
    private String dob;
    private int age;
    private Timestamp createdOn;
    private Timestamp modifiedOn;
    private String gender;
    private String fatherName;
    private String motherName;

    // Getters and Setters
    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public int getPin() {
        return pin;
    }
    public void setPin(int pin) {
        this.pin = pin;
    }

    public long getAadharNumber() { return aadharNumber; }
    public void setAadharNumber(long aadharNumber) { this.aadharNumber = aadharNumber; }

    public String getPermanentAddress() { return permanentAddress; }
    public void setPermanentAddress(String permanentAddress) { this.permanentAddress = permanentAddress; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public String getCountry() { return country; }
    public void setCountry(String country) { this.country = country; }

    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public long getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(long phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDob() { return dob; }
    public void setDob(String dob) { this.dob = dob; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public Timestamp getCreatedOn() { return createdOn; }
    public void setCreatedOn(Timestamp createdOn) { this.createdOn = createdOn; }

    public Timestamp getModifiedOn() { return modifiedOn; }
    public void setModifiedOn(Timestamp modifiedOn) { this.modifiedOn = modifiedOn; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }

    public String getFatherName() { return fatherName; }
    public void setFatherName(String fatherName) { this.fatherName = fatherName; }

    public String getMotherName() { return motherName; }
    public void setMotherName(String motherName) { this.motherName = motherName; }
}

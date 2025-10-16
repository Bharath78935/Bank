package com.example.bank.model;

import java.sql.Timestamp;

public class Account {
    private int accountId;
    private int customerId;
    private long accountNumber;
    private String accountType;
    private String bankName;
    private String branch;
    private double balance;
    private String status;
    private Timestamp createdAt;
    private Timestamp modifiedAt;
    private String ifscCode;
    private String nameOnAccount;
    private long phoneLinked;
    private double savingAmount;

    public int getAccountId() { 
    return accountId; 
}

public void setAccountId(int accountId) { 
    this.accountId = accountId;
}

public int getCustomerId() { 
    return customerId; 
}

public void setCustomerId(int customerId) { 
    this.customerId = customerId; 
}

public String getAccountType() { 
    return accountType; 
}

public void setAccountType(String accountType) { 
    this.accountType = accountType; 
}

public String getBankName() { 
    return bankName; 
}

public void setBankName(String bankName) { 
    this.bankName = bankName; 
}

public String getBranch() { 
    return branch; 
}

public void setBranch(String branch) { 
    this.branch = branch; 
}

public double getBalance() { 
    return balance; 
}

public void setBalance(double balance) { 
    this.balance = balance; 
}

public String getStatus() { 
    return status; 
}

public void setStatus(String status) { 
    this.status = status; 
}

public Timestamp getCreatedAt() { 
    return createdAt; 
}

public void setCreatedAt(Timestamp createdAt) { 
    this.createdAt = createdAt; 
}

public Timestamp getModifiedAt() { 
    return modifiedAt; 
}

public void setModifiedAt(Timestamp modifiedAt) { 
    this.modifiedAt = modifiedAt; 
}

public long getAccountNumber() { 
    return accountNumber; 
}

public void setAccountNumber(long accountNumber) { 
    this.accountNumber = accountNumber; 
}

public String getIfscCode() { 
    return ifscCode; 
}

public void setIfscCode(String ifscCode) { 
    this.ifscCode = ifscCode; 
}

public String getNameOnAccount() { 
    return nameOnAccount; 
}

public void setNameOnAccount(String nameOnAccount) { 
    this.nameOnAccount = nameOnAccount; 
}

public long getPhoneLinked() { 
    return phoneLinked; 
}

public void setPhoneLinked(long phoneLinked) { 
    this.phoneLinked = phoneLinked; 
}

public double getSavingAmount() { 
    return savingAmount; 
}

public void setSavingAmount(double savingAmount) { 
    this.savingAmount = savingAmount; 
}

}

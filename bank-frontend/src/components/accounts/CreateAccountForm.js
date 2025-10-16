import React, { useState } from "react";
import { createAccountAPI } from "../../api/AccountAPI";

export default function CreateAccountForm({ customer, onAccountCreated, onCancel }) {
  const [formData, setFormData] = useState({
    accountNumber: "",
    accountType: "",
    bankName: "",
    branch: "",
    balance: 0,
    status: "ACTIVE",
    ifscCode: "",
    savingAmount: 0,
  });
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: name === "balance" || name === "savingAmount" ? Number(value) : value
    }));
    setErrors(prev => ({ ...prev, [name]: "" }));
  };

  const validate = () => {
    const newErrors = {};
    if (!formData.accountNumber) newErrors.accountNumber = "Account number is required.";
    if (!formData.accountType) newErrors.accountType = "Account type is required.";
    if (!formData.bankName) newErrors.bankName = "Bank name is required.";
    if (formData.balance < 0) newErrors.balance = "Balance cannot be negative.";
    return newErrors;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrors({});

    const validationErrors = validate();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    if (!customer || !customer.customerId) {
      setErrors({ general: "Customer information is missing!" });
      return;
    }

    const accountToCreate = {
      accountNumber: formData.accountNumber,
      accountType: formData.accountType,
      bankName: formData.bankName,
      branch: formData.branch,
      balance: formData.balance,
      status: formData.status,
      ifscCode: formData.ifscCode,
      savingAmount: formData.savingAmount,
      customerId: customer.customerId,
      nameOnAccount: customer.name,
      phoneLinked: customer.phone
    };

    setLoading(true);
    try {
      const newAccount = await createAccountAPI(accountToCreate);
      onAccountCreated(newAccount);
    } catch (err) {
  console.error("Error creating account:", err);
  const response = err.response?.data;

  if (Array.isArray(response)) {
    // Handle validation errors array
    const fieldErrors = {};
    response.forEach(e => {
      fieldErrors[e.field] = e.message;
    });
    setErrors(fieldErrors);
  } else if (response?.field && response?.message) {
    setErrors({ [response.field]: response.message });
  } else if (typeof response === 'string' && 
              response.includes("An Account already exists with this account number")) {
    setErrors({ accountNumber: response }); // show message below account number field
  } else {
    setErrors({ general: "Account creation failed" });
  }
}

  };

  return (
    <div className="form-container">
      <div className="form-card">
        <div className="form-header">
          <div className="form-header-icon">🏦</div>
          <h2>Create Your Banking Account</h2>
          <p>Fill in the details below to open a new account with us</p>
        </div>

        {errors.general && (
          <div className="alert alert-error">
            <span className="alert-icon">⚠️</span>
            <span>{errors.general}</span>
          </div>
        )}

        <form className="account-form" onSubmit={handleSubmit}>
          <div className="form-row">
            <div className="form-group">
              <label htmlFor="accountNumber">
                <span className="label-icon">🔢</span> Account Number *
              </label>
              <input
                type="text"
                id="accountNumber"
                name="accountNumber"
                placeholder="Enter account number"
                value={formData.accountNumber}
                onChange={handleChange}
              />
              {errors.accountNumber && <span className="field-error">{errors.accountNumber}</span>}
            </div>

            <div className="form-group">
              <label htmlFor="accountType">
                <span className="label-icon">📁</span> Account Type *
              </label>
              <select
                id="accountType"
                name="accountType"
                value={formData.accountType}
                onChange={handleChange}
              >
                <option value="">Select account type</option>
                <option value="SAVINGS">Savings</option>
                <option value="CURRENT">Current</option>
                <option value="SALARY">Salary</option>
              </select>
              {errors.accountType && <span className="field-error">{errors.accountType}</span>}
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="bankName">
                <span className="label-icon">🏛️</span> Bank Name *
              </label>
              <input
                type="text"
                id="bankName"
                name="bankName"
                placeholder="Enter bank name"
                value={formData.bankName}
                onChange={handleChange}
              />
              {errors.bankName && <span className="field-error">{errors.bankName}</span>}
            </div>

            <div className="form-group">
              <label htmlFor="branch">
                <span className="label-icon">📍</span> Branch
              </label>
              <input
                type="text"
                id="branch"
                name="branch"
                placeholder="Enter branch name"
                value={formData.branch}
                onChange={handleChange}
              />
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="ifscCode">
                <span className="label-icon">🔐</span> IFSC Code
              </label>
              <input
                type="text"
                id="ifscCode"
                name="ifscCode"
                placeholder="Enter IFSC code (e.g., SBIN0001234)"
                value={formData.ifscCode}
                onChange={handleChange}
                pattern="[A-Z]{4}0[A-Z0-9]{6}"
                title="IFSC code format: 4 letters, 0, then 6 alphanumeric characters"
              />
            </div>

            <div className="form-group">
              <label htmlFor="balance">
                <span className="label-icon">💰</span> Initial Balance
              </label>
              <input
                type="number"
                id="balance"
                name="balance"
                placeholder="Enter initial balance"
                value={formData.balance}
                onChange={handleChange}
                min="0"
                step="0.01"
              />
              {errors.balance && <span className="field-error">{errors.balance}</span>}
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="savingAmount">
                <span className="label-icon">🏦</span> Saving Amount
              </label>
              <input
                type="number"
                id="savingAmount"
                name="savingAmount"
                placeholder="Enter saving amount"
                value={formData.savingAmount}
                onChange={handleChange}
                min="0"
                step="0.01"
              />
              {errors.savingAmount && <span className="field-error">{errors.savingAmount}</span>}
            </div>
          </div>

          <div className="form-actions">
            <button type="submit" className="btn btn-primary btn-large" disabled={loading}>
              <span className="btn-icon">✓</span>
              {loading ? "Creating..." : "Create Account"}
            </button>
            <button type="button" className="btn btn-secondary btn-large" onClick={onCancel}>
              <span className="btn-icon">✗</span>
              Cancel
            </button>
          </div>
        </form>
      </div>
    </div>
  );
}

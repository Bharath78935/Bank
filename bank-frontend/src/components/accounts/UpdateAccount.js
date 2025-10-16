import React, { useState, useEffect } from "react";
import { updateAccountAPI } from "../../api/AccountAPI";

export default function EditAccountForm({ account, customer, onAccountUpdated, onCancel }) {
  const [formData, setFormData] = useState({
    account_number: "",
    account_type: "",
    bank_name: "",
    branch: "",
    balance: 0,
    status: "ACTIVE",
    ifsc_code: "",
    saving_amount: 0,
  });
  const [errors, setErrors] = useState({});
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (account) {
      setFormData({
        account_number: account.account_number || account.accountNumber || "",
        account_type: account.account_type || account.accountType || "",
        bank_name: account.bank_name || account.bankName || "",
        branch: account.branch || "",
        balance: account.balance || 0,
        status: account.status || "ACTIVE",
        ifsc_code: account.ifsc_code || account.ifscCode || "",
        saving_amount: account.saving_amount || account.savingAmount || 0,
      });
    }
  }, [account]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: name === "balance" || name === "saving_amount" ? Number(value) : value
    }));
    setErrors(prev => ({ ...prev, [name]: "" }));
  };

  const validate = () => {
    const newErrors = {};
    if (!formData.account_number) newErrors.account_number = "Account number is required.";
    if (!formData.account_type) newErrors.account_type = "Account type is required.";
    if (!formData.bank_name) newErrors.bank_name = "Bank name is required.";
    return newErrors;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrors({});

    if (!account?.account_id && !account?.accountId) {
      setErrors({ general: "Account ID is missing!" });
      return;
    }

    const validationErrors = validate();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    const accountToUpdate = {
      ...formData,
      account_id: account.account_id || account.accountId,
      customer_id: customer.customerId,
      name_on_account: customer.name,
      phone_linked: customer.phone
    };

    setLoading(true);
    try {
      const updatedAccount = await updateAccountAPI(accountToUpdate);
      onAccountUpdated(updatedAccount);
    } catch (err) {
      console.error("Error updating account:", err);
      setErrors({ general: err.response?.data?.message || "Update failed" });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="form-container">
      <div className="form-card">
        <div className="form-header">
          <h2>Edit Account Information</h2>
          <p>Update your account details below</p>
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
              <label htmlFor="account_number">
                <span className="label-icon">🔢</span>
                Account Number *
              </label>
              <input
                type="text"
                id="account_number"
                name="account_number"
                placeholder="Enter account number"
                value={formData.account_number}
                onChange={handleChange}
                required
                disabled
                className="input-disabled"
              />
              {errors.account_number && <span className="field-error">{errors.account_number}</span>}
            </div>

            <div className="form-group">
              <label htmlFor="account_type">
                <span className="label-icon">📁</span>
                Account Type *
              </label>
              <select
                id="account_type"
                name="account_type"
                value={formData.account_type}
                onChange={handleChange}
                required
              >
                <option value="">Select account type</option>
                <option value="SAVINGS">Savings</option>
                <option value="CURRENT">Current</option>
                <option value="SALARY">Salary</option>
              </select>
              {errors.account_type && <span className="field-error">{errors.account_type}</span>}
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="bank_name">
                <span className="label-icon">🏛️</span>
                Bank Name *
              </label>
              <input
                type="text"
                id="bank_name"
                name="bank_name"
                placeholder="Enter bank name"
                value={formData.bank_name}
                onChange={handleChange}
                required
              />
              {errors.bank_name && <span className="field-error">{errors.bank_name}</span>}
            </div>

            <div className="form-group">
              <label htmlFor="branch">
                <span className="label-icon">📍</span>
                Branch
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
              <label htmlFor="ifsc_code">
                <span className="label-icon">🔐</span>
                IFSC Code
              </label>
              <input
                type="text"
                id="ifsc_code"
                name="ifsc_code"
                placeholder="Enter IFSC code (e.g., SBIN0001234)"
                value={formData.ifsc_code}
                onChange={handleChange}
                pattern="[A-Z]{4}0[A-Z0-9]{6}"
                title="IFSC code format: 4 letters, 0, then 6 alphanumeric characters"
              />
            </div>

            <div className="form-group">
              <label htmlFor="status">
                <span className="label-icon">✓</span>
                Status
              </label>
              <select
                id="status"
                name="status"
                value={formData.status}
                onChange={handleChange}
              >
                <option value="ACTIVE">Active</option>
                <option value="INACTIVE">Inactive</option>
                <option value="SUSPENDED">Suspended</option>
                <option value="CLOSED">Closed</option>
              </select>
            </div>
          </div>

          <div className="form-row">
            <div className="form-group">
              <label htmlFor="balance">
                <span className="label-icon">💰</span>
                Balance
              </label>
              <input
                type="number"
                id="balance"
                name="balance"
                placeholder="Enter balance"
                value={formData.balance}
                onChange={handleChange}
                min="0"
                step="0.01"
              />
            </div>

            <div className="form-group">
              <label htmlFor="saving_amount">
                <span className="label-icon">🏦</span>
                Saving Amount
              </label>
              <input
                type="number"
                id="saving_amount"
                name="saving_amount"
                placeholder="Enter saving amount"
                value={formData.saving_amount}
                onChange={handleChange}
                min="0"
                step="0.01"
              />
            </div>
          </div>

          <div className="form-actions">
            <button type="submit" className="btn btn-primary btn-large" disabled={loading}>
              <span className="btn-icon">💾</span>
              {loading ? "Updating..." : "Update Account"}
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

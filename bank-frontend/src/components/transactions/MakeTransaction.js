import React, { useState } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import Alert from "../common/Alert";
import { createTransaction } from "../../api/TransactionAPI";
import { getAccountByAccountNumber } from "../../api/AccountAPI";
import "../styles/ManageAccounts.css";
import "../styles/Transaction.css";

export default function MakeTransaction() {
  const navigate = useNavigate();
  const { account, customer } = useLocation().state || {};

  const [formData, setFormData] = useState({
    transactionType: "",
    transactionAmount: "",
    receiverAccount: "",
    description: "",
    mode: "",
    bankBranch: "",
  });

  const [errors, setErrors] = useState({});
  const [success, setSuccess] = useState("");
  const [loading, setLoading] = useState(false);
  const [checkingReceiver, setCheckingReceiver] = useState(false);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData(prev => ({
      ...prev,
      [name]: name === "transactionAmount" ? Number(value) : value,
    }));
    setErrors(prev => ({ ...prev, [name]: "" }));
    setSuccess("");
  };

  // Frontend validation with receiver account check
  const validate = async () => {
    const newErrors = {};
    if (!formData.transactionType) newErrors.transactionType = "Transaction type is required.";
    if (!formData.mode) newErrors.mode = "Transaction mode is required.";
    if (!formData.bankBranch) newErrors.bankBranch = "Bank branch is required.";
    if (!formData.transactionAmount || formData.transactionAmount <= 0)
      newErrors.transactionAmount = "Transaction amount must be greater than zero.";
    if (account && formData.transactionAmount > account.balance)
      newErrors.transactionAmount = "Insufficient balance.";
    
    if (formData.transactionType === "TRANSFER") {
      if (!formData.receiverAccount) newErrors.receiverAccount = "Receiver account is required.";
      if (formData.receiverAccount === String(account.accountNumber))
        newErrors.receiverAccount = "Sender and receiver accounts cannot be the same.";

      if (formData.receiverAccount) {
        setCheckingReceiver(true);
        try {
          const resp = await getAccountByAccountNumber(formData.receiverAccount);
          if (!resp) newErrors.receiverAccount = `Receiver account ${formData.receiverAccount} not found.`;
        } catch {
          newErrors.receiverAccount = `Receiver account ${formData.receiverAccount} not found.`;
        } finally {
          setCheckingReceiver(false);
        }
      }
    }
    return newErrors;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrors({});
    setSuccess("");

    const validationErrors = await validate();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    setLoading(true);
    try {
      const transactionData = {
        accountNumber: account.accountNumber,
        transactionAmount: formData.transactionAmount,
        description: formData.description,
        modifiedBy: customer?.name || "System",
        receiverAccount: formData.receiverAccount || 0,
        transactionType: formData.transactionType,
        mode: formData.mode,
        bankBranch: formData.bankBranch,
      };

      await createTransaction(transactionData);

      setSuccess("Transaction completed successfully!");
      setFormData({
        transactionType: "",
        transactionAmount: "",
        receiverAccount: "",
        description: "",
        mode: "",
        bankBranch: "",
      });
    } catch (err) {
      setErrors({ general: err.response?.data?.message || "Transaction failed" });
    } finally {
      setLoading(false);
    }
  };

  if (!account || !customer) {
    return (
      <div className="content-area" style={{ maxWidth: 600, margin: "2rem auto" }}>
        <h2>No Account Found</h2>
        <p>Please go back to the dashboard and try again.</p>
        <button className="btn btn-primary" onClick={() => navigate("/dashboard")}>
          Go to Dashboard
        </button>
      </div>
    );
  }

  return (
    <div className="dashboard-wrapper" style={{ justifyContent: "center", padding: "2rem" }}>
      <main className="main-content" style={{ maxWidth: 500 }}>
        <header className="topbar" style={{ marginBottom: "1.5rem" }}>
          <h1 className="page-title" style={{ textAlign: "center" }}>Make Transaction</h1>
        </header>

        <div className="content-area">
          {errors.general && <Alert type="error" message={errors.general} />}
          {success && <Alert type="success" message={success} />}

          <form className="account-form" onSubmit={handleSubmit}>
            {/* Transaction Type */}
            <div className="floating-group select-group">
              <select
                name="transactionType"
                value={formData.transactionType}
                onChange={handleChange}
                className={formData.transactionType ? "not-empty" : ""}
                required
              >
                <option value="" disabled hidden></option>
                <option value="DEPOSIT">Deposit</option>
                <option value="WITHDRAWAL">Withdrawal</option>
                <option value="TRANSFER">Transfer</option>
                <option value="CREDIT">Credit</option>
              </select>
              <label>Transaction Type *</label>
              {errors.transactionType && <div className="field-error">{errors.transactionType}</div>}
            </div>

            {/* Amount */}
            <div className="form-group floating">
              <input
                type="number"
                name="transactionAmount"
                value={formData.transactionAmount}
                onChange={handleChange}
                min="0.01"
                step="0.01"
                placeholder=" "
                required
              />
              <label>Amount *</label>
              {errors.transactionAmount && <div className="field-error">{errors.transactionAmount}</div>}
            </div>

            {/* Receiver Account */}
            {formData.transactionType === "TRANSFER" && (
              <div className="form-group floating">
                <input
                  type="text"
                  name="receiverAccount"
                  value={formData.receiverAccount}
                  onChange={handleChange}
                  placeholder=" "
                  required
                />
                <label>Recipient Account *</label>
                {checkingReceiver && <div className="field-info">Checking account...</div>}
                {errors.receiverAccount && <div className="field-error">{errors.receiverAccount}</div>}
              </div>
            )}

            {/* Mode */}
            <div className="floating-group select-group">
              <select
                name="mode"
                value={formData.mode}
                onChange={handleChange}
                className={formData.mode ? "not-empty" : ""}
                required
              >
                <option value="" disabled hidden></option>
                <option value="UPI">UPI</option>
                <option value="QRCODE">QR Code</option>
                <option value="CASH">Cash</option>
              </select>
              <label>Transaction Mode *</label>
              {errors.mode && <div className="field-error">{errors.mode}</div>}
            </div>

            {/* Branch */}
            <div className="form-group floating">
              <input
                type="text"
                name="bankBranch"
                value={formData.bankBranch}
                onChange={handleChange}
                placeholder=" "
                required
              />
              <label>Bank Branch *</label>
              {errors.bankBranch && <div className="field-error">{errors.bankBranch}</div>}
            </div>

            {/* Description */}
            <div className="form-group floating">
              <textarea
                name="description"
                rows={3}
                value={formData.description}
                onChange={handleChange}
                placeholder=" "
              />
              <label>Description</label>
            </div>

            <div className="form-actions">
              <button
                type="submit"
                disabled={loading || checkingReceiver}
                className="btn btn-primary"
              >
                {loading ? "Processing..." : "Complete Transaction"}
              </button>
              <button
                type="button"
                className="btn btn-secondary"
                onClick={() => navigate(-1)}
              >
                Cancel
              </button>
            </div>
          </form>
        </div>
      </main>
    </div>
  );
}

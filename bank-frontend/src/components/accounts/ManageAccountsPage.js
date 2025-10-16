import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { getCustomerByUsername } from "../../api/CustomerAPI";
import { getAccountByCustomerId, deleteAccountAPI } from "../../api/AccountAPI";
import EditAccountForm from "./UpdateAccount";
import CreateAccountForm from "./CreateAccountForm";
import Alert from "../common/Alert";
import Loading from "../common/Loading";
import "../styles/ManageAccounts.css";

export default function ManageAccountsPage() {
  const navigate = useNavigate();
  const [account, setAccount] = useState(null);
  const [customer, setCustomer] = useState(null);
  const [loading, setLoading] = useState(true);
  const [errors, setErrors] = useState({});
  const [success, setSuccess] = useState("");
  const [isEditing, setIsEditing] = useState(false);

  useEffect(() => {
    fetchCustomerAndAccount();
  }, []);

  const fetchCustomerAndAccount = async () => {
    setLoading(true);
    setErrors({});
    try {
      const username = localStorage.getItem("username");
      if (!username) {
        setErrors({ general: "No logged-in user found. Please login again." });
        setLoading(false);
        return;
      }
      const customerData = await getCustomerByUsername(username);
      if (!customerData || !customerData.customerId) {
        setErrors({ general: "Customer data not found." });
        setLoading(false);
        return;
      }
      setCustomer(customerData);
      const accountData = await getAccountByCustomerId(customerData.customerId);
      setAccount(accountData || null);
    } catch (err) {
      setErrors({ general: "Failed to fetch account information." });
    } finally {
      setLoading(false);
    }
  };

  const handleAccountCreated = (newAccount) => {
    setAccount(newAccount);
    setSuccess("Account created successfully!");
    setTimeout(() => setSuccess(""), 5000);
  };

  const handleAccountUpdated = (updatedAccount) => {
    setAccount(updatedAccount);
    setSuccess("Account updated successfully!");
    setIsEditing(false);
    setTimeout(() => setSuccess(""), 5000);
  };

  const handleEditToggle = () => {
    setIsEditing(!isEditing);
    setErrors({});
    setSuccess("");
  };

  const handleGetDetails = () => {
    setIsEditing(false);
  };

  const handleDeleteAccount = async () => {
    if (!account) return;
    if (!window.confirm("Are you sure you want to delete your account? This action cannot be undone.")) return;
    try {
      await deleteAccountAPI(account.accountId);
      setAccount(null);
      setSuccess("Account deleted successfully!");
      setTimeout(() => setSuccess(""), 5000);
    } catch (err) {
      setErrors({ general: "Failed to delete account." });
    }
  };

  const handleMakeTransaction = () => {
    navigate("/transactions", { state: { account, customer } });
  };

  const handleViewTransactions = () => {
    navigate("/view-transactions", { state: { account, customer } });
  };

  const handleBackToDashboard = () => {
    navigate("/dashboard");
  };

  if (loading) return <Loading />;

  return (
    <div className="dashboard-wrapper" style={{ justifyContent: "center", padding: "2rem" }}>
      <main className="main-content" style={{ maxWidth: 600, width: "100%" }}>
        <header className="topbar" style={{ marginBottom: "1.5rem" }}>
          <h1 className="page-title" style={{ textAlign: "center" }}>
            Manage Account
          </h1>
        </header>

        {errors.general && <Alert type="error" message={errors.general} />}
        {success && <Alert type="success" message={success} />}
        
        <div style={{ marginBottom: "1.5rem", textAlign: "center" }}>
          <button onClick={handleGetDetails} className="btn btn-secondary" style={{ marginRight: "0.75rem" }}>
            Account Profile
          </button>
          <button onClick={handleEditToggle} className="btn btn-primary">
            {isEditing ? "Cancel Update" : "Update Account Details"}
          </button>
        </div>

        {account ? (
          isEditing ? (
            <EditAccountForm
              account={account}
              customer={customer}
              onAccountUpdated={handleAccountUpdated}
              onCancel={handleEditToggle}
            />
          ) : (
            <>
              <div
                style={{
                  background: "#fff",
                  borderRadius: 12,
                  padding: "1.5rem 2rem",
                  boxShadow: "0 4px 12px rgba(0,0,0,0.08)",
                  fontSize: "1rem",
                  color: "#1f2937",
                  lineHeight: 1.8,
                  marginBottom: "1rem"
                }}
              >
                <p><strong>Account Number:</strong> {account.accountNumber}</p>
                <p><strong>Account Type:</strong> {account.accountType}</p>
                <p><strong>Bank Name:</strong> {account.bankName}</p>
                <p><strong>Branch:</strong> {account.branch}</p>
                <p><strong>Status:</strong> {account.status}</p>
                <p><strong>IFSC Code:</strong> {account.ifscCode}</p>
                <p><strong>Name on Account:</strong> {account.nameOnAccount}</p>
                <p><strong>Phone Linked:</strong> {account.phoneLinked}</p>
                <p><strong>Balance:</strong> ₹{account.balance?.toLocaleString("en-IN")}</p>
                <p><strong>Saving Amount:</strong> ₹{account.savingAmount?.toLocaleString("en-IN")}</p>
                <p><strong>Created At:</strong> {new Date(account.createdAt).toLocaleString()}</p>
                <p><strong>Last Modified:</strong> {new Date(account.modifiedAt).toLocaleString()}</p>
              </div>
              <div style={{ display: "flex", justifyContent: "center", gap: "1rem", flexWrap: "wrap" }}>
                <button className="btn btn-primary" onClick={handleMakeTransaction}>
                  Make Transaction
                </button>
                <button className="btn btn-secondary" onClick={handleViewTransactions}>
                  Transaction History
                </button>
                <button className="btn btn-danger" onClick={handleDeleteAccount}>
                  Delete Account
                </button>
              </div>
            </>
          )
        ) : (
          <div style={{ textAlign: "center" }}>
            <p>No account found for this customer.</p>
            <button className="btn btn-primary" onClick={() => setIsEditing(true)}>
              Create New Account
            </button>
            {isEditing && (
              <CreateAccountForm
                customer={customer}
                onAccountCreated={handleAccountCreated}
                onCancel={() => setIsEditing(false)}
              />
            )}
          </div>
        )}

        <div style={{ marginTop: 24, textAlign: "center" }}>
          <button className="btn btn-secondary" onClick={handleBackToDashboard}>
            Back to Customer Dashboard
          </button>
        </div>
      </main>
    </div>
  );
}

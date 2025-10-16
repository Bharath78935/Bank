import React from "react";
import { deleteAccountAPI } from "../../api/AccountAPI";

export default function AccountDashboard({
  account,
  customer,
  onEdit,
  onDelete,
  onMakeTransaction,
  onViewTransactions,
}) {
  const getAccountField = (field1, field2) => {
    if (!account) return "N/A";
    const value = account[field1] || account[field2];
    return value || "N/A";
  };

  const handleDelete = async () => {
  const accountId = account?.account_id;

  if (!accountId) {
    console.error("Account ID missing!", account);
    alert("Cannot delete: account ID missing.");
    return;
  }

  if (!window.confirm("⚠️ Are you sure you want to delete this account?")) return;

  try {
    console.log("Deleting account with account ID:", accountId);
    await deleteAccountAPI(accountId);  // Pass account_id
    onDelete();  // Callback to refresh list
  } catch (err) {
    console.error("Error deleting account:", err.response?.data || err.message);
    alert("Failed to delete account: " + (err.response?.data || err.message));
  }
};


  return (
    <div className="account-dashboard">
      {/* Account Summary Cards */}
      <div className="summary-cards">
        <div className="summary-card card-balance">
          <div className="card-icon">💰</div>
          <div className="card-content">
            <p className="card-label">Current Balance</p>
            <h2 className="card-value">
              ₹{(account.balance || 0).toLocaleString('en-IN', { 
                minimumFractionDigits: 2, 
                maximumFractionDigits: 2 
              })}
            </h2>
          </div>
        </div>

        <div className="summary-card card-savings">
          <div className="card-icon">🏦</div>
          <div className="card-content">
            <p className="card-label">Savings Amount</p>
            <h2 className="card-value">
              ₹{(account.saving_amount || account.savingAmount || 0).toLocaleString('en-IN', { 
                minimumFractionDigits: 2, 
                maximumFractionDigits: 2 
              })}
            </h2>
          </div>
        </div>

        <div className="summary-card card-status">
          <div className="card-icon">
            {(account.status || "").toUpperCase() === "ACTIVE" ? "✓" : "✗"}
          </div>
          <div className="card-content">
            <p className="card-label">Account Status</p>
            <h2 className={`card-value status-${(account.status || "").toLowerCase()}`}>
              {(account.status || "N/A").toUpperCase()}
            </h2>
          </div>
        </div>
      </div>

      {/* Account Details Card */}
      <div className="details-card">
        <div className="card-header">
          <h3 className="card-title">Account Information</h3>
          <button className="btn btn-sm btn-secondary" onClick={onEdit}>
            <span className="btn-icon">✏️</span>
            Edit
          </button>
        </div>

        <div className="details-grid">
          <div className="detail-row">
            <div className="detail-item">
              <span className="detail-icon">🔢</span>
              <div className="detail-content">
                <p className="detail-label">Account Number</p>
                <p className="detail-value">{getAccountField('account_number', 'accountNumber')}</p>
              </div>
            </div>

            <div className="detail-item">
              <span className="detail-icon">📁</span>
              <div className="detail-content">
                <p className="detail-label">Account Type</p>
                <p className="detail-value">{getAccountField('account_type', 'accountType')}</p>
              </div>
            </div>
          </div>

          <div className="detail-row">
            <div className="detail-item">
              <span className="detail-icon">🏛️</span>
              <div className="detail-content">
                <p className="detail-label">Bank Name</p>
                <p className="detail-value">{getAccountField('bank_name', 'bankName')}</p>
              </div>
            </div>

            <div className="detail-item">
              <span className="detail-icon">📍</span>
              <div className="detail-content">
                <p className="detail-label">Branch</p>
                <p className="detail-value">{getAccountField('branch', 'branch')}</p>
              </div>
            </div>
          </div>

          <div className="detail-row">
            <div className="detail-item">
              <span className="detail-icon">🔐</span>
              <div className="detail-content">
                <p className="detail-label">IFSC Code</p>
                <p className="detail-value">{getAccountField('ifsc_code', 'ifscCode')}</p>
              </div>
            </div>

            <div className="detail-item">
              <span className="detail-icon">👤</span>
              <div className="detail-content">
                <p className="detail-label">Account Holder</p>
                <p className="detail-value">
                  {getAccountField('name_on_account', 'nameOnAccount') !== "N/A" 
                    ? getAccountField('name_on_account', 'nameOnAccount') 
                    : customer?.name || "N/A"}
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>

      {/* Quick Actions */}
      <div className="actions-card">
        <h3 className="card-title">Quick Actions</h3>
        <div className="action-buttons">
          <button className="action-btn action-btn-primary" onClick={onMakeTransaction}>
            <div className="action-icon">💳</div>
            <div className="action-content">
              <h4>Make Transaction</h4>
              <p>Transfer money or deposit funds</p>
            </div>
          </button>

          <button className="action-btn action-btn-secondary" onClick={onViewTransactions}>
            <div className="action-icon">📊</div>
            <div className="action-content">
              <h4>View Transactions</h4>
              <p>Check your transaction history</p>
            </div>
          </button>

          <button className="action-btn action-btn-warning" onClick={onEdit}>
            <div className="action-icon">✏️</div>
            <div className="action-content">
              <h4>Edit Account</h4>
              <p>Update account information</p>
            </div>
          </button>

          <button className="action-btn action-btn-danger" onClick={handleDelete}>
            <div className="action-icon">🗑️</div>
            <div className="action-content">
              <h4>Delete Account</h4>
              <p>Permanently remove this account</p>
            </div>
          </button>
        </div>
      </div>
    </div>
  );
}

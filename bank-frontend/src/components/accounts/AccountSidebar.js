import React from "react";
import { deleteAccountAPI } from "../../api/AccountAPI";

export default function AccountSidebar({
  customer,
  account,
  isEditing,
  onBackToDashboard,
  onMakeTransaction,
  onViewTransactions,
  onEditToggle,
  onDelete,
}) {
  const handleDelete = async () => {
    if (!account?.account_id && !account?.accountId) return;
    if (!window.confirm("⚠️ Are you sure you want to delete this account? This action cannot be undone.")) return;

    try {
      await deleteAccountAPI(account.account_id || account.accountId);
      onDelete();
    } catch (err) {
      console.error("Error deleting account:", err);
      alert("Failed to delete account.");
    }
  };

  return (
    <aside className="sidebar">
      <div className="sidebar-header">
        <div className="bank-logo">
          <div className="logo-icon">🏦</div>
          <h2 className="bank-name">Bank</h2>
        </div>
      </div>

      <nav className="sidebar-menu">
        <button className="menu-btn menu-btn-back" onClick={onBackToDashboard}>
          <span className="btn-icon">←</span>
          <span>Back to Dashboard</span>
        </button>
        
        {account && !isEditing && (
          <>
            <div className="menu-divider"></div>
            <p className="menu-label">Quick Actions</p>
            
            <button className="menu-btn menu-btn-action" onClick={onMakeTransaction}>
              <span className="btn-icon">💳</span>
              <span>Make Transaction</span>
            </button>
            
            <button className="menu-btn menu-btn-action" onClick={onViewTransactions}>
              <span className="btn-icon">📊</span>
              <span>View Transactions</span>
            </button>
            
            <div className="menu-divider"></div>
            <p className="menu-label">Account Settings</p>

            <button>Get Account Details</button>
            
            <button className="menu-btn menu-btn-edit" onClick={onEditToggle}>
              <span className="btn-icon">✏️</span>
              <span>Edit Account</span>
            </button>
            
            <button className="menu-btn menu-btn-danger" onClick={handleDelete}>
              <span className="btn-icon">🗑️</span>
              <span>Delete Account</span>
            </button>
          </>
        )}
      </nav>

      <div className="sidebar-footer">
        <p className="footer-text">© 2025 SecureBank</p>
        <p className="footer-subtext">Secure Banking Portal</p>
      </div>
    </aside>
  );
}

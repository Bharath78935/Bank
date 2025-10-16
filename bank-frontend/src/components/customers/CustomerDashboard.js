import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import GetCustomer from "./GetCustomer"; // Customer profile component
import DeleteCustomer from "./DeleteCustomer";
import UpdateCustomer from "./CustomerForm";
import "../styles/CustomerDashboard.css";

export default function Dashboard() {
  const [activeTab, setActiveTab] = useState("details");
  const navigate = useNavigate();

  const logout = () => {
    localStorage.removeItem("username");
    window.location.href = "/";
  };

  return (
    <div className="dashboard-wrapper">
      {/* Sidebar */}
      <aside className="sidebar">
        <div className="sidebar-header">
          <h2 className="bank-name">Banking System</h2>
        </div>

        <nav className="sidebar-menu">
          <button
            className={`menu-btn ${activeTab === "details" ? "active" : ""}`}
            onClick={() => setActiveTab("details")}
          >
            👤 My Profile
          </button>

          {/* Navigate to Manage Accounts page */}
          <button
            className="menu-btn"
            onClick={() => navigate("/manage-accounts")}
          >
            🏦 Manage Account
          </button>

          <button
            className={`menu-btn ${activeTab === "update" ? "active" : ""}`}
            onClick={() => setActiveTab("update")}
          >
            ✏️ Update Details
          </button>

          <button
            className={`menu-btn ${activeTab === "delete" ? "active" : ""}`}
            onClick={() => setActiveTab("delete")}
          >
            ❌ Delete Customer
          </button>
        </nav>

        <button onClick={logout} className="logout-btn">
          🚪 Logout
        </button>
      </aside>

      {/* Main content */}
      <main className="main-content">
        <header className="topbar">
          <h1 className="page-title">
            {activeTab === "details" && "Customer Profile"}
            {activeTab === "update" && "Update Customer Details"}
            {activeTab === "delete" && "Delete Customer"}
          </h1>
        </header>

        <div className="content-area">
          {activeTab === "details" && <GetCustomer />}
          {activeTab === "update" && <UpdateCustomer />}
          {activeTab === "delete" && <DeleteCustomer />}
        </div>
      </main>
    </div>
  );
}

import React from "react";
import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import LoginForm from "./components/LoginForm";
import RegisterForm from "./components/RegisterForm";          // adjust import
import Dashboard from "./components/customers/CustomerDashboard";
import ManageAccountsPage from "./components/accounts/ManageAccountsPage";
import MakeTransaction from "./components/transactions/MakeTransaction";
import ViewTransactions from "./components/transactions/ViewTransaction";



function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<LoginForm />} />
        <Route path="/register" element={<RegisterForm />} />   {/* use URL path */}
        <Route path="/dashboard" element={<Dashboard />} /> 
        <Route path="/manage-accounts" element={<ManageAccountsPage />} />
        <Route path="/transactions" element={<MakeTransaction />} />
        <Route path="/view-transactions" element={<ViewTransactions />} />
      </Routes>
    </Router>
  );
}

export default App;

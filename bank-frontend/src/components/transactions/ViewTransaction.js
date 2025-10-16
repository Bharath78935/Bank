import React, { useState, useEffect } from "react";
import { useNavigate, useLocation } from "react-router-dom";
import axios from "axios";
import Loading from "../common/Loading";
import "../styles/ViewTransactions.css";

export default function ViewTransactions() {
  const navigate = useNavigate();
  const { account, customer } = useLocation().state || {};

  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [downloading, setDownloading] = useState(false);

  useEffect(() => {
    if (!account) return;
    fetchTransactions();
  }, [account]);

  const fetchTransactions = async () => {
    setLoading(true);
    setError("");
    try {
      const acctNum = account.account_number || account.accountNumber;
      if (!acctNum) throw new Error("Account number missing");

      const resp = await axios.get(
        `http://localhost:8080/banking/api/transactions/transaction/account/${acctNum}`
      );

      setTransactions(Array.isArray(resp.data) ? resp.data : []);
    } catch (err) {
      console.error(err);
      setError("Failed to load transactions.");
      setTransactions([]);
    } finally {
      setLoading(false);
    }
  };

  const downloadCsv = async () => {
    setDownloading(true);
    try {
      const acctNum = account.account_number || account.accountNumber;
      const resp = await axios.get(
        `http://localhost:8080/banking/api/transactions/account/${acctNum}`,
        { responseType: "blob" }
      );
      const blob = new Blob([resp.data], { type: "text/csv;charset=UTF-8" });
      const url = window.URL.createObjectURL(blob);
      const link = document.createElement("a");
      link.href = url;
      link.setAttribute("download", `transactions_${acctNum}.csv`);
      document.body.appendChild(link);
      link.click();
      link.remove();
      window.URL.revokeObjectURL(url);
    } catch (err) {
      console.error("Download failed", err);
      alert("Could not download transaction history.");
    } finally {
      setDownloading(false);
    }
  };

  if (!account || !customer) {
    return (
      <div className="error-container">
        <h2>No Account Found</h2>
        <p>Please go back to the dashboard and try again.</p>
        <button className="btn btn-primary" onClick={() => navigate("/dashboard")}>
          Go to Dashboard
        </button>
      </div>
    );
  }

  if (loading) return <Loading />;

  return (
    <div className="transactions-page-wrapper">
      {/* Header */}
      <div className="transaction-header-bar">
        <button className="btn-back" onClick={() => navigate(-1)}>← Back</button>
        <div className="account-info-wrapper">
          <h1>Transaction History</h1>
          <p className="account-info">Account: {account.account_number || account.accountNumber}</p>
        </div>
      </div>

      {/* Error */}
      {error && (
        <div className="alert-error">
          ⚠️ {error}
          <button className="btn-retry" onClick={fetchTransactions}>Retry</button>
        </div>
      )}

      {/* Table */}
      {transactions.length === 0 ? (
        <div className="empty-state">
          <div className="empty-icon">📭</div>
          <h3>No Transactions Found</h3>
          <p>You don’t have any transactions yet.</p>
        </div>
      ) : (
        <div className="transactions-table-container">
          <table className="transactions-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>UTR Number</th>
                <th>Date</th>
                <th>Type</th>
                <th>Mode</th>
                <th>Amount</th>
                <th>Balance</th>
                <th>Receiver</th>
                <th>Branch</th>
                <th>Description</th>
              </tr>
            </thead>
            <tbody>
              {transactions.map((txn) => (
                <tr key={txn.transactionId}>
                  <td>{txn.transactionId}</td>
                  <td>{txn.utrNumber}</td>
                  <td>{new Date(txn.transactionDate).toLocaleString("en-IN")}</td>
                  <td>{txn.transactionType}</td>
                  <td>{txn.mode}</td>
                  <td className={txn.transactionType === "DEPOSIT" ? "credit" : "debit"}>
                    ₹{txn.transactionAmount.toLocaleString("en-IN", { minimumFractionDigits: 2, maximumFractionDigits: 2 })}
                  </td>
                  <td>₹{txn.balanceAmount.toLocaleString("en-IN", { minimumFractionDigits: 2, maximumFractionDigits: 2 })}</td>
                  <td>{txn.receiverAccount}</td>
                  <td>{txn.bankBranch}</td>
                  <td>{txn.description}</td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      {/* Download CSV */}
      <div className="download-container">
        <button
          className="btn-secondary"
          onClick={downloadCsv}
          disabled={downloading || transactions.length === 0}
        >
          {downloading ? "Downloading..." : "Print"}
        </button>
      </div>
    </div>
  );
}

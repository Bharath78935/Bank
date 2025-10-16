import React, { useState, useEffect } from "react";
import { getTransactionsByAccountNumber } from "../api/TransactionAPI";
import Loading from "./common/Loading";

export default function TransactionsPage({ account, view }) {
  const [amount, setAmount] = useState(0);
  const [success, setSuccess] = useState("");
  const [transactions, setTransactions] = useState([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    if (view === "history" && account) {
      fetchTransactions();
    }
  }, [view, account]);

  const fetchTransactions = async () => {
    setLoading(true);
    setError("");
    
    try {
      const accountNumber = account?.account_number || account?.accountNumber;
      
      if (!accountNumber) {
        throw new Error("Account number is missing");
      }

      const data = await getTransactionsByAccountNumber(accountNumber);
      setTransactions(data || []);
    } catch (err) {
      console.error("Error fetching transactions:", err);
      setError("Failed to load transactions. Please try again.");
      setTransactions([]);
    } finally {
      setLoading(false);
    }
  };

  const handleTransaction = async () => {
    try {
      // await makeTransactionAPI(account.account_id, amount);
      setSuccess(`Transaction of ${amount} successful!`);
    } catch {
      setSuccess("Transaction failed!");
    }
  };

  if (view === "history") {
    if (loading) {
      return <Loading />;
    }

    return (
      <div className="transaction-history">
        <h3>Transaction History</h3>
        
        {error && (
          <div className="alert alert-error">
            <span>{error}</span>
            <button onClick={fetchTransactions}>Retry</button>
          </div>
        )}

        {transactions.length === 0 && !error ? (
          <div className="empty-state">
            <p>No transactions found for this account.</p>
          </div>
        ) : (
          <div className="transactions-list">
            {transactions.map((transaction) => {
              const transactionType = transaction.type || transaction.transactionType;
              const transactionDate = transaction.date || transaction.transactionDate || transaction.createdAt;
              const transactionStatus = transaction.status || transaction.transactionStatus || "COMPLETED";
              
              return (
                <div key={transaction.id || transaction.transactionId} className="transaction-item">
                  <div className="transaction-info">
                    <span className="transaction-type">{transactionType}</span>
                    <span className="transaction-description">
                      {transaction.description || transaction.remarks || "No description"}
                    </span>
                    <span className="transaction-date">
                      {new Date(transactionDate).toLocaleDateString('en-IN', {
                        year: 'numeric',
                        month: 'short',
                        day: 'numeric',
                        hour: '2-digit',
                        minute: '2-digit'
                      })}
                    </span>
                  </div>
                  <div className="transaction-amount-status">
                    <span className={`amount ${transactionType === "DEPOSIT" ? "credit" : "debit"}`}>
                      {transactionType === "DEPOSIT" ? "+" : "-"}₹
                      {(transaction.amount || 0).toLocaleString('en-IN', {
                        minimumFractionDigits: 2,
                        maximumFractionDigits: 2
                      })}
                    </span>
                    <span className={`status status-${transactionStatus.toLowerCase()}`}>
                      {transactionStatus}
                    </span>
                  </div>
                </div>
              );
            })}
          </div>
        )}
      </div>
    );
  }

  return (
    <div className="make-transaction">
      <h3>Make Transaction</h3>
      {success && <p className="success-message">{success}</p>}
      <input 
        type="number" 
        value={amount} 
        onChange={(e) => setAmount(Number(e.target.value))} 
        placeholder="Amount" 
      />
      <button onClick={handleTransaction}>Submit</button>
    </div>
  );
}

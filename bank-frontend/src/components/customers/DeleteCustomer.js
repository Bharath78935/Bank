import React, { useState, useEffect } from "react";
import { deleteCustomer, getCustomerByUsername } from "../../api/CustomerAPI";

export default function DeleteCustomer() {
  const [customerId, setCustomerId] = useState(null);
  const [message, setMessage] = useState("");

  useEffect(() => {
    const fetchCustomerId = async () => {
      const username = localStorage.getItem("username");
      if (!username) return;

      try {
        const res = await getCustomerByUsername(username);
        setCustomerId(res.customerId); // store customerId for deletion
      } catch (err) {
        console.error(err);
        setMessage("Unable to fetch customer info.");
      }
    };

    fetchCustomerId();
  }, []);

  const handleDelete = async () => {
    if (!customerId) {
      setMessage("Customer ID not found.");
      return;
    }

    const confirmDelete = window.confirm(
      "Are you sure you want to delete your account? This action cannot be undone."
    );
    if (!confirmDelete) return;

    try {
      await deleteCustomer(customerId);
      setMessage("Your account has been deleted successfully!");
      localStorage.removeItem("username");
      window.location.href = "/"; // redirect to login
    } catch (err) {
      console.error(err);
      setMessage("Error deleting account.");
    }
  };

  return (
    <div className="delete-customer">
      <h2 className="text-xl font-semibold mb-4">Delete Account</h2>
      <button onClick={handleDelete} className="btn mt-2">
        Delete My Account
      </button>
      {message && <p className="mt-2">{message}</p>}
    </div>
  );
}

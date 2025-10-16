import axios from "axios";

const BASE_URL = "http://localhost:8080/banking/api/account";

// Fetch account by account number
export const getAccountByAccountNumber = async (accountNumber) => {
  try {
    const response = await axios.get(`${BASE_URL}/${accountNumber}`);
    return response.data; // returns the account object
  } catch (err) {
    console.error("Error fetching account:", err);
    return null; // return null if not found or error
  }
}
export const getAccountByCustomerId = async (customerId) => {
  try {
    const response = await axios.get(`${BASE_URL}/customer/${customerId}`);
    return response.data; // returns the account object
  } catch (err) {
    console.error("Error fetching account by customerId:", err);
    return null; // return null if not found or error
  }
};

// Create new account
export const createAccountAPI = async (account) => {
  try {
    const response = await axios.post(`${BASE_URL}/create`, account);
    return response.data; // newly created account object
  } catch (err) {
    console.error("Error creating account:", err);
    throw err; // throw to handle in component
  }
};

// Optional: update account
export const updateAccountAPI = async (account) => {
  try {
    const response = await axios.put(`${BASE_URL}/update`, account);
    return response.data;
  } catch (err) {
    console.error("Error updating account:", err);
    throw err;
  }
};

// Optional: delete account
export const deleteAccountAPI = async (accountId) => {
  try {
    const response = await axios.delete(`${BASE_URL}/${accountId}`);
    return response.data;
  } catch (err) {
    console.error("Error deleting account:", err);
    throw err;
  }
};

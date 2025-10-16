import axios from "axios";

const BASE_URL = "http://localhost:8080/banking/api/transactions";

export async function createTransaction(transactionData) {
  // transactionData: object with transaction fields to send in POST
  try {
    const response = await axios.post(`${BASE_URL}/create`, transactionData);
    return response.data;
  } catch (error) {
    throw error;
  }
}

export async function getTransactionsByAccountNumber(accountNumber) {
  try {
    const response = await axios.get(`${BASE_URL}/account/${accountNumber}`);
    return response.data;
  } catch (error) {
    throw error;
  }
}

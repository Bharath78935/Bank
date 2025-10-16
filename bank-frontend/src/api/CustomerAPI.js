import axios from 'axios';

const BASE_URL = "http://localhost:8080/banking/api/customer";

// Get customer by ID
export const getCustomerById = async (id) => {
  const res = await axios.get(`${BASE_URL}/${id}`);
  return res.data;
};

// Get customer by username
export const getCustomerByUsername = async (username) => {
  const res = await axios.get(`${BASE_URL}/username/${username}`);
  return res.data;
};

// Login customer
export const loginCustomer = async (username, password) => {
  try {
    const res = await axios.post(
      `${BASE_URL}/login`,
      { username, password },
      { headers: { "Content-Type": "application/json" } } // important!
    );
    return res.data;
  } catch (err) {
    console.error("API login error:", err.response?.data || err.message);
    throw err.response ? err.response.data : err.message;
  }
};


// Register new customer
export const registerCustomer = async (customerData) => {
  try {
    const res = await axios.post(`${BASE_URL}/create`, customerData);
    return res.data;
  } catch (err) {
    throw err.response ? err.response.data : err.message;
  }
};

export const createCustomer = async (data) => {
  const res = await axios.post(`${BASE_URL}`, data);
  return res.data;
};

export const updateCustomer = (customer) => {
  return axios.put(`${BASE_URL}/update`, customer);
};

export const deleteCustomer = async (id) => {
  const res = await axios.delete(`${BASE_URL}/${id}`);
  return res.data;
};

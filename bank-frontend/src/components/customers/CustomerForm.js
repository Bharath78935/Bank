import React, { useState, useEffect } from "react";
import { getCustomerByUsername, updateCustomer } from "../../api/CustomerAPI";
import "../styles/CustomerForm.css";

export default function CustomerForm() {
  const [customer, setCustomer] = useState(null);
  const [loading, setLoading] = useState(true);
  const [errors, setErrors] = useState({});
  const [success, setSuccess] = useState("");

  useEffect(() => {
    const username = localStorage.getItem("username");
    if (!username) return;

    const fetchCustomer = async () => {
      try {
        const res = await getCustomerByUsername(username);
        // Convert numbers to strings for validation
        const { password, pin, phoneNumber, aadharNumber, ...rest } = res;
        setCustomer({
          ...rest,
          password: "",
          pin: "",
          phoneNumber: phoneNumber ? String(phoneNumber) : "",
          aadharNumber: aadharNumber ? String(aadharNumber) : "",
        });
      } catch (err) {
        console.error(err);
        alert("Customer not found");
      } finally {
        setLoading(false);
      }
    };

    fetchCustomer();
  }, []);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setCustomer(prev => ({ ...prev, [name]: value }));
    setErrors(prev => ({ ...prev, [name]: "" }));
  };

  const validate = () => {
    const newErrors = {};
    if (!customer.customerName?.trim()) newErrors.customerName = "Name is required.";
    if (!customer.username?.trim()) newErrors.username = "Username is required.";
    if (!customer.email?.match(/^[^\s@]+@[^\s@]+\.[^\s@]+$/)) newErrors.email = "Invalid email format.";

    if (customer.phoneNumber && !/^\d{10}$/.test(customer.phoneNumber)) newErrors.phoneNumber = "Phone number must be 10 digits.";
    if (!customer.aadharNumber?.match(/^\d{12}$/)) newErrors.aadharNumber = "Aadhar must be 12 digits.";
    if (customer.password && customer.password.length < 6) newErrors.password = "Password must be at least 6 characters.";
    if (customer.pin && !/^\d{4}$/.test(customer.pin)) newErrors.pin = "PIN must be 4 digits.";
    if (!customer.gender) newErrors.gender = "Please select gender.";
    return newErrors;
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setErrors({});
    setSuccess("");

    const validationErrors = validate();
    if (Object.keys(validationErrors).length > 0) {
      setErrors(validationErrors);
      return;
    }

    if (!customer?.customerId) {
      setErrors({ general: "Customer ID missing!" });
      return;
    }

    // Copy to send to backend
    const updatedCustomer = { ...customer };

    if (!customer.password?.trim()) delete updatedCustomer.password;
    if (!customer.pin || customer.pin.trim() === "") delete updatedCustomer.pin;

    try {
      await updateCustomer(updatedCustomer);
      setSuccess("Customer details updated successfully!");
      setCustomer(prev => ({ ...prev, password: "", pin: "" }));
    } catch (err) {
      let msg = "";
      if (typeof err === "string") msg = err;
      else if (err?.response?.data) msg = typeof err.response.data === "string" ? err.response.data : JSON.stringify(err.response.data);
      else if (err?.message) msg = err.message;
      else msg = "Update failed. Please check details.";

      const lowerMsg = msg.toLowerCase();

      // Show field-specific error in red under the field
      if (lowerMsg.includes("email")) setErrors({ email: "Email already exists." });
      else if (lowerMsg.includes("username")) setErrors({ username: "Username already exists." });
      else if (lowerMsg.includes("aadhar")) setErrors({ aadharNumber: "Aadhar already exists." });
      else setErrors({ general: msg });
    }
  };

  if (loading) return <p>Loading...</p>;
  if (!customer) return <p className="text-center text-red-500">Customer not found</p>;

  const renderInput = (label, name, type = "text", required = false) => (
    <div className="form-row">
      <input
        type={type}
        name={name}
        value={customer[name] || ""}
        onChange={handleChange}
        placeholder=" "
        className={errors[name] ? "input-error" : ""}
        required={required}
      />
      <label>{label}{required && " *"}</label>
      {errors[name] && <span className="error-text">{errors[name]}</span>}
    </div>
  );

  return (
    <form className="form-container profile-card" onSubmit={handleSubmit}>
      <h2>Update My Details</h2>
      {errors.general && <div className="form-error">{errors.general}</div>}
      {success && <div className="form-success">{success}</div>}

      {renderInput("Name", "customerName", "text", true)}
      {renderInput("Username", "username", "text", true)}
      {renderInput("Email", "email", "email", true)}
      {renderInput("Phone", "phoneNumber", "text")}
      {renderInput("Aadhar", "aadharNumber", "text", true)}
      {renderInput("Address", "permanentAddress")}
      {renderInput("City", "city")}
      {renderInput("State", "state")}
      {renderInput("Country", "country")}
      {renderInput("DOB", "dob", "date")}
      {renderInput("Age", "age", "number")}

      <div className="form-row">
        <select
          name="gender"
          value={customer.gender || ""}
          onChange={handleChange}
          className={errors.gender ? "input-error" : ""}
          required
        >
          <option value="" disabled hidden>Select Gender</option>
          <option value="Male">Male</option>
          <option value="Female">Female</option>
          <option value="Other">Other</option>
        </select>
        <label>Gender *</label>
        {errors.gender && <span className="error-text">{errors.gender}</span>}
      </div>

      {renderInput("Father Name", "fatherName")}
      {renderInput("Mother Name", "motherName")}
      {renderInput("Status", "status")}
      {renderInput("Password", "password", "password")}
      {renderInput("PIN", "pin", "password")}

      <button className="btn-submit" type="submit">Update Details</button>
    </form>
  );
}

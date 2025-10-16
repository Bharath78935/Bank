import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { registerCustomer } from "../api/CustomerAPI";
import "./styles/RegisterForm.css";

export default function RegisterForm() {
  const [customer, setCustomer] = useState({
    customerName: "",
    username: "",
    email: "",
    phoneNumber: "",
    aadharNumber: "",
    permanentAddress: "",
    city: "",
    state: "",
    country: "",
    dob: "",
    age: "",
    gender: "",
    fatherName: "",
    motherName: "",
    status: "",
    password: "",
    pin: "",
  });

  const [errors, setErrors] = useState({});
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setCustomer((prev) => ({ ...prev, [name]: value }));
    setErrors((prev) => ({ ...prev, [name]: "" }));
  };

  const validate = () => {
    const newErrors = {};
    if (!customer.customerName.trim()) newErrors.customerName = "Name is required.";
    if (!customer.username.trim()) newErrors.username = "Username is required.";
    if (!customer.email.match(/^[^\s@]+@[^\s@]+\.[^\s@]+$/)) newErrors.email = "Invalid email format.";
    if (customer.phoneNumber && !/^\d{10}$/.test(customer.phoneNumber)) newErrors.phoneNumber = "Phone number must be 10 digits.";
    if (!customer.aadharNumber.match(/^\d{12}$/)) newErrors.aadharNumber = "Aadhar number must be 12 digits.";
    if (!customer.password || customer.password.length < 6) newErrors.password = "Password must be at least 6 characters.";
    if (!customer.pin.match(/^\d{4}$/)) newErrors.pin = "PIN must be 4 digits.";
    if (!customer.gender) newErrors.gender = "Please select gender.";
    return newErrors;
  };

  const handleRegister = async (e) => {
    e.preventDefault();
    const newErrors = validate();
    if (Object.keys(newErrors).length > 0) {
      setErrors(newErrors);
      return;
    }

    try {
      await registerCustomer(customer);
      alert("🎉 Registration successful! Please login.");
      navigate("/");
    } catch (err) {
  console.error("🔥 Registration error object:", err);

  // Normalize the error message — support plain string, Error object, or Axios response
  let msg = "";

  if (typeof err === "string") {
    msg = err;
  } else if (err?.response?.data) {
    msg = typeof err.response.data === "string"
      ? err.response.data
      : JSON.stringify(err.response.data);
  } else if (err?.message) {
    msg = err.message;
  } else {
    msg = "Registration failed. Please check details.";
  }

  const lowerMsg = msg.toLowerCase();
  console.log("Extracted error message:", lowerMsg);

  // Match for specific fields
  if (lowerMsg.includes("email")) {
    setErrors({ email: "Email already exists." });
  } else if (lowerMsg.includes("username")) {
    setErrors({ username: "Username already exists." });
  } else if (lowerMsg.includes("aadhar")) {
    setErrors({ aadharNumber: "Aadhar already exists." });
  } else {
    setErrors({ general: msg });
  }
}

  };

  const renderInput = (label, name, type = "text", required = false, maxLength) => (
    <div className="form-group">
      <input
        type={type}
        name={name}
        value={customer[name]}
        onChange={handleChange}
        placeholder=" "
        maxLength={maxLength}
        className={errors[name] ? "input-error" : ""}
        required={required}
      />
      <label className={customer[name] ? "filled" : ""}>{label}{required && " *"}</label>
      {errors[name] && <span className="error-text">{errors[name]}</span>}
    </div>
  );

  return (
    <div className="register-page">
      <div className="glass-card">
        <h2 className="title">Create New Account</h2>
        {errors.general && <div className="form-error">{errors.general}</div>}

        <form onSubmit={handleRegister}>
          {renderInput("Full Name", "customerName", "text", true)}
          {renderInput("Username", "username", "text", true)}
          {renderInput("Email", "email", "email", true)}
          {renderInput("Phone Number", "phoneNumber", "text", false, 10)}
          {renderInput("Aadhar Number", "aadharNumber", "text", true, 12)}
          {renderInput("Address", "permanentAddress")}
          {renderInput("City", "city")}
          {renderInput("State", "state")}
          {renderInput("Country", "country")}
          {renderInput("Date of Birth", "dob", "date")}
          {renderInput("Age", "age", "number")}

          <div className="form-group">
            <select
              name="gender"
              value={customer.gender}
              onChange={handleChange}
              className={`${errors.gender ? "input-error" : ""} ${customer.gender ? "filled" : ""}`}
              required
            >
              <option value=""></option>
              <option value="Male">Male</option>
              <option value="Female">Female</option>
              <option value="Other">Other</option>
            </select>
            <label className={customer.gender ? "filled" : ""}>Gender *</label>
            {errors.gender && <span className="error-text">{errors.gender}</span>}
          </div>

          {renderInput("Father's Name", "fatherName")}
          {renderInput("Mother's Name", "motherName")}
          {renderInput("Status", "status")}
          {renderInput("Password", "password", "password", true)}
          {renderInput("PIN (4 digits)", "pin", "password", true, 4)}

          <button className="btn-submit" type="submit">Register</button>
        </form>

        <p className="login-link">
          Already have an account? <Link to="/">Login</Link>
        </p>
      </div>
    </div>
  );
}
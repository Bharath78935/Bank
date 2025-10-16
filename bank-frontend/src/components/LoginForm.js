import React, { useState } from "react";
import { useNavigate, Link } from "react-router-dom";
import { loginCustomer } from "../api/CustomerAPI";
import "./LoginForm.css";

export default function LoginForm() {
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleLogin = async (e) => {
  e.preventDefault();
  try {
    const data = await loginCustomer(username, password);
    console.log("Login success:", data); // check what backend returns
    localStorage.setItem("username", username);
    navigate("/dashboard");
  } catch (err) {
    console.error("Login failed:", err); // logs the backend error
    setError("Invalid username or password");
  }
};

  return (
    <div className="login-page">
      <div className="glass-card">
        <h2 className="title">Customer Login</h2>
        {error && <div className="form-error">{error}</div>}
        <form>
          <div className="form-group">
            <input
              type="text"
              name="username"
              placeholder=" "
              value={username}
              onChange={(e) => setUsername(e.target.value)}
              required
            />
            <label>Username</label>
          </div>
          <div className="form-group">
            <input
              type="password"
              name="password"
              placeholder=" "
              value={password}
              onChange={(e) => setPassword(e.target.value)}
              required
            />
            <label>Password</label>
          </div>
          <button
            type="submit"
            className="btn-submit"
            onClick={handleLogin}
          >
            Login
          </button>
        </form>
        <div className="login-link">
          New here? <Link to="/register">Register</Link>
        </div>
      </div>
    </div>
  );
}

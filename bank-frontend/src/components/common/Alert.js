import React from "react";
import "../styles/Common.css";

export default function Alert({ type, message }) {
  return (
    <div className={`alert alert-${type}`}>
      <span className="alert-icon">
        {type === "error" && "⚠️"}
        {type === "success" && "✅"}
        {type === "info" && "ℹ️"}
      </span>
      <div className="alert-content">
        <strong>{type === "error" ? "Error" : type === "success" ? "Success" : "Info"}</strong>
        <p>{message}</p>
      </div>
    </div>
  );
}

import React, { useEffect, useState } from "react";
import { getCustomerByUsername } from "../../api/CustomerAPI";

export default function GetCustomer() {
  const [customer, setCustomer] = useState(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const username = localStorage.getItem("username");
    if (!username) {
      alert("No user logged in");
      setLoading(false);
      return;
    }

   const fetchCustomer = async () => {
  try {
    const data = await getCustomerByUsername(username); // already res.data
    console.log("Customer data:", data); // see full object
    setCustomer(data); // ✅ set the customer object directly
  } catch (err) {
    console.error(err);
    alert("Customer not found");
  } finally {
    setLoading(false);
  }
};


    fetchCustomer();
  }, []);

  if (loading) return <p className="text-center">Loading...</p>;
  if (!customer) return <p className="text-center text-red-500">Customer not found</p>;

  return (
  <div className="profile-card">
    <h2>My Details</h2>
    {loading ? (
      <div className="skeleton">
        <div className="skeleton-line"></div>
        <div className="skeleton-line"></div>
        <div className="skeleton-line"></div>
      </div>
    ) : (
      <div className="profile-info">
        <p><strong>Name:</strong> {customer.customerName}</p>
        <p><strong>Email:</strong> {customer.email}</p>
        <p><strong>Username:</strong> {customer.username}</p>
        <p><strong>Aadhar:</strong> {customer.aadharNumber}</p>
        <p><strong>City:</strong> {customer.city}</p>
        <p><strong>State:</strong> {customer.state}</p>
        <p><strong>Country:</strong> {customer.country}</p>
        <p><strong>Phone:</strong> {customer.phoneNumber}</p>
        <p><strong>Status:</strong> {customer.status}</p>
        <p><strong>DOB:</strong> {customer.dob}</p>
        <p><strong>Gender:</strong> {customer.gender}</p>
        <p><strong>Father Name:</strong> {customer.fatherName}</p>
        <p><strong>Mother Name:</strong> {customer.motherName}</p>
      </div>
    )}
  </div>
);


  // return (
  //   <div className="p-6 bg-white rounded-lg shadow-lg max-w-md mx-auto mt-8">
  //     <h2 className="text-2xl font-semibold mb-4 text-center text-blue-700">
  //       My Details
  //     </h2>
  //     <div className="space-y-2">
  //       <p><strong>Name:</strong> {customer.customerName}</p>
  //       <p><strong>Email:</strong> {customer.email}</p>
  //       <p><strong>Username:</strong> {customer.username}</p>
  //       <p><strong>Aadhar:</strong> {customer.aadharNumber}</p>
  //       <p><strong>City:</strong> {customer.city}</p>
  //       <p><strong>State:</strong> {customer.state}</p>
  //       <p><strong>Country:</strong> {customer.country}</p>
  //       <p><strong>Phone:</strong> {customer.phoneNumber}</p>
  //       <p><strong>Status:</strong> {customer.status}</p>
  //       <p><strong>DOB:</strong> {customer.dob}</p>
  //       <p><strong>Gender:</strong> {customer.gender}</p>
  //       <p><strong>Father Name:</strong> {customer.fatherName}</p>
  //       <p><strong>Mother Name:</strong> {customer.motherName}</p>
  //     </div>
  //   </div>
  // );
}

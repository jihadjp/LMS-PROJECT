import React from 'react';
import { Navigate } from 'react-router-dom';
import { authService } from '../api/auth.service';

// Admin-only route protection
export const AdminRoute = ({ children }) => {
  const isAdminAuthenticated = authService.isAdminAuthenticated();
  
  if (!isAdminAuthenticated) {
    // Admin না হলে home page এ redirect করবে
    return <Navigate to="/" replace />;
  }
  
  return children;
};

// User-only route protection
export const UserRoute = ({ children }) => {
  const isUserAuthenticated = authService.isUserAuthenticated();
  
  if (!isUserAuthenticated) {
    // User না হলে login page এ redirect করবে
    return <Navigate to="/login" replace />;
  }
  
  return children;
};

// Both Admin and User can access (any logged in user)
export const PrivateRoute = ({ children }) => {
  const token = localStorage.getItem('token');
  
  if (!token) {
    // কোন authentication না থাকলে login page এ redirect করবে
    return <Navigate to="/login" replace />;
  }
  
  return children;
};
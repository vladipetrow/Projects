import React, { createContext, useContext, useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";

// Define the shape of the AuthContext
interface AuthContextType {
  isAuthenticated: boolean;
  login: () => void;
  logout: () => void;
}

// Create the AuthContext
const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const navigate = useNavigate(); // Now this works because it's inside BrowserRouter

  // Check authentication state
  const checkAuthState = () => {
    const isAuth = document.cookie.split("; ").some((cookie) => cookie.startsWith("Authorization="));
    setIsAuthenticated(isAuth);
  };

  useEffect(() => {
    checkAuthState();
  }, []);

  const login = () => {
    setIsAuthenticated(true); // Update authentication state
  };

  const logout = () => {
    document.cookie = "Authorization=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;"; // Clear cookies
    setIsAuthenticated(false); // Update state
    window.location.replace("/"); // Redirect to home
  };

  return (
    <AuthContext.Provider value={{ isAuthenticated, login, logout }}>
      {children}
    </AuthContext.Provider>
  );
};

// Custom hook to use the AuthContext
export const useAuth = () => {
  const context = useContext(AuthContext);
  if (!context) {
    throw new Error("useAuth must be used within an AuthProvider");
  }
  return context;
};

import React, { createContext, useContext, useState, useEffect } from "react";
import { useNavigate } from "react-router-dom";
import { isAuthenticated as checkAuth, clearAuthCache } from "../utils/authUtils";

// Define the shape of the AuthContext
interface AuthContextType {
  isAuthenticated: boolean;
  isLoading: boolean;
  userRole: string | null;
  login: () => void;
  logout: () => void;
}

// Create the AuthContext
const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: React.ReactNode }> = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [userRole, setUserRole] = useState<string | null>(null);
  const [forceLoggedOut, setForceLoggedOut] = useState(() => {
    // Check if user was force logged out in this session
    return sessionStorage.getItem('forceLoggedOut') === 'true';
  });
  const navigate = useNavigate();
  
  // Development mode - set to false for normal operation
  const DEV_MODE = false;

  // Check authentication state
  const checkAuthState = async () => {
    console.log("AuthContext - checkAuthState called, forceLoggedOut:", forceLoggedOut);
    
    // If we force logged out, don't check auth
    if (forceLoggedOut) {
      console.log("Skipping auth check - force logged out");
      setIsAuthenticated(false);
      setUserRole(null);
      setIsLoading(false);
      return;
    }
    
    try {
      setIsLoading(true);
      console.log("AuthContext - Checking authentication with backend...");
      const isAuth = await checkAuth();
      console.log("AuthContext - Backend auth check result:", isAuth);
      setIsAuthenticated(isAuth);
      
      // If authenticated, get user role
      if (isAuth) {
        // Clear forceLoggedOut flag since user is successfully authenticated
        sessionStorage.removeItem('forceLoggedOut');
        setForceLoggedOut(false);
        
        try {
          console.log("AuthContext - Getting user role...");
          const response = await fetch('http://localhost:8080/auth/check', {
            credentials: 'include'
          });
          console.log("AuthContext - Role check response status:", response.status);
          if (response.ok) {
            const data = await response.json();
            setUserRole(data.role || null);
            console.log("AuthContext - userRole:", data.role);
          } else {
            console.log("AuthContext - Role check failed, setting role to null");
            setUserRole(null);
          }
        } catch (error) {
          console.error("Error getting user role:", error);
          setUserRole(null);
        }
      } else {
        console.log("AuthContext - Not authenticated, clearing role");
        setUserRole(null);
      }
    } catch (error) {
      console.error("Error checking authentication:", error);
      setIsAuthenticated(false);
      setUserRole(null);
    } finally {
      setIsLoading(false);
      console.log("AuthContext - checkAuthState completed, final state:", { isAuthenticated, userRole, isLoading });
    }
  };

  useEffect(() => {
    // Check authentication state on app start
    checkAuthState();

    // Removed automatic re-checking listeners to prevent navbar flickering
  }, []);

  const login = async () => {
    console.log("AuthContext - Login called, checking auth state...");
    sessionStorage.removeItem('forceLoggedOut'); // Clear persistent flag
    setForceLoggedOut(false); // Reset flag when logging in
    await checkAuthState();
  };

  const logout = async () => {
    console.log("AuthContext - Logout called, current state:", { isAuthenticated, userRole, forceLoggedOut });
    
    // Set persistent flag to prevent any auth checks
    sessionStorage.setItem('forceLoggedOut', 'true');
    setForceLoggedOut(true);
    
    // Clear everything immediately
    clearAuthCache();
    setIsAuthenticated(false);
    setUserRole(null);
    setIsLoading(false);
    
    console.log("AuthContext - Cleared local state");
    
    // Clear cookies with more specific settings
    document.cookie = "Authorization=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/; domain=localhost;";
    document.cookie = "RefreshToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/; domain=localhost;";
    document.cookie = "Authorization=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    document.cookie = "RefreshToken=; expires=Thu, 01 Jan 1970 00:00:00 UTC; path=/;";
    
    console.log("AuthContext - Cleared cookies, current cookies:", document.cookie);
    
    // Call appropriate backend logout endpoint based on user role
    const logoutEndpoint = userRole === 'AGENCY' ? '/agency/logout' : '/logout';
    console.log("AuthContext - Calling backend logout:", logoutEndpoint);
    
    try {
      const response = await fetch(`http://localhost:8080${logoutEndpoint}`, {
        method: 'POST',
        credentials: 'include'
      });
      console.log("AuthContext - Backend logout response:", response.status);
    } catch (error) {
      console.log("Backend logout call failed (expected if server not running):", error);
    }
    
    // Add a small delay to ensure cookies are cleared
    setTimeout(() => {
      console.log("AuthContext - Forcing page reload after logout");
      window.location.href = "/";
    }, 100);
  };

  return (
    <AuthContext.Provider value={{ isAuthenticated, isLoading, userRole, login, logout }}>
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

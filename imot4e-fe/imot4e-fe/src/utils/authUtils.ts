// Utility functions for authentication using backend endpoint

let authCache: { authenticated: boolean; role: string; userId?: number } | null = null;
let lastCheckTime = 0;
const CACHE_DURATION = 5000; // 5 seconds

export const getAuthToken = async (): Promise<string | null> => {
  console.log("getAuthToken - Checking authentication via backend...");
  console.log("getAuthToken - Current cookies:", document.cookie);
  
  try {
    const response = await fetch('http://localhost:8080/auth/check', {
      method: 'GET',
      credentials: 'include', // Include cookies
    });
    
    console.log("getAuthToken - Response status:", response.status);
    
    if (response.ok) {
      const data = await response.json();
      console.log("getAuthToken - Auth check response:", data);
      
      if (data.authenticated) {
        authCache = {
          authenticated: true,
          role: data.role,
          userId: data.userId
        };
        lastCheckTime = Date.now();
        console.log("getAuthToken - User is authenticated, returning token");
        return 'authenticated';
      } else {
        authCache = { authenticated: false, role: '' };
        lastCheckTime = Date.now();
        console.log("getAuthToken - User is not authenticated, returning null");
        return null;
      }
    } else {
      console.log("getAuthToken - Auth check failed with status:", response.status);
      authCache = { authenticated: false, role: '' };
      lastCheckTime = Date.now();
      return null;
    }
  } catch (error) {
    console.error("getAuthToken - Error checking auth:", error);
    authCache = { authenticated: false, role: '' };
    lastCheckTime = Date.now();
    return null;
  }
};

export const getUserRole = async (): Promise<string> => {
  // Use cache if available and not expired
  if (authCache && (Date.now() - lastCheckTime) < CACHE_DURATION) {
    console.log("Using cached role:", authCache.role);
    return authCache.role;
  }
  
  const token = await getAuthToken();
  console.log("getUserRole - Token:", token ? "Present" : "Missing");
  
  if (!token) {
    console.log("No token found, returning empty role");
    return "";
  }
  
  return authCache?.role || "";
};

export const isAuthenticated = async (): Promise<boolean> => {
  // Always check with backend, don't use cache
  console.log("isAuthenticated - Checking authentication with backend (bypassing cache)");
  const token = await getAuthToken();
  console.log("isAuthenticated - Token result:", token ? "Present" : "Missing");
  return token !== null;
};

// Clear cache when user logs out
export const clearAuthCache = () => {
  console.log("Clearing auth cache");
  authCache = null;
  lastCheckTime = 0;
};
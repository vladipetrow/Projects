// API Configuration
export const API_CONFIG = {
  BASE_URL: process.env.REACT_APP_API_BASE_URL || 'http://localhost:8080',
  ENDPOINTS: {
    // Auth
    USER_LOGIN: '/user/login',
    AGENCY_LOGIN: '/agency/login',
    USER_REGISTER: '/user/register',
    AGENCY_REGISTER: '/agency/register',
    LOGOUT: '/logout',
    AGENCY_LOGOUT: '/agency/logout',
    AUTH_CHECK: '/auth/check',
    PASSWORD_RESET: '/reset-password',
    
    // Posts
    POSTS_LIST: '/posts/list/posts',
    POSTS_ADD: '/posts/add',
    POSTS_UPDATE: '/posts/update',
    POSTS_DELETE: '/posts/delete',
    POSTS_FILTER: '/posts/filter',
    POSTS_VIEW: '/posts/view',
    
    // Dashboard
    DASHBOARD_POSTS: '/dashboard/my-posts',
    
    // Subscriptions
    SUBSCRIPTION_CREATE: '/subscriptions/subscribe',
    SUBSCRIPTION_TIERS: '/subscription-tiers',
    
    // Upload
    UPLOAD_IMAGE: '/upload/image'
  }
} as const;

// Helper function to build full URLs
export const buildApiUrl = (endpoint: string): string => {
  return `${API_CONFIG.BASE_URL}${endpoint}`;
};

// Common fetch options
export const getDefaultFetchOptions = (): RequestInit => ({
  credentials: 'include',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Fetch options for file uploads
export const getFileUploadOptions = (): RequestInit => ({
  credentials: 'include',
  // Don't set Content-Type for FormData - let browser set it with boundary
});

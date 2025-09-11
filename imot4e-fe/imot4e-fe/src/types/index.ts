// Common types used across the application

export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  passwordHash?: string;
  salt?: string;
}

export interface Agency {
  id: number;
  agencyName: string;
  email: string;
  phoneNumber: string;
  address: string;
  passwordHash?: string;
  salt?: string;
}

export interface Post {
  postId: number;
  location: string;
  price: number;
  area: number;
  description: string;
  type: string; // ApartmentType enum
  transactionType: 'BUY' | 'RENT';
  userId: number;
  agencyId: number;
  imageUrls: string[];
  postDate: string;
  isPromoted: boolean;
  viewCount?: number;
}

export interface Subscription {
  id: number;
  userId: number;
  agencyId: number;
  email: string;
  tier: string;
  status: 'ACTIVE' | 'INACTIVE' | 'EXPIRED';
  chargeId: string;
  checkoutUrl: string;
  expirationDate: string;
}

export interface SubscriptionTier {
  id: number;
  name: string;
  type: 'USER' | 'AGENCY';
  price: number;
  maxPosts: number;
  features: string[];
}

export interface ApiResponse<T = any> {
  success: boolean;
  data?: T;
  message?: string;
  error?: string;
}

export interface PaginatedResponse<T> {
  posts: T[];
  totalCount: number;
  totalPages: number;
  currentPage: number;
}

export interface AuthState {
  isAuthenticated: boolean;
  isLoading: boolean;
  userRole: string | null;
  userId?: number;
}

export interface FilterState {
  type: 'BUY' | 'RENT';
  location: string;
  propertyType: string;
  maxPrice: string;
}

export interface PostInput {
  location: string;
  price: number;
  area: number;
  description: string;
  apartmentType: string;
  type: 'BUY' | 'RENT';
}

export interface UserInput {
  firstName: string;
  lastName: string;
  email: string;
  password: string;
}

export interface AgencyInput {
  agencyName: string;
  email: string;
  password: string;
  phoneNumber: string;
  address: string;
}

// Enum types
export type ApartmentType = 
  | 'STUDIO'
  | 'ONE_BEDROOM'
  | 'TWO_BEDROOM'
  | 'THREE_BEDROOM'
  | 'FOUR_BEDROOM'
  | 'MULTI_BEDROOM'
  | 'HOUSE'
  | 'PENTHOUSE';

export type TransactionType = 'BUY' | 'RENT';

export type UserRole = 'ROLE_USER' | 'ROLE_AGENCY';

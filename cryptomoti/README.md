# 🏠 Cryptomoti - Real Estate Platform with Cryptocurrency Payments

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7+-green.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18-blue.svg)](https://reactjs.org/)
[![TypeScript](https://img.shields.io/badge/TypeScript-4.0+-blue.svg)](https://www.typescriptlang.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com/)
[![Redis](https://img.shields.io/badge/Redis-6.0+-red.svg)](https://redis.io/)
[![Material-UI](https://img.shields.io/badge/Material--UI-5.0+-blue.svg)](https://mui.com/)

**Cryptomoti** is a modern real estate platform that revolutionizes property advertising by integrating cryptocurrency payments. Users can post property listings for sale or rent, with a unique subscription system powered by multiple cryptocurrencies via Coinbase Commerce.

## 🎯 What is Cryptomoti?

Cryptomoti is a **real estate marketplace** where:
- **Property owners** can list apartments and houses for sale or rent
- **Agencies** can manage multiple property listings with enhanced features
- **Users** can search and filter properties with interactive maps
- **Subscriptions** are powered by **cryptocurrency payments** - no traditional payment methods required

The platform features two user types with different posting limits:
- **Regular Users**: 3 free ads (5 when subscribed)
- **Agencies**: 5 free ads (10 when subscribed)

**Future Vision**: Enable tenants to pay rent directly with cryptocurrencies, creating a fully crypto-based real estate ecosystem.

## 🚀 Quick Start

### Prerequisites
- Java 17+, Node.js 16+, MySQL 8.0+

### Backend
```bash
cd cryptomoti/workProject1
# Configure database in application.properties
./gradlew bootRun
# Runs on http://localhost:8080
```

### Frontend
```bash
cd imot4e-fe
npm install --force
npm start
# Runs on http://localhost:3000
```

## ✨ Key Features

### 🏠 Property Management
- **Smart Listings**: Create detailed property posts with multiple images
- **Advanced Search**: Filter by location, price, property type, and amenities with real-time filtering
- **View Analytics**: Track property view counts and engagement
- **Image Management**: Cloudinary integration for optimized image storage and delivery

### 👥 User System
- **Dual User Types**: Regular users and real estate agencies with different privileges
- **Cross-Table Email Uniqueness**: Advanced validation preventing duplicate emails across user types
- **Subscription Tiers**: Cryptocurrency-powered subscriptions for enhanced posting limits
- **User Dashboard**: Personal property management and statistics
- **Role-Based Access**: Secure authentication with different endpoints for users and agencies

### 💰 Cryptocurrency Integration
- **Multi-Crypto Payments**: Support for Bitcoin, Ethereum, USDT, SOL, and 100+ cryptocurrencies via Coinbase Commerce
- **QR Code Payments**: Mobile-friendly payment process for all supported cryptos
- **Automatic Activation**: Instant subscription activation after payment confirmation
- **Real-time Conversion**: Automatic fiat-to-crypto conversion for seamless payments

### ⚡ Performance & Caching
- **Redis Caching**: High-performance caching for posts, user data, and filtered results
- **Smart Cache Invalidation**: Intelligent cache management with atomic operations
- **Debounced Search**: Optimized search with 500ms debouncing to reduce server load
- **Lazy Loading**: Images load only when entering viewport for better performance
- **Memoized Components**: React components optimized to prevent unnecessary re-renders

### 🔐 Security & Communication
- **JWT Authentication**: Secure token-based authentication with 15-minute access tokens and 7-day refresh tokens
- **HTTP-Only Cookies**: Secure token storage with SameSite protection
- **Email Notifications**: Automated confirmations for registration, payments, and subscriptions
- **Password Security**: SHA-256 hashing with salt and pepper
- **Database Triggers**: Cross-table data integrity enforcement at database level

## 🛠️ Tech Stack

**Backend:** Spring Boot, Java 17, MySQL, Redis, JWT, Gradle  
**Frontend:** React 18, TypeScript, Material-UI v5, Custom Hooks  
**External:** Coinbase Commerce (100+ cryptos), Google Maps, Cloudinary, Mailgun  
**Caching:** Redis with atomic operations and smart invalidation  
**Performance:** Debounced search, lazy loading, memoized components

## 📁 Project Structure

```
cryptomoti/
├── workProject1/                    # Spring Boot Backend
│   ├── coreServices/               # Business logic & caching
│   │   ├── models/                 # Data models
│   │   ├── ServiceExeptions/       # Custom exceptions
│   │   ├── EmailCacheService.java  # Redis email caching
│   │   ├── PostCacheService.java   # Redis post caching
│   │   └── RedisKeyTracker.java    # Cache key management
│   ├── repositories/               # Data access layer
│   │   └── mysql/                  # MySQL implementations
│   ├── web/api/                    # REST controllers
│   ├── security/                   # JWT & authentication
│   └── config/                     # Redis configuration
└── imot4e-fe/                      # React Frontend
    ├── src/ads/                    # Property components
    ├── src/auth/                   # Authentication
    ├── src/components/             # Reusable components
    ├── src/hooks/                  # Custom React hooks
    ├── src/contexts/               # React contexts
    └── src/config/                 # API configuration
```

## ⚙️ Configuration

Update `application.properties` with your credentials:

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/cryptomoti
spring.datasource.username=your_username
spring.datasource.password=your_password

# Redis Configuration
spring.redis.host=localhost
spring.redis.port=6379
spring.redis.timeout=2000ms
spring.redis.jedis.pool.max-active=8
spring.redis.jedis.pool.max-idle=8
spring.redis.jedis.pool.min-idle=0

# JWT (15-minute access tokens, 7-day refresh tokens)
jwt.secretKey=your_jwt_secret

# Coinbase Commerce
COINBASE_API_KEY=your_api_key
COINBASE_WEBHOOK_SECRET=your_webhook_secret

# Cloudinary (Images)
CLOUDINARY_CLOUD_NAME=your_cloud_name
CLOUDINARY_API_KEY=your_api_key
CLOUDINARY_API_SECRET=your_api_secret

# Mailgun (Email)
MAILGUN_DOMAIN=your_domain
MAILGUN_API_KEY=your_api_key
MAILGUN_FROM_EMAIL=your_email@domain.com
```

## 🚀 Production

1. Generate new peppers using `PepperGenerator`
2. Use environment variables for sensitive data
3. Configure production database
4. Deploy with proper secret management

---

**Built with ❤️ using Spring Boot and React**
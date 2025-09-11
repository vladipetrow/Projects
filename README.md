# 🚀 Projects Portfolio

[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7+-green.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18-blue.svg)](https://reactjs.org/)
[![TypeScript](https://img.shields.io/badge/TypeScript-4.0+-blue.svg)](https://www.typescriptlang.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0+-blue.svg)](https://www.mysql.com/)

Welcome to my **Projects Portfolio**! This repository showcases a collection of full-stack applications demonstrating modern development practices, clean architecture, and cutting-edge technologies.

## 📋 Table of Contents
- [🎯 Overview](#-overview)
- [🛠️ Featured Projects](#️-featured-projects)
- [🚀 Quick Start](#-quick-start)
- [📚 Technologies Used](#-technologies-used)
- [📞 Contact](#-contact)

## 🎯 Overview

This portfolio represents my journey as a **Full-Stack Developer**, featuring projects that demonstrate:
- **Enterprise-level architecture** with clean code principles
- **Modern tech stacks** including Spring Boot, React, and TypeScript
- **Performance optimization** with Redis caching and lazy loading
- **Security best practices** with JWT authentication and data validation
- **Real-world applications** solving actual business problems

Each project is production-ready with comprehensive documentation, testing, and deployment configurations.

## 🛠️ Featured Projects

### 🏠 [Cryptomoti](cryptomoti/) - Real Estate Platform with Cryptocurrency Payments
**A modern real estate marketplace revolutionizing property advertising with crypto payments**

- **🎯 Purpose**: Property listing platform where users can post apartments/houses for sale or rent
- **💰 Unique Feature**: Cryptocurrency-powered subscriptions (Bitcoin, Ethereum, 100+ cryptos)
- **🏗️ Architecture**: Spring Boot backend + React frontend with Redis caching
- **🔐 Security**: JWT authentication, cross-table email validation, database triggers
- **⚡ Performance**: Redis caching, debounced search, lazy loading, memoized components
- **📱 Tech Stack**: Java 17, Spring Boot, React 18, TypeScript, MySQL, Redis, Material-UI v5

**Key Highlights:**
- Multi-layer caching strategy reducing API calls by 80%
- 15-minute access tokens with 7-day refresh tokens for enhanced security
- Real-time filtering with 500ms debouncing
- Cross-table email uniqueness enforcement
- Production-ready with comprehensive error handling

### 🎮 [Bomb](bomb/) - Bomberman 2D Game
**Classic arcade-style game built with Java Swing**

- **🎯 Purpose**: 2D Bomberman game where players eliminate enemies with strategic bomb placement
- **🎮 Gameplay**: Navigate mazes, place bombs, collect power-ups, and survive enemy encounters
- **💻 Tech Stack**: Java, Java Swing, Object-Oriented Programming
- **🏗️ Architecture**: Clean separation of game logic, rendering, and user input

**Key Features:**
- Real-time game mechanics with collision detection
- Power-up system with various bomb types
- Enemy AI with pathfinding algorithms
- Score tracking and level progression

## 🚀 Quick Start

### Prerequisites
- **Java 17+** for backend projects
- **Node.js 16+** for frontend projects
- **MySQL 8.0+** for database
- **Redis 6.0+** for caching (Cryptomoti)

### Installation
```bash
# Clone the repository
git clone https://github.com/vladipetrow/Projects.git
cd Projects

# Navigate to specific project
cd cryptomoti/workProject1  # Backend
cd imot4e-fe/imot4e-fe     # Frontend
cd bomb                    # Game

# Follow individual project READMEs for detailed setup
```

### Running Projects
```bash
# Cryptomoti Backend
cd cryptomoti/workProject1
./gradlew bootRun

# Cryptomoti Frontend  
cd imot4e-fe/imot4e-fe
npm install
npm start

# Bomb Game
cd bomb
javac *.java
java Main
```

## 📚 Technologies Used

### Backend Technologies
- **Java 17** - Modern Java features and performance
- **Spring Boot 2.7+** - Enterprise application framework
- **Spring Security** - Authentication and authorization
- **Spring Data JPA** - Database abstraction layer
- **MySQL 8.0+** - Relational database management
- **Redis 6.0+** - In-memory data structure store
- **JWT** - JSON Web Token authentication
- **Gradle** - Build automation and dependency management

### Frontend Technologies
- **React 18** - Modern UI library with hooks
- **TypeScript 4.0+** - Type-safe JavaScript
- **Material-UI v5** - Component library and design system
- **React Router** - Client-side routing
- **Custom Hooks** - Reusable stateful logic
- **Context API** - Global state management

### External Services
- **Coinbase Commerce** - Cryptocurrency payment processing
- **Cloudinary** - Image upload and management
- **Mailgun** - Transactional email service
- **Google Maps API** - Location services

### Development Practices
- **Clean Architecture** - Separation of concerns
- **SOLID Principles** - Object-oriented design
- **Test-Driven Development** - Comprehensive test coverage
- **RESTful API Design** - Standard HTTP methods and status codes
- **Performance Optimization** - Caching, lazy loading, memoization
- **Security Best Practices** - Input validation, secure authentication

## 📞 Contact

**Vladimir Petrov**  
Full-Stack Developer

- **GitHub**: [@vladipetrow](https://github.com/vladipetrow)
- **Email**: [Your Email]
- **LinkedIn**: [Your LinkedIn Profile]

---

⭐ **Star this repository** if you find any of these projects interesting or useful!

**Built with ❤️ using modern technologies and best practices**

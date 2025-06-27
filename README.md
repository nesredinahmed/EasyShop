# EasyShop - E-commerce REST API

A robust Spring Boot-based REST API for an e-commerce platform with JWT authentication, shopping cart functionality, and comprehensive product management.

## 🚀 Features

- **🔐 JWT Authentication & Authorization**
    - Secure user registration and login
    - Role-based access control
    - Token-based authentication

- **🛍️ Shopping Cart Management**
    - Add/remove products from cart
    - Update quantities
    - Clear entire cart
    - Persistent cart storage

- **📦 Product Management**
    - Product search with filters (category, price range, color)
    - Featured products
    - Product categories
    - Stock management

- **👤 User Profile Management**
    - User registration and profiles
    - Profile updates
    - Address and contact information

- **🖼️ Image Management**
    - Static image serving
    - Placeholder image generation
    - Support for multiple image formats (JPG, PNG, GIF, WebP)

- **🔒 Security Features**
    - Spring Security integration
    - CORS support
    - Input validation
    - Error handling




## 📁 Project Structure

```
EasyShop/
├── src/
│   ├── main/
│   │   ├── java/org/yearup/
│   │   │   ├── controllers/          # REST controllers
│   │   │   ├── data/                 # Data access layer
│   │   │   ├── models/               # Domain models
│   │   │   ├── security/             # Security configuration
│   │   │   └── configurations/       # Application configuration
│   │   └── resources/
│   │       ├── static/images/        # Product images
│   │       └── application.properties
│   └── test/                         # Test files
├── database/                         # Database scripts
├── lib/                             # External libraries
└── pom.xml                          # Maven configuration
```


⭐ **Here is some screenshots Home Page**
![Screenshot 2025-06-26 183125.png](READMe/Screenshot%202025-06-26%20183125.png)


⭐ **Here is some screenshots Profile Page**

![Screenshot 2025-06-26 183139.png](READMe/Screenshot%202025-06-26%20183139.png)


⭐ **Here is some screenshots Cart Page**

![Screenshot 2025-06-26 183158.png](READMe/Screenshot%202025-06-26%20183158.png)
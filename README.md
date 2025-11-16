# 💳 E-Wallet & Payment Gateway Simulator

A microservices-based e-wallet system built using **Java, Spring Boot, and MySQL**, designed to simulate wallet management, wallet-to-wallet transfers, adding money using a payment gateway, and QR-code–based payments.

This project is currently under development and will later expand into a complete microservices architecture with authentication, API Gateway, Razorpay integration, and more.

---

## 📌 Project Overview

This application allows users to:
- Create an account
- Maintain a wallet
- Add money using a payment gateway
- Transfer money to other users
- Pay using QR Codes (UPI-like flow)

Future phases introduce microservices, distributed config, Kubernetes deployment, and real-world payment integration.

---

## 🛠 Tech Stack

**Backend**
- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security (JWT) *(unfinished/remaining)*
- Razorpay API *(unfinished/remaining)*

**Database**
- MySQL
- Hibernate/JPA

**DevOps**
- Docker *(unfinished/remaining)*
- Kubernetes *(unfinished/remaining)*
- Spring Cloud (Eureka, Config, Gateway) *(unfinished/remaining)*

---

## 🚀 Current Development Phase

### Phase 1 – Core Modules Development
- User Module
- Wallet Module
- Basic Transaction Flow
- QR Code generation for payment requests

---

## 📦 Features & Status

### ✅ Completed
- [x] User Entity + Wallet Entity
- [x] Wallet auto-creation on user registration
- [x] Add money (dummy flow for now)
- [x] Wallet-to-Wallet transfer
- [x] QR Code generation for payment

### 🚧 In Progress / Remaining
- [ ] User Registration/Login with JWT *(unfinished/remaining)*
- [ ] API Security (Bearer token middleware) *(unfinished/remaining)*
- [ ] Razorpay Sandbox Payment Integration *(unfinished/remaining)*
- [ ] Payment Confirmation Webhook *(unfinished/remaining)*
- [ ] Transaction History with filtering *(unfinished/remaining)*
- [ ] Merchant Payment Flow (Scan & Pay) *(unfinished/remaining)*
- [ ] Microservices Migration
    - [ ] User Service *(unfinished/remaining)*
    - [ ] Wallet Service *(unfinished/remaining)*
    - [ ] Payment Service *(unfinished/remaining)*
    - [ ] Notification Service *(unfinished/remaining)*
- [ ] API Gateway *(unfinished/remaining)*
- [ ] Eureka Service Discovery *(unfinished/remaining)*
- [ ] Centralized Config Server *(unfinished/remaining)*
- [ ] Dockerization *(unfinished/remaining)*
- [ ] Kubernetes Deployment *(unfinished/remaining)*

---

## 🗂 Project Structure

```
e-wallet/
│
├── user/
│   ├── UserController.java
│   ├── UserService.java
│   └── UserRepository.java
│
├── wallet/
│   ├── WalletController.java
│   ├── WalletService.java
│   └── WalletRepository.java
│
├── transaction/
│   ├── TransactionController.java
│   ├── TransactionService.java
│   └── TransactionRepository.java
│
└── payment/
    ├── PaymentController.java
    ├── PaymentService.java
    └── qr/
        ├── QRGenerator.java
```

*(Structure may evolve after microservices split.)*

---

## 🔐 Authentication (Planned)

JWT-based authentication:
- `/auth/register`
- `/auth/login`
- Access tokens sent via `Authorization: Bearer <token>`
- All microservices protected by API Gateway *(unfinished/remaining)*

---

## 💸 Payment Flow

### Add Money (Current Dummy Flow)
1. User enters amount
2. Backend creates a “Payment Request”
3. QR generated / link created
4. After simulation → Wallet balance updated

### Planned Razorpay Flow
- Create order → Generate payment link
- Use Razorpay Checkout form
- Razorpay verifies payment
- Backend webhook updates transaction status *(unfinished/remaining)*

---

## 📍 QR-Code Payment Flow

1. User scans QR
2. QR contains `paymentRequestId`
3. User approves payment
4. Their wallet transfers amount to merchant
5. Transaction stored in DB

---

## 🏗️ Setup Instructions

### Clone Project
```bash
git clone <repository-url>
cd e-wallet
```

### Configure MySQL
```sql
CREATE DATABASE ewallet;
```

### application.properties
```
spring.datasource.url=jdbc:mysql://localhost:3306/ewallet
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

### Run Application
```bash
./mvnw spring-boot:run
```

---

## 🧪 API Testing

Use Postman or Thunder Client.

### Create User
```
POST /users/register
```

### Add Money
```
POST /wallet/add
```

### Transfer Money
```
POST /wallet/transfer
```

### Generate Payment QR
```
POST /payment/qr
```

---

## 📅 Future Enhancements

- UPI-style PIN system
- Monthly statements
- Merchant dashboard
- Full microservices with distributed tracing
- Notification service (email/SMS)

---

## 🤝 Contributing

Pull requests are welcome once the project stabilizes.

---

## 📄 License

MIT License (or your preferred license)


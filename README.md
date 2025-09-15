# E-Wallet & Payment Gateway Simulator 💳

A microservices-based e-wallet application built with **Java + Spring Boot + MySQL**.  
Features wallet-to-wallet transfers, add money via payment gateway, and QR code payments.

## 🛠 Tech Stack
- Java 17 + Spring Boot
- MySQL + JPA/Hibernate
- Docker + Kubernetes (later phases)
- Spring Cloud (Eureka, Config Server, API Gateway)
- JWT Security

## 🚀 Current Phase
Phase 1 – Building core features (User, Wallet, Transactions)

## 📦 Features Roadmap
- [ ] User Registration/Login (JWT)
- [ ] Wallet Management (view balance, add money)
- [ ] Wallet-to-Wallet Transfer
- [ ] Transaction History
- [ ] Payment Gateway Sandbox Integration
- [ ] QR Code Payments
- [ ] Notifications (optional)

## 🏗 Setup
```bash
# clone repository
git clone <repo_url>
cd e-wallet

# build & run (initially)
./mvnw spring-boot:run

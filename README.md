# eWallet Project

## Overview

eWallet is a microservices-based application for managing digital wallets, built with **Spring Boot** and **Spring Cloud Gateway**. The project leverages **Kafka** for messaging and **JWT** for security.

## Microservices

1. **auth-service**: Handles user authentication and authorization.
2. **user-service**: Manages user profiles and information.
3. **wallet-service**: Manages wallets, including creating, transferring funds, converting currencies, top-ups, and withdrawals.
4. **report-service**: Generates transaction reports and provides Excel downloads.
5. **gateway-service**: Routes requests to appropriate microservices and applies security filters.

## Technologies Used

- **Spring Boot**
- **Spring Cloud Gateway**
- **Kafka**
- **JWT**

## Getting Started

### Prerequisites

- Java 17+
- Maven
- Kafka

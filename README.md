# 🏃 Fitness Tracker Application (Microservices Backend + Frontend)

A robust **microservice-based fitness tracking system** built using **Spring Boot**, **Spring Security**, **MongoDB**, **PostgreSQL**, and **RabbitMQ**, with **Keycloak** for authentication and **Spring Cloud Config** for centralized configuration. The application also integrates **AI-powered recommendations** for personalized fitness insights using Gemini API, and includes a modern **React.js** frontend.

---

## 🚀 Core Features

- 🔐 OAuth2 / OIDC authentication with **Keycloak**
- ⚙️ Microservice architecture: independently deployable services
- 🛡️ Centralized config management using **Spring Cloud Config Server**
- 🧠 AI-powered recommendation engine using Gemini API
- 🔄 Asynchronous messaging using **RabbitMQ**
- 📚 REST APIs for activity tracking, user management, and recommendations
- 🌍 Gateway routing via **Spring Cloud Gateway**
- 📂 PostgreSQL & MongoDB integration
  - `user-service` uses **PostgreSQL** for relational user data
  - `activity-service` and `recommendation-service` use **MongoDB** for NoSQL operations
- 🖥️ React frontend for tracking activities, viewing progress, and managing profiles

---

## 🧱 Project Structure

```
Fitness-Application/
├── user-service/           # Manages user data (PostgreSQL)
├── activity-service/       # Tracks user activities (MongoDB)
├── recommendation-service/ # AI-based fitness suggestions (MongoDB)
├── api-gateway/            # Central entry point for routing
├── config-server/          # Centralized externalized config
├── frontend/               # React.js client (calls backend via gateway)
```

---

## 💠 Technologies

| Component        | Tech Stack                                  |
|------------------|---------------------------------------------|
| Core Framework   | Spring Boot (Microservices)                 |
| Auth & Security  | Keycloak, Spring Security, JWT              |
| Config Server    | Spring Cloud Config Server                  |
| Gateway          | Spring Cloud Gateway                        |
| Databases        | PostgreSQL (User), MongoDB (Activity, Rec)  |
| Messaging Queue  | RabbitMQ                                    |
| AI Integration   | Gemini API (Google Generative AI)           |
| Frontend         | React.js, Axios and Other                   |

---

## 🐳 External Dependencies (via Docker)

### 🔑 Run Keycloak

```bash
docker run --name keycloak \
  -p 127.0.0.1:8084:8080 \
  -e KC_BOOTSTRAP_ADMIN_USERNAME=admin \
  -e KC_BOOTSTRAP_ADMIN_PASSWORD=admin \
  -v keycloak_data:/opt/keycloak/data \
  quay.io/keycloak/keycloak:26.3.1 start-dev
```

### 📩 Run RabbitMQ

```bash
docker run -it --rm --name rabbitmq \
  -p 5672:5672 -p 15672:15672 \
  rabbitmq:4-management
```

---

## 📌 Port Summary

| Component             | Port    | Description                            |
|-----------------------|---------|----------------------------------------|
| Keycloak              | 8084    | Admin Console                          |
| RabbitMQ (AMQP)       | 5672    | Message broker port                    |
| RabbitMQ UI           | 15672   | Web-based Management UI                |
| Config Server         | 8888    | Serves all service configs             |
| API Gateway           | 8080    | Routes all external/internal requests  |
| PostgreSQL            | 5432    | Used by `user-service`                 |
| MongoDB               | 27017   | Used by `activity` and `recommendation`|
| Frontend (Dev Server) | 8085    | React UI during local development      |

---

## 👤 Author

**Shafay Khan**  
GitHub: [@shafaykhan](https://github.com/shafaykhan)

---

## ⭐ Support

If you found this full-stack architecture useful, leave a ⭐ and feel free to contribute or raise issues.

---

## ⚠️ Disclaimer

This README was generated with assistance from **AI tools**, including ChatGPT. While the structure and content aim to provide clarity and accuracy, always validate configurations, ports, and implementation details according to your environment.

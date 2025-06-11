# 🛒 E-Commerce Platform (Java 1.8 + Spring Boot + JPA)
# Description
Welcome to HexaFusion, a modern E-commerce backend built on Java, powered by Spring Boot and Spring Data JPA. Designed specifically for selling electronic items—such as smartphones, laptops, cameras, and accessories—this robust platform features a RESTful API and a secure admin panel.

A backend for an electronics e-commerce platform built with Java 1.8, powered by Spring Boot and Spring Data JPA. It supports:

- RESTful APIs for managing Products, Categories, Orders, and Users

- An Admin Panel to create, update, and delete entities

- A layered architecture: Controller → Service → Repository → Entity

This backend is designed to be efficient, maintainable, and scalable—forming a solid foundation for any full-stack e-commerce system
# 📋Features
- ## Product Management
  Add, update, and delete electronic items with comprehensive metadata—multiple images, pricing tiers, stock management, and specifications.

- ## Category Management
  Organize items into custom categories (e.g., “Smartphones”, “Laptops”, “Audio”) for easy browsing.
- ## Order Processing
  Full order lifecycle support—create, view, update, and manage customer orders with seamless tracking and fulfillment options.

- ## User Management
  Registration and login with role-based access, distinguishing between customer and admin privileges using Spring Security.

- ## Admin Panel Endpoints
  Secure, admin-only CRUD APIs to manage all entities—products, categories, users, and orders.

- ## 🔐 Spring Security Authentication (Session-Based)
  Uses standard Spring Security session cookie authentication—no JWT required 

  Supports form-based login with server-managed sessions, plus optional HTTP Basic for admin/API access

- ## 📩 OTP for Secure Registration & Password Reset

  OTP on Sign-Up: Users get a time-limited one-time code via email to verify and activate their account before logging in 

  Forgot Password Workflow: Secure process where users request a reset via email, receive an OTP or token, and reset their password with validation and expiry checks

  Tokens/OTPs are stored in the database with an expiration (e.g., 5–10 minutes), ensuring secure validation
# ⚙️ Tech Stack

| Component         | Technology                                      |
|------------------|-------------------------------------------------|
| Language          | Java 1.8                                        |
| Framework         | Spring Boot                                     |
| Persistence       | Spring Data JPA (Hibernate)                     |
| Database          | MySQL                             |
| API Exposure      | Spring Web MVC (REST controllers + Thymeleaf)   |
| Frontend          | Thymeleaf + HTML, CSS, JavaScript               |
| Build Tool        | Maven                                           |


# 🚀 Setup & Installation
## Prerequisites
Make sure you have installed:

- Java 1.8

- Maven 3.x

- MySQL
# 1. Clone & Enter Project
git clone https://github.com/vansh-rajvanshi/Ecommerce.git<br>
cd Ecommerce<br>
# 2. Cloudnary Configration
## Update src/main/Config/CloudnaryConfig with your Cloudnary settings:
- Add Cloud Name
- Add Api key
- Add Api Secret
# 3. Database Configuration
## Update src/main/resources/application.properties with your DB, File, Mail settings:
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce<br>
spring.datasource.username=root<br>
spring.datasource.password=yourPass<br>
spring.jpa.hibernate.ddl-auto=update<br>
spring.jpa.show-sql=true<br>
# 4. File Upload Configration
spring.servlet.multipart.enabled=true<br>
spring.servlet.multipart.max-file-size=20MB<br>
spring.servlet.multipart.max-request-size=20MB<br>
# 5. SMTP Configuration
spring.mail.host=smtp.gmail.com<br>
spring.mail.port=587<br>
spring.mail.username=your mail<br>
spring.mail.password=your password<br>

## Additional Mail Properties
spring.mail.properties.mail.smtp.auth=true<br>
spring.mail.properties.mail.smtp.starttls.enable=true<br>
spring.mail.properties.mail.smtp.starttls.required=true<br>
spring.mail.properties.mail.smtp.connectiontimeout=5000<br>
spring.mail.properties.mail.smtp.timeout=5000<br>
spring.mail.properties.mail.smtp.writetimeout=5000<br>
# 5. Build the Project
mvn clean install
# 🧩Running the Application
## Run from Your IDE
In IntelliJ IDEA, Spring Tool Suite, Eclipse, or VS Code:

- Import the project as a Maven project (if not already).

- Locate your main class (e.g., Application.java) annotated with @SpringBootApplication.

- In IntelliJ: click the 🔼 icon in the gutter or use Ctrl+Shift+F10 

- In Eclipse/STS: right-click → Run As → Spring Boot App or Java Application 

It will run an exploded version of your app using embedded Tomcat—just like Maven does 

# 🌍 Access the Website
## After launching, visit:
http://localhost:8080/

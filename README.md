# 🚗 Garage Billing System (Java + MySQL)

A full-fledged **Garage Billing System** built in **Java** using **MySQL** as the backend. It allows garages and workshops to manage customers, vehicles, services, job cards, and billing seamlessly from a simple console interface.

---

## ✅ Features

- 📇 Add and manage customers
- 🚙 Add and manage vehicles linked to customers
- 🛠️ Define and manage service types with cost
- 🧾 Create job cards with multiple services
- 💵 Auto-calculates total cost for job cards
- 📋 View job card history with date and total

---

## 🛠️ Technologies Used

- Java (JDK 8+)
- JDBC (Java Database Connectivity)
- MySQL
- Console-based User Interface

---

## 🧩 Database Setup

1. Start your MySQL server.
2. Create the database and tables by running the following script:

```sql
CREATE DATABASE garage_billing;
USE garage_billing;

CREATE TABLE customers (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    phone VARCHAR(20),
    email VARCHAR(100),
    address TEXT
);

CREATE TABLE vehicles (
    id INT AUTO_INCREMENT PRIMARY KEY,
    customer_id INT,
    registration_no VARCHAR(20),
    brand VARCHAR(50),
    model VARCHAR(50),
    type VARCHAR(20),
    FOREIGN KEY (customer_id) REFERENCES customers(id)
);

CREATE TABLE services (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100),
    cost DECIMAL(10, 2)
);

CREATE TABLE job_cards (
    id INT AUTO_INCREMENT PRIMARY KEY,
    vehicle_id INT,
    service_date DATE,
    total_cost DECIMAL(10, 2),
    FOREIGN KEY (vehicle_id) REFERENCES vehicles(id)
);

CREATE TABLE job_services (
    job_card_id INT,
    service_id INT,
    FOREIGN KEY (job_card_id) REFERENCES job_cards(id),
    FOREIGN KEY (service_id) REFERENCES services(id)
);
⚙️ How to Run
1. Compile the Java Program
Ensure you have the MySQL JDBC driver (mysql-connector-j-<version>.jar) in your classpath.

javac -cp .:mysql-connector-j-8.0.33.jar GarageBillingSystem.java
(Use ; instead of : on Windows.)

2. Run the Program

java -cp .:mysql-connector-j-8.0.33.jar GarageBillingSystem


🚀 Future Improvements
 GUI Interface using Java Swing

 Login System with Admin/User Roles

 PDF Invoice Generation

 Export Reports to Excel/CSV


👤 Author

Varad Naik





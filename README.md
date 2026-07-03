#  Canteen Pre-order System

![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Console App](https://img.shields.io/badge/Type-Console_App-4D4D4D?style=for-the-badge)
![OOP](https://img.shields.io/badge/Concept-OOP_Principles-brightgreen?style=for-the-badge)

A console-based College Canteen Pre-order System built in Core Java, designed to solve real-world queue congestion problems in college canteens. This project was developed as a team effort to demonstrate all 4 pillars of Object-Oriented Programming in a practical, real-world scenario.

---

## 🎯 Problem Statement
Students waste a lot of time standing in canteen queues, especially during lunch breaks. This system allows students to pre-order food, get a token number, and pick it up at the scheduled time — significantly reducing waiting time and improving canteen efficiency.

---

## ✨ Features

-  **Secure Access:** Student registration and login system with password authentication and a secure reset-password flow.
-  **Interactive Menu:** View menu categorized by Veg / Non-Veg / Beverages.
-  **Smart Ordering & Confirmation:** Place orders, review the total cost with a confirmation screen, and receive a unique token number.
-  **Slot Booking & Waitlist:** Peak-hour slot booking to avoid the rush. Includes an automated Waitlist system that auto-assigns slots to waitlisted students if an order is cancelled.
-  **Cancellation Policy:** Built-in cancellation system with a 5% cancellation fee deduction.
-  **Digital Wallet:** Built-in student digital wallet (add balance, auto-deduct on order).
-  **Order Tracking:** Maintain and view order history per student.
-  **Admin Insights:** Generate daily sales reports, view waitlist, and manage overall operations for canteen administration.
-  **Robust Error Handling:** Custom exceptions like `InsufficientBalanceException`, `SlotFullException`, and `OrderNotFoundException`.
-  **Data Persistence:** File handling to securely persist menu, students, and order data automatically.

---

## 💻 OOP Concepts Used

This project heavily relies on core Object-Oriented Programming principles:

| OOP Pillar | Where & How it is Used |
| :--- | :--- |
| **Abstraction** | `FoodItem` — abstract class with an abstract `getCategory()` method. |
| **Inheritance** | `VegItem`, `NonVegItem`, and `Beverage` classes extend the base `FoodItem` class. |
| **Polymorphism**| `calculatePrice()` and `getCategory()` behave differently per subclass; implemented via `Priceable` and `Orderable` interfaces. |
| **Encapsulation**| `Student` class — private `walletBalance` and `password`, accessed securely only via getters and setters (e.g., `addBalance()`, `deductBalance()`). |

---

## 📁 Project Structure
```text
CanteenPreorderSystem/
│
└── src/
    └── canteen/
        ├── data/                      # Text files for data persistence (Auto-generated)
        │   ├── menu.txt               # Stores menu items
        │   ├── orders.txt             # Stores order history
        │   ├── report.txt             # Stores daily sales reports
        │   └── students.txt           # Stores student profiles, passwords & wallet balances
        │
        ├── exceptions/                # Custom exception classes for error handling
        │   ├── InsufficientBalanceException.java
        │   ├── InvalidInputException.java
        │   ├── ItemNotFoundException.java
        │   ├── OrderNotFoundException.java
        │   └── SlotFullException.java
        │
        ├── filehandler/               # File I/O operations
        │   └── FileHandler.java       # Logic to read/write data to .txt files
        │
        ├── interfaces/                # Abstraction interfaces
        │   ├── Orderable.java         # Interface for ordering behavior
        │   ├── Priceable.java         # Interface for price calculation
        │   └── Reportable.java        # Interface for report generation
        │
        ├── manager/                   # Core business logic
        │   └── CanteenManager.java    # Manages overall canteen operations, waitlist & services
        │
        ├── model/                      
        │   ├── Beverage.java          # Extends FoodItem
        │   ├── FoodCategory.java      # Enum: VEG, NON_VEG, BEVERAGE 
        │   ├── FoodItem.java          # Abstract base class
        │   ├── NonVegItem.java        # Extends FoodItem
        │   ├── Order.java             # Contains order details and items
        │   ├── OrderStatus.java       # Enum: PENDING, READY, COMPLETED
        │   ├── Student.java           # Encapsulated student profile, password and wallet
        │   ├── Token.java             # Handles unique token generation
        │   ├── VegItem.java           # Extends FoodItem
        │   └── WaitlistEntry.java     # Handles waitlisted orders and automatic queue processing
        │
        ├── Main.java                  # Application entry point and User Interface
        └── Printer.java               # Helper class for clean and standardized console output

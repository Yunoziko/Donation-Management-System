# 💚 Donation Management System — Java Swing

A complete, professional **Donation Management System** built with **Core Java + Java Swing**.

---

## 📁 Project Structure

```
DonationManagementSystem/
└── src/
    ├── MainApp.java              ← Entry point
    ├── model/
    │   ├── Donor.java            ← Donor data model
    │   └── Donation.java         ← Donation data model
    ├── service/
    │   ├── DonorService.java     ← CRUD + validation logic for donors
    │   └── DonationService.java  ← Add/view + report logic for donations
    └── gui/
        ├── LoginFrame.java       ← Admin login screen
        ├── MainFrame.java        ← Root window with sidebar navigation
        ├── DonorPanel.java       ← Donor management UI
        ├── DonationPanel.java    ← Donation management UI
        └── ReportPanel.java      ← Reports & analytics UI
```

---

## 🚀 How to Run

### Prerequisites
- Java JDK 8 or later installed
- A terminal / command prompt

### Step 1 — Compile

```bash
cd "DonationManagementSystem/src"

# Compile all Java files
javac -d ../out model/Donor.java model/Donation.java \
      service/DonorService.java service/DonationService.java \
      gui/LoginFrame.java gui/MainFrame.java \
      gui/DonorPanel.java gui/DonationPanel.java gui/ReportPanel.java \
      MainApp.java
```

### Step 2 — Run

```bash
cd ../out
java MainApp
```

### One-liner (compile + run from project root)

```bash
cd "DonationManagementSystem"
mkdir -p out
javac -d out $(find src -name "*.java") && java -cp out MainApp
```

---

## 🔐 Login Credentials

| Field    | Value      |
|----------|------------|
| Username | `admin`    |
| Password | `admin123` |

---

## ✨ Features

### 👥 Donor Management
| Feature           | Detail                          |
|-------------------|---------------------------------|
| Add Donor         | Name, Contact (10-digit), Email |
| View All Donors   | Scrollable styled table         |
| Update Donor      | Click a row → edit → Update     |
| Delete Donor      | Confirmation dialog             |
| Search by ID      | Instant filter by Donor ID      |

### 💰 Donation Management
| Feature           | Detail                                      |
|-------------------|---------------------------------------------|
| Add Donation      | Donor ID, Amount, Date (YYYY-MM-DD), Purpose|
| Donor Validation  | Checks that donor exists before adding      |
| View All          | Table with resolved donor names             |

### 📊 Reports
| Feature                    | Detail                                      |
|----------------------------|---------------------------------------------|
| Total Donors               | Live count card                             |
| Total Donations            | Live count card                             |
| Total Amount Raised        | Summed across all donations                 |
| Donations by Specific Donor| Per-donor breakdown with sub-total          |

---

## 🛡️ Input Validation
- Name cannot be empty
- Contact must be exactly **10 digits**
- Email must contain **@**
- Donation amount must be **positive**
- Date must match **YYYY-MM-DD**
- Donor must **exist** before a donation can be recorded

---

## 🎨 UI Highlights
- Dark **glassmorphism-inspired** theme throughout
- Highlighted sidebar navigation with hover effects
- Row-click editing in the Donors table
- Colour-coded status messages (green = success, yellow = warning, red = error)

---

## 📝 Notes
- Data is held **in-memory** (ArrayList). Restart clears all data.
- To persist data, swap the `ArrayList` stores in the Service classes with JDBC + MySQL.

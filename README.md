# Milk Dairy Management System

A Spring Boot web application for managing dairy production, cows, and analyzing monthly statistics.

## Project Setup

### Prerequisites
- Java 17 or higher
- Maven 3.6+ (Optional, wrapper/script provided)

### Opening in IDE
To avoid "non-project file" warnings and ensure full IDE support (IntelliJ, VS Code, Eclipse):
1. **Open the `dairy-management` folder** directly as the root of your workspace.
   - **VS Code**: File > Open Folder... > Select `dairy-management`.
   - **IntelliJ**: File > Open... > Select `dairy-management` > Open as Project.

### Running the Application

#### Option 1: Using the provided PowerShell script (Windows)
This script sets up the environment and runs the application.
```powershell
.\run.ps1
```

#### Option 2: Using Maven
If you have Maven installed globally:
```bash
mvn spring-boot:run
```

## Features
- **Dashboard**: Overview of total production, average fat percentage, and top performing cow.
- **Manage Cows**: Add, view, and delete cows.
- **Record Production**: Log daily milk production for each cow.
- **Monthly Analysis**: View production charts and statistics.

## Technologies
- Java 17
- Spring Boot 3.2.0
- Thymeleaf
- H2 Database (In-memory)
- Chart.js

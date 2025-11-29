package com.example.dairy;

import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:h2:mem:dairy_management;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASSWORD = "";

    public static Connection getConnection() throws SQLException {
        try {
            // Load the H2 driver
            Class.forName("org.h2.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL JDBC Driver not found. Include the library in your classpath.", e);
        }
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection();
                Statement stmt = conn.createStatement()) {

            // Create cows table
            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS cows (
                            cow_id INT PRIMARY KEY AUTO_INCREMENT,
                            name VARCHAR(100) NOT NULL,
                            breed VARCHAR(100),
                            date_of_birth DATE,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                        )
                    """);

            // Create milk_production table
            stmt.execute("""
                        CREATE TABLE IF NOT EXISTS milk_production (
                            record_id INT PRIMARY KEY AUTO_INCREMENT,
                            cow_id INT NOT NULL,
                            production_date DATE NOT NULL,
                            shift VARCHAR(20) NOT NULL,
                            quantity DECIMAL(10, 2) NOT NULL,
                            fat_content DECIMAL(5, 2),
                            temperature DECIMAL(5, 2),
                            quality VARCHAR(20),
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (cow_id) REFERENCES cows(cow_id) ON DELETE CASCADE
                        )
                    """);

            // Create indexes for performance
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_production_date ON milk_production(production_date)");
            stmt.execute("CREATE INDEX IF NOT EXISTS idx_cow_date ON milk_production(cow_id, production_date)");

            System.out.println("✓ Database initialized successfully!");

        } catch (SQLException e) {
            System.err.println("✗ Database initialization error: " + e.getMessage());
            System.err.println("  Make sure MySQL is running and database 'dairy_management' exists.");
        }
    }
}

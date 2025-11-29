package com.example.dairy;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class DairyDAO {

    public void addCow(Cow cow) throws SQLException {
        String sql = "INSERT INTO cows (name, breed, date_of_birth) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            pstmt.setString(1, cow.getName());
            pstmt.setString(2, cow.getBreed());
            pstmt.setDate(3, Date.valueOf(cow.getDateOfBirth()));

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        cow.setCowId(generatedKeys.getInt(1));
                    }
                }
            }
        }
    }

    public List<Cow> getAllCows() throws SQLException {
        List<Cow> cows = new ArrayList<>();
        String sql = "SELECT * FROM cows";

        try (Connection conn = DatabaseConnection.getConnection();
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                cows.add(new Cow(
                        rs.getInt("cow_id"),
                        rs.getString("name"),
                        rs.getString("breed"),
                        rs.getDate("date_of_birth").toLocalDate()));
            }
        }
        return cows;
    }

    public void recordProduction(MilkProductionRecord record) throws SQLException {
        String sql = "INSERT INTO milk_production (cow_id, production_date, shift, quantity, fat_content, temperature, quality) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, record.getCowId());
            pstmt.setDate(2, Date.valueOf(record.getProductionDate()));
            pstmt.setString(3, record.getShift());
            pstmt.setDouble(4, record.getQuantity());
            pstmt.setDouble(5, record.getFatContent());
            pstmt.setDouble(6, record.getTemperature());
            pstmt.setString(7, record.getQuality());

            pstmt.executeUpdate();
        }
    }

    public List<MilkProductionRecord> getDailyProduction(LocalDate date) throws SQLException {
        List<MilkProductionRecord> records = new ArrayList<>();
        String sql = "SELECT * FROM milk_production WHERE production_date = ?";

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, Date.valueOf(date));
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    records.add(new MilkProductionRecord(
                            rs.getInt("record_id"),
                            rs.getInt("cow_id"),
                            rs.getDate("production_date").toLocalDate(),
                            rs.getString("shift"),
                            rs.getDouble("quantity"),
                            rs.getDouble("fat_content"),
                            rs.getDouble("temperature"),
                            rs.getString("quality")));
                }
            }
        }
        return records;
    }

    public List<MonthlyStats> getMonthlyAnalysis(int month, int year) throws SQLException {
        List<MonthlyStats> stats = new ArrayList<>();
        String sql = """
                    SELECT
                        c.cow_id,
                        c.name,
                        SUM(mp.quantity) as total_milk,
                        SUM(CASE WHEN mp.shift = 'Morning' THEN mp.quantity ELSE 0 END) as morning_milk,
                        SUM(CASE WHEN mp.shift = 'Evening' THEN mp.quantity ELSE 0 END) as evening_milk,
                        AVG(mp.fat_content) as avg_fat,
                        COUNT(*) as record_count
                    FROM cows c
                    JOIN milk_production mp ON c.cow_id = mp.cow_id
                    WHERE MONTH(mp.production_date) = ? AND YEAR(mp.production_date) = ?
                    GROUP BY c.cow_id, c.name
                """;

        try (Connection conn = DatabaseConnection.getConnection();
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, month);
            pstmt.setInt(2, year);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    stats.add(new MonthlyStats(
                            rs.getInt("cow_id"),
                            rs.getString("name"),
                            month,
                            year,
                            rs.getDouble("total_milk"),
                            rs.getDouble("morning_milk"),
                            rs.getDouble("evening_milk"),
                            rs.getDouble("avg_fat"),
                            rs.getInt("record_count")));
                }
            }
        }
        return stats;
    }
}

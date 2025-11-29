package com.example.dairy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.util.List;
import java.util.Random;

@Component
public class DataSeeder implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        // Ensure DB is initialized before we try to use it
        DatabaseConnection.initializeDatabase();

        DairyDAO dao = new DairyDAO();

        // Check if data already exists
        if (!dao.getAllCows().isEmpty()) {
            System.out.println("Database already contains data. Skipping seed.");
            return;
        }

        System.out.println("🌱 Seeding sample data...");

        // Add Sample Cows
        dao.addCow(new Cow(0, "Bessie", "Holstein", LocalDate.of(2019, 5, 12)));
        dao.addCow(new Cow(0, "Daisy", "Jersey", LocalDate.of(2020, 8, 23)));
        dao.addCow(new Cow(0, "Molly", "Guernsey", LocalDate.of(2021, 2, 15)));
        dao.addCow(new Cow(0, "Bella", "Holstein", LocalDate.of(2018, 11, 30)));
        dao.addCow(new Cow(0, "Luna", "Ayrshire", LocalDate.of(2022, 1, 10)));

        // Fetch the cows we just added to get their IDs
        List<Cow> cows = dao.getAllCows();

        // Add Production Records for the current month (up to today)
        LocalDate today = LocalDate.now();
        Random rand = new Random();

        // Generate data for the last 30 days
        LocalDate startDate = today.minusDays(29);

        for (LocalDate date = startDate; !date.isAfter(today); date = date.plusDays(1)) {
            for (Cow cow : cows) {
                // Morning Shift
                // Randomize quantity based on cow (some are better producers)
                double baseQty = 10 + (cow.getCowId() % 3) * 2;
                double qtyM = baseQty + rand.nextDouble() * 4; // Variance
                double fatM = 3.5 + rand.nextDouble();

                dao.recordProduction(new MilkProductionRecord(0, cow.getCowId(), date, "Morning",
                        Math.round(qtyM * 100.0) / 100.0,
                        Math.round(fatM * 100.0) / 100.0,
                        37.0, "Good"));

                // Evening Shift (usually slightly less)
                double qtyE = (baseQty * 0.8) + rand.nextDouble() * 3;
                double fatE = 3.8 + rand.nextDouble();

                dao.recordProduction(new MilkProductionRecord(0, cow.getCowId(), date, "Evening",
                        Math.round(qtyE * 100.0) / 100.0,
                        Math.round(fatE * 100.0) / 100.0,
                        37.5, "Good"));
            }
        }

        System.out.println("✅ Sample data seeded successfully!");
    }
}

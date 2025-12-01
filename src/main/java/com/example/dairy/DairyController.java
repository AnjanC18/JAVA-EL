package com.example.dairy;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

@Controller
public class DairyController {

    private final DairyDAO dao;

    public DairyController() {
        this.dao = new DairyDAO();
        // Ensure DB is initialized
        DatabaseConnection.initializeDatabase();
    }

    @GetMapping("/")
    public String home(@RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year,
            Model model) {
        if (month == null)
            month = LocalDate.now().getMonthValue();
        if (year == null)
            year = LocalDate.now().getYear();

        try {
            List<MonthlyStats> stats = dao.getMonthlyAnalysis(month, year);
            model.addAttribute("stats", stats);

            // Calculate Dashboard Metrics
            double totalProduction = stats.stream().mapToDouble(MonthlyStats::getTotalMilk).sum();
            double overallAvgFat = stats.stream().mapToDouble(s -> s.getAverageFat() * s.getTotalMilk()).sum()
                    / totalProduction;
            if (Double.isNaN(overallAvgFat))
                overallAvgFat = 0.0;

            MonthlyStats topCow = stats.stream()
                    .max((s1, s2) -> Double.compare(s1.getTotalMilk(), s2.getTotalMilk()))
                    .orElse(null);

            model.addAttribute("totalProduction", totalProduction);
            model.addAttribute("overallAvgFat", overallAvgFat);
            model.addAttribute("topCow", topCow);
            model.addAttribute("month", month);
            model.addAttribute("year", year);

        } catch (SQLException e) {
            model.addAttribute("error", "Error loading dashboard: " + e.getMessage());
        }
        return "index";
    }

    @GetMapping("/cows")
    public String viewCows(Model model) {
        try {
            List<Cow> cows = dao.getAllCows();
            model.addAttribute("cows", cows);
            model.addAttribute("newCow", new Cow(0, "", "", LocalDate.now()));
        } catch (SQLException e) {
            model.addAttribute("error", "Error fetching cows: " + e.getMessage());
        }
        return "cows";
    }

    @PostMapping("/cows/add")
    public String addCow(@ModelAttribute Cow cow) {
        try {
            dao.addCow(cow);
        } catch (SQLException e) {
            return "redirect:/cows?error=" + e.getMessage();
        }
        return "redirect:/cows";
    }

    @PostMapping("/cows/delete/{id}")
    public String deleteCow(@PathVariable int id) {
        try {
            dao.deleteCow(id);
        } catch (SQLException e) {
            return "redirect:/cows?error=" + e.getMessage();
        }
        return "redirect:/cows";
    }

    @GetMapping("/production")
    public String viewProduction(@RequestParam(required = false) String date, Model model) {
        LocalDate viewDate = (date == null || date.isEmpty()) ? LocalDate.now() : LocalDate.parse(date);
        try {
            List<MilkProductionRecord> records = dao.getDailyProduction(viewDate);
            model.addAttribute("records", records);
            model.addAttribute("viewDate", viewDate);
            model.addAttribute("cows", dao.getAllCows()); // For the dropdown
        } catch (SQLException e) {
            model.addAttribute("error", "Error fetching data: " + e.getMessage());
        }
        return "production";
    }

    @PostMapping("/production/add")
    public String recordProduction(
            @RequestParam int cowId,
            @RequestParam String date,
            @RequestParam String shift,
            @RequestParam double quantity,
            @RequestParam double fatContent,
            @RequestParam double temperature) {

        try {
            String quality = calculateQuality(fatContent, temperature);
            MilkProductionRecord record = new MilkProductionRecord(0, cowId, LocalDate.parse(date), shift, quantity,
                    fatContent, temperature, quality);
            dao.recordProduction(record);
        } catch (SQLException e) {
            return "redirect:/production?error=" + e.getMessage();
        }
        return "redirect:/production?date=" + date;
    }

    @GetMapping("/analysis")
    public String viewAnalysis(@RequestParam(required = false) Integer month,
            @RequestParam(required = false) Integer year,
            Model model) {
        if (month == null)
            month = LocalDate.now().getMonthValue();
        if (year == null)
            year = LocalDate.now().getYear();

        try {
            List<MonthlyStats> stats = dao.getMonthlyAnalysis(month, year);
            model.addAttribute("stats", stats);
            model.addAttribute("month", month);
            model.addAttribute("year", year);
        } catch (SQLException e) {
            model.addAttribute("error", "Error generating report: " + e.getMessage());
        }
        return "analysis";
    }

    private String calculateQuality(double fat, double temp) {
        if (fat >= 4.0 && temp <= 7.0)
            return "Good";
        if (fat >= 3.5)
            return "Average";
        return "Poor";
    }
}

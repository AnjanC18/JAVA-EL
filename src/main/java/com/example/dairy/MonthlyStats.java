package com.example.dairy;

public class MonthlyStats {
    private int cowId;
    private String cowName;
    private int month;
    private int year;
    private double totalMilk;
    private double morningMilk;
    private double eveningMilk;
    private double averageFat;
    private int recordCount;

    public MonthlyStats(int cowId, String cowName, int month, int year, double totalMilk,
            double morningMilk, double eveningMilk, double averageFat, int recordCount) {
        this.cowId = cowId;
        this.cowName = cowName;
        this.month = month;
        this.year = year;
        this.totalMilk = totalMilk;
        this.morningMilk = morningMilk;
        this.eveningMilk = eveningMilk;
        this.averageFat = averageFat;
        this.recordCount = recordCount;
    }

    // Getters
    public int getCowId() {
        return cowId;
    }

    public String getCowName() {
        return cowName;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public double getTotalMilk() {
        return totalMilk;
    }

    public double getMorningMilk() {
        return morningMilk;
    }

    public double getEveningMilk() {
        return eveningMilk;
    }

    public double getAverageFat() {
        return averageFat;
    }

    public int getRecordCount() {
        return recordCount;
    }
}

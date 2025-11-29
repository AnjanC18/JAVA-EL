package com.example.dairy;

import java.time.LocalDate;

public class MilkProductionRecord {
    private int recordId;
    private int cowId;
    private LocalDate productionDate;
    private String shift;
    private double quantity;
    private double fatContent;
    private double temperature;
    private String quality;

    public MilkProductionRecord(int recordId, int cowId, LocalDate productionDate, String shift,
            double quantity, double fatContent, double temperature, String quality) {
        this.recordId = recordId;
        this.cowId = cowId;
        this.productionDate = productionDate;
        this.shift = shift;
        this.quantity = quantity;
        this.fatContent = fatContent;
        this.temperature = temperature;
        this.quality = quality;
    }

    // Getters
    public int getRecordId() {
        return recordId;
    }

    public int getCowId() {
        return cowId;
    }

    public LocalDate getProductionDate() {
        return productionDate;
    }

    public String getShift() {
        return shift;
    }

    public double getQuantity() {
        return quantity;
    }

    public double getFatContent() {
        return fatContent;
    }

    public double getTemperature() {
        return temperature;
    }

    public String getQuality() {
        return quality;
    }

    @Override
    public String toString() {
        return String.format("Date: %s | Shift: %s | Qty: %.2fL | Fat: %.1f%% | Quality: %s",
                productionDate, shift, quantity, fatContent, quality);
    }
}

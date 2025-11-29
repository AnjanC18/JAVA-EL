package com.example.dairy;

import java.time.LocalDate;

public class Cow {
    private int cowId;
    private String name;
    private String breed;
    private LocalDate dateOfBirth;

    public Cow() {
    }

    public Cow(int cowId, String name, String breed, LocalDate dateOfBirth) {
        this.cowId = cowId;
        this.name = name;
        this.breed = breed;
        this.dateOfBirth = dateOfBirth;
    }

    // Getters and Setters
    public int getCowId() {
        return cowId;
    }

    public void setCowId(int cowId) {
        this.cowId = cowId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBreed() {
        return breed;
    }

    public void setBreed(String breed) {
        this.breed = breed;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return String.format("ID: %d | Name: %s | Breed: %s",
                cowId, name, breed);
    }
}

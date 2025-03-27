package com.example.carnest;

public class CarModel {
    private String brand;
    private String title;
    private String transmission;
    private String fuelType;
    private double price;
    private int kilometers;
    private double rating;
    private boolean isFavorite;
    private int imageResId;

    public CarModel(String brand, String title, String transmission, String fuelType, double price,
                    int kilometers, double rating, boolean isFavorite, int imageResId) {
        this.brand = brand;
        this.title = title;
        this.transmission = transmission;
        this.fuelType = fuelType;
        this.price = price;
        this.kilometers = kilometers;
        this.rating = rating;
        this.isFavorite = isFavorite;
        this.imageResId = imageResId;
    }

    public String getBrand() { return brand; }
    public String getTitle() { return title; }
    public String getTransmission() { return transmission; }
    public String getFuelType() { return fuelType; }
    public double getPrice() { return price; }
    public int getKilometers() { return kilometers; }
    public double getRating() { return rating; }
    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
    public int getImageResId() { return imageResId; }

    public String getDetails() {
        return transmission + " | " + fuelType + " | " + kilometers + " km | ⭐ " + rating;
    }
}

package com.restassured.basics;

public class Car {

    private String plateNumber;
    private String colour;
    private String brand;


    void setPlateNumber(String plateNumber) {
        this.plateNumber = plateNumber;
    }

    public String getPlateNumber() {
        return plateNumber;
    }

    void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrand() {
        return brand;
    }

    void setColour(String colour) {
        this.colour = colour;
    }

    public String getColour() {
        return colour;
    }
}

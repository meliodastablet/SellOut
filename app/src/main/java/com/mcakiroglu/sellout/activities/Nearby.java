package com.mcakiroglu.sellout.activities;

import java.io.Serializable;

public class Nearby implements Serializable {

    private String city;
    private String image1;
    private double lat;
    private double lon;
    private String name;
    private double price;

    public  Nearby(){}
    public Nearby(String city, String image1, double lat, double lon, String name, double price) {
        this.city = city;
        this.image1 = image1;
        this.lat = lat;
        this.lon = lon;
        this.name = name;
        this.price = price;
    }

    public String getCity() {
        return city;
    }

    public String getImage1() {
        return image1;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return "Nearby{" +
                "city='" + city + '\'' +
                ", image1='" + image1 + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}

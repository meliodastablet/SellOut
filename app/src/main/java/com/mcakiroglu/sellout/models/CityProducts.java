package com.mcakiroglu.sellout.models;

public class CityProducts {
    String city;
    double lat;
    double lon;
    String name;
    String price;
    String image1;

    public CityProducts(String city, double lat, double lon, String name, String price, String image1) {
        this.city = city;
        this.lat = lat;
        this.lon = lon;
        this.name = name;
        this.price = price;
        this.image1 = image1;
    }

    public CityProducts() {
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }
}

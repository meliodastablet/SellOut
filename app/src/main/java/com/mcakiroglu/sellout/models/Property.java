package com.mcakiroglu.sellout.models;

import java.io.Serializable;

public class Property implements Serializable {

private String adress;
private String name;
private String status;
private double price;
private String date;
private String comment;
private String counter;
private String image1, image2, image3, image4;
private String category;
private String toID;

    public Property(String adress, String name, String status, double price, String date, String comment, String counter, String i, String i2, String i3, String i4, String category,String id) {
        this.adress = adress;
        this.name = name;
        this.status = status;
        this.price = price;
        this.date = date;
        this.comment = comment;
        this.counter = counter;
        this.image1 = i;
        this.image2 = i2;
        this.image3 = i3;
        this.image4 = i4;
        this.category=category;
        this.toID = id;
    }

    public Property() {
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCounter() {
        return counter;
    }

    public void setCounter(String counter) {
        this.counter = counter;
    }

    public String getImage1() {
        return image1;
    }

    public void setImage1(String image1) {
        this.image1 = image1;
    }

    public String getImage2() {
        return image2;
    }

    public void setImage2(String image2) {
        this.image2 = image2;
    }

    public String getImage3() {
        return image3;
    }

    public void setImage3(String image3) {
        this.image3 = image3;
    }

    public String getImage4() {
        return image4;
    }

    public void setImage4(String image4) {
        this.image4 = image4;
    }

    public String getToID() {
        return toID;
    }

    public void setToID(String toID) {
        this.toID = toID;
    }

    @Override
    public String toString() {
        return "Property{" +
                "adress='" + adress + '\'' +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", price=" + price +
                ", date='" + date + '\'' +
                ", comment='" + comment + '\'' +
                ", counter='" + counter + '\'' +
                ", image1='" + image1 + '\'' +
                ", image2='" + image2 + '\'' +
                ", image3='" + image3 + '\'' +
                ", image4='" + image4 + '\'' +
                ", category='" + category + '\'' +
                ", id='" + toID + '\'' +
                '}';
    }
}

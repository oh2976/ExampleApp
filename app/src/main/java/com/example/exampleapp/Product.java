package com.example.exampleapp;

import java.io.Serializable;

public class Product implements Serializable {
    private String img;
    private String name;
    private int price;

    private String longimg;

    private int pId;

    public String getLongimg() {
        return longimg;
    }

    public void setLongimg(String longimg) {
        this.longimg = longimg;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    private String description;

    private int stock;

    public Product(){
    }

    public String getImg(){
        return img;
    }

    public void setImg(String img){
        this.img = img;
    }

    public String getName(){
        return name;
    }

    public void setName(String name){
        this.name = name;
    }
    public int getPrice(){
        return price;
    }

    public void setPrice(int price){
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

}

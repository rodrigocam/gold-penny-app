package com.redcode.goldpenny.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Product {
    @PrimaryKey
    private int id;

    private int apiId;

    private String name;
    private double price;
    private int quantity;
    public Product() {
        super();
    }

    @Ignore
    public Product(int id, String name, Double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    @Override
    public String toString(){
        return  "id:"+id + ". "+quantity + " " + name + " " + price + " API_id:" + apiId;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getApiId() {
        return apiId;
    }

    public void setApiId(int api_id) {
        this.apiId = api_id;
    }

    public Product clone(){
        Product product = new Product(id,name,price,quantity);
        product.setApiId(apiId);
        return product;
    }
}

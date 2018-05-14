package com.code.red.playvendas.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Product {
    @PrimaryKey
    private int id;
    private String name;
    private double price;
}

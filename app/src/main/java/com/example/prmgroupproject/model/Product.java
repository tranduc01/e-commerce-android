package com.example.prmgroupproject.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

@Entity
public class Product implements Serializable {

    @SerializedName("id")
    @PrimaryKey(autoGenerate = true)
    private int id;

    @SerializedName("name")
    @ColumnInfo(name = "name")
    private String name;

    @SerializedName("price")
    @ColumnInfo(name = "price")
    private Double price;

    @SerializedName("image")
    @ColumnInfo(name = "image")
    private String image;

    @SerializedName("quantity")
    @ColumnInfo(name = "quantity")
    private int quantity;

    @SerializedName("description")
    private String description;

    public Product() {
    }

    public Product(int id, String name, Double price, String image, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

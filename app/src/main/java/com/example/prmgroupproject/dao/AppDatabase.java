package com.example.prmgroupproject.dao;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.prmgroupproject.model.Product;

@Database(entities = {Product.class},version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ProductDAO productDAO();
}

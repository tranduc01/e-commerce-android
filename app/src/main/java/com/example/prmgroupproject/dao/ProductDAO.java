package com.example.prmgroupproject.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.prmgroupproject.model.Product;

import java.util.List;
@Dao
public interface ProductDAO {

    @Insert
    void addToCart(Product product);

    @Query("SELECT * FROM Product")
    List<Product> getAll();

    @Query("SELECT * FROM Product WHERE name=:name")
    Product getByName(String name);

    @Query("DELETE FROM Product WHERE id=:id")
    void deleteById(int id);

    @Query("SELECT EXISTS(SELECT * FROM Product WHERE name=:name)")
    Boolean is_exist(String name);

    @Query("UPDATE Product set quantity=:quantity WHERE name=:name")
    void updateQuantity(String name,int quantity);
}

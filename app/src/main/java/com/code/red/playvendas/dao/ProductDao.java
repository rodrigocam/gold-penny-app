package com.code.red.playvendas.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.code.red.playvendas.model.Product;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface ProductDao {
    @Insert(onConflict = REPLACE)
    void save(Product product);

    @Query("SELECT * FROM product WHERE id = :productId")
    LiveData<Product> load(int productId);

    @Query("SELECT * FROM product")
    List<Product> loadAll();
}
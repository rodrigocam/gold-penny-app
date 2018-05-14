package com.code.red.playvendas.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.code.red.playvendas.dao.ProductDao;
import com.code.red.playvendas.model.Product;

@Database(entities={Product.class}, version=1)
public abstract class playdb extends RoomDatabase {
    public abstract ProductDao productDao();
}

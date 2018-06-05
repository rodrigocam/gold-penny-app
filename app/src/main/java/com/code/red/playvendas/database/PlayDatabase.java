package com.code.red.playvendas.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.code.red.playvendas.dao.ProductDao;
import com.code.red.playvendas.dao.TokenDao;
import com.code.red.playvendas.model.Product;
import com.code.red.playvendas.model.Token;

@Database(entities = {Product.class, Token.class}, version = 1)
public abstract class PlayDatabase extends RoomDatabase {
    public abstract ProductDao productDao();

    public abstract TokenDao tokenDao();
}

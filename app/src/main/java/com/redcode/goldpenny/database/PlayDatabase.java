package com.redcode.goldpenny.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.redcode.goldpenny.dao.ProductDao;
import com.redcode.goldpenny.dao.TokenDao;
import com.redcode.goldpenny.model.Product;
import com.redcode.goldpenny.model.Token;

@Database(entities = {Product.class, Token.class}, version = 1)
public abstract class PlayDatabase extends RoomDatabase {
    public abstract ProductDao productDao();

    public abstract TokenDao tokenDao();
}

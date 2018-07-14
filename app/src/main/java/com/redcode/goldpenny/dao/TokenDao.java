package com.redcode.goldpenny.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.redcode.goldpenny.model.Token;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface TokenDao {
    @Insert(onConflict = REPLACE)
    void save(Token token);

    @Query("SELECT * FROM token ORDER BY id DESC LIMIT 1 ")
    LiveData<Token> load();
}
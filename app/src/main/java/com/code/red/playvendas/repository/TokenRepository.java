package com.code.red.playvendas.repository;

import android.arch.lifecycle.LiveData;
import com.code.red.playvendas.dao.TokenDao;
import com.code.red.playvendas.model.Token;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TokenRepository {
    private final TokenDao tokenDao;

    @Inject
    public TokenRepository(TokenDao tokenDao) {
        this.tokenDao = tokenDao;
    }

    public LiveData<Token> getToken() {
        // return a LiveData token directly from the database.
        return tokenDao.load();
    }

    public void saveToken(Token token){
        tokenDao.save(token);
    }
}
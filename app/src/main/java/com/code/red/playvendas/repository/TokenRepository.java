package com.code.red.playvendas.repository;

import android.arch.lifecycle.LiveData;
import com.code.red.playvendas.dao.TokenDao;
import com.code.red.playvendas.model.Token;

import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class TokenRepository {
    private final TokenDao tokenDao;
    private final Executor executor;
    @Inject
    public TokenRepository(TokenDao tokenDao, Executor executor) {
        this.tokenDao = tokenDao;
        this.executor = executor;
    }

    public LiveData<Token> getToken() {
        // return a LiveData token directly from the database.
        return tokenDao.load();
    }

    public void saveToken(Token token){
        executor.execute(()->{
            tokenDao.save(token);
        });
    }
}
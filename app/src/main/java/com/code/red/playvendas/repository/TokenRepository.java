package com.code.red.playvendas.repository;

import android.arch.lifecycle.LiveData;

import com.code.red.playvendas.dao.ProductDao;
import com.code.red.playvendas.dao.TokenDao;
import com.code.red.playvendas.model.Product;
import com.code.red.playvendas.model.Token;
import com.code.red.playvendas.utils.Webservice;

import java.io.IOException;
import java.util.concurrent.Executor;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit2.Response;


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
package com.code.red.playvendas.viewmodel;

import android.app.Application;
import android.arch.persistence.room.Room;

import com.code.red.playvendas.dao.ProductDao;
import com.code.red.playvendas.dao.TokenDao;
import com.code.red.playvendas.database.PlayDatabase;
import com.code.red.playvendas.repository.ProductRepository;
import com.code.red.playvendas.repository.TokenRepository;
import com.code.red.playvendas.utils.Webservice;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
public class AppModule {

    // --- DATABASE INJECTION ---

    @Provides
    @Singleton
    PlayDatabase provideDatabase(Application application) {
        return Room.databaseBuilder(application,
                PlayDatabase.class, "PlayDatabase.db")
                .build();
    }

    @Provides
    @Singleton
    TokenDao provideTokenDao(PlayDatabase database) {
        return database.tokenDao();
    }
    @Provides
    @Singleton
    ProductDao provideProductDao(PlayDatabase database) {
        return database.productDao();
    }
    // --- REPOSITORY INJECTION ---

    @Provides
    Executor provideExecutor() {
        return Executors.newSingleThreadExecutor();
    }

    @Provides
    @Singleton
    TokenRepository provideTokenRepository(Webservice webservice, TokenDao tokenDao, Executor executor) {
        return new TokenRepository(tokenDao, executor);
    }

    @Provides
    @Singleton
    ProductRepository provideProductRepository(Webservice webservice, ProductDao productDao, Executor executor) {
        return new ProductRepository(webservice,productDao, executor);
    }

    // --- NETWORK INJECTION ---

    private static String BASE_URL = "https://api.github.com/";

    @Provides
    Gson provideGson() { return new GsonBuilder().create(); }

    @Provides
    Retrofit provideRetrofit(Gson gson) {
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .baseUrl(BASE_URL)
                .build();
        return retrofit;
    }

    @Provides
    @Singleton
    Webservice provideApiWebservice(Retrofit restAdapter) {
        return restAdapter.create(Webservice.class);
    }
}
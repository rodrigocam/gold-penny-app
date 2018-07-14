package com.redcode.goldpenny.viewmodel;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.redcode.goldpenny.model.Token;
import com.redcode.goldpenny.repository.TokenRepository;

import javax.inject.Inject;

public class TokenViewModel extends ViewModel {
    public TokenRepository tokenRepository;
    public LiveData<Token> token;

    @Inject
    public TokenViewModel(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public void init() {
        if (token == null) {
            token = tokenRepository.getToken();
        }
    }

    public LiveData<Token> getToken() {
        return token;
    }

    public void saveToken() {
        if (token != null) {
            tokenRepository.saveToken(token.getValue());
        }
    }

    public void saveToken(Token token) {
        tokenRepository.saveToken(token);
        this.token = tokenRepository.getToken();
    }
}

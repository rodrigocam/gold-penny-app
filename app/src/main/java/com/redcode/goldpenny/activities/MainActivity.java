package com.redcode.goldpenny.activities;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.redcode.goldpenny.R;
import com.redcode.goldpenny.model.Token;
import com.redcode.goldpenny.utils.RequestBuilder;
import com.redcode.goldpenny.viewmodel.TokenViewModel;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity {
    private Button login = null;
    private EditText username = null;
    private EditText password = null;
    private StringRequest stringRequest = null;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private TokenViewModel tokenViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Activate dependency injection for ViewModels
        this.configureDagger();

        login = findViewById(R.id.loginButton);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        tokenViewModel = ViewModelProviders.of(this, viewModelFactory).get(TokenViewModel.class);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stringRequest == null) {
                    requestToken();
                }
            }
        });
    }

    public void requestToken(){
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = RequestBuilder.getTokenRequest(this, username.getText().toString(), password.getText().toString());
        queue.add(stringRequest);
    }

    public void createToken(String token) {
            try {
                Token newToken = new Token();
                newToken.setToken(token);
                tokenViewModel.saveToken(newToken);
                startActivity(new Intent(this, DisplayProductsActivity.class));
            } catch (Exception e) {
                e.printStackTrace();
                //TODO Toast error on token.
            }
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }
}
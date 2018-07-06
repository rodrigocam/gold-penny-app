package com.code.red.playvendas.activities;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.code.red.playvendas.R;
import com.code.red.playvendas.model.Token;
import com.code.red.playvendas.utils.RequestBuilder;
import com.code.red.playvendas.viewmodel.TokenViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
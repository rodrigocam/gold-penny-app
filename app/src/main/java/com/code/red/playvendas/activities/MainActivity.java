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


        RequestQueue queue = Volley.newRequestQueue(this);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stringRequest == null) {
                    StringRequest stringRequest = getTokenRequest(v.getContext(), username.getText().toString(), password.getText().toString());
                    queue.add(stringRequest);
                }
            }
        });
    }

    public StringRequest getTokenRequest(Context context, String username, String password) {
        String url = "http://159.89.140.211/get-token/";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try{
                            JSONObject responseJson = new JSONObject(response);
                            String token = responseJson.getString("token");
                            createToken(token);
                        }catch(JSONException e){
                            e.printStackTrace();
                            Log.e("Response", "get-token api response was not a json");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override

                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(context,
                                    "No conection, Timeout",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(context,
                                    "Usuário ou senha inválidos.",
                                    Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(context,
                                    "Server Error",
                                    Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(context,
                                    "Network error",
                                    Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(context,
                                    "Parse error",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("username", username);
                params.put("password", password);
                return params;
            }
        };
        return postRequest;
    }

    private void createToken(String token) {
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
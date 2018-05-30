package com.code.red.playvendas.activities;


import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.code.red.playvendas.R;
import com.code.red.playvendas.model.Token;
import com.code.red.playvendas.viewmodel.TokenViewModel;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class MainActivity extends AppCompatActivity {
    private Button sendToken = null;
    private EditText token = null;
    private StringRequest stringRequest = null;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private TokenViewModel tokenViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.configureDagger();

        sendToken = findViewById(R.id.sendToken);
        token = findViewById(R.id.token);

        tokenViewModel = ViewModelProviders.of(this, viewModelFactory).get(TokenViewModel.class);


        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://206.189.123.66/api/v1/products";


        sendToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stringRequest == null){
                    StringRequest stringRequest = getRequest(url, token.getText().toString());
                    queue.add(stringRequest);
                    //createToken("[{id:0}]","Token 0a056e99fd9310bab4a99fcc4a8d227db01a91c6");
                }
            }
        });
    }

    public StringRequest getRequest(String url, String token){
        // TODO: param token should be used here instead of hardcoded token
        String request_token = "b54556ba791a2cd0c65349ddf16969de83bb7fc9";

        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        createToken(response, request_token);
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener()
                {
                    @Override

                    public void onErrorResponse(VolleyError error) {
                        // TODO Toast informing error
                        stringRequest = null;
                        //
                        Log.d("ERROR","error => "+error.toString());
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "Token "+ request_token);
                return params;
            }
        };

        return getRequest;
    }

    private void createToken(String response, String token) {
        try {
            JSONArray teste = new JSONArray(response);
            try{
                teste.getJSONObject(0).getInt("id");
                Token newToken = new Token();
                newToken.setId(0);
                newToken.setToken(token);
                tokenViewModel.saveToken(newToken);
                startActivity(new Intent(this, DisplayProductsActivity.class));
            }catch (Exception e){
                e.printStackTrace();
                //TODO Toast error on token.
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void configureDagger(){
        AndroidInjection.inject(this);
    }
}
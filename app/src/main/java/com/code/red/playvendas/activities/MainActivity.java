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
import android.widget.TextView;

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
import com.code.red.playvendas.viewmodel.ViewModelFactory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    private Button sendToken = null;
    private EditText token = null;
    private StringRequest stringRequest = null;
//    @Inject
//    ViewModelFactory viewModelFactory;
    private TokenViewModel tokenViewModel = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        sendToken = findViewById(R.id.sendToken);
        token = findViewById(R.id.token);
//        tokenViewModel = ViewModelProviders.of(this,viewModelFactory).get(TokenViewModel.class);
        tokenViewModel = ViewModelProviders.of(this).get(TokenViewModel.class);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://206.189.123.66/api/v1/products";


        sendToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stringRequest == null){
                    StringRequest stringRequest = getRequest(url, token.getText().toString());
                    //queue.add(stringRequest);
                    createToken("[{id:0}]","Token 0a056e99fd9310bab4a99fcc4a8d227db01a91c6");
                }
            }
        });
    }

    public StringRequest getRequest(String url, String token){
        StringRequest getRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        createToken(response, token);
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
                // TODO: param token should be used here instead of hardcoded token
                params.put("Authorization", "Token 0a056e99fd9310bab4a99fcc4a8d227db01a91c6");
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
                startActivity(new Intent(getApplicationContext(), DisplayProductsActivity.class));
            }catch (Exception e){
                e.printStackTrace();
                //TODO Toast error on token.
            }
//                            Log.d("first", ""+teste.getJSONObject(0).getDouble("price"));
//                            Log.d("first", ""+teste.getJSONObject(0).getString("name"));    Log.d("first", ""+teste.getJSONObject(0).getDouble("price"));
//                            Log.d("first", ""+teste.getJSONObject(0).getInt("id"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

package com.code.red.playvendas.activities;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;

public class TokenFragment extends Fragment {

    // FOR DATA
    private Button sendToken = null;
    private EditText token = null;
    private StringRequest stringRequest = null;

    @Inject
    ViewModelProvider.Factory viewModelFactory;
    private TokenViewModel tokenViewModel;

    public TokenFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.token_fragment, container, false);

        sendToken = view.findViewById(R.id.sendToken);
        token = view.findViewById(R.id.token);


        RequestQueue queue = Volley.newRequestQueue(this.getContext());
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
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        this.configureDagger();
        this.configureViewModel();
    }

    // -----------------
    // CONFIGURATION
    // -----------------

    private void configureDagger(){
        AndroidSupportInjection.inject(this);
    }

    private void configureViewModel(){
        tokenViewModel = ViewModelProviders.of(this, viewModelFactory).get(TokenViewModel.class);
        //tokenViewModel.getToken().observe(this, user -> updateUI(token));
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
                startActivity(new Intent(this.getContext(), DisplayProductsActivity.class));
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
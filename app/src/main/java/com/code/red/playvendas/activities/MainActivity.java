package com.code.red.playvendas.activities;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private Button sendToken = null;
    private EditText token = null;
    private StringRequest stringRequest = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sendToken = findViewById(R.id.sendToken);
        token = findViewById(R.id.token);

        RequestQueue queue = Volley.newRequestQueue(this);
        String url ="http://206.189.123.66/api/v1/products";


        sendToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(stringRequest == null){
                    StringRequest stringRequest = getRequest(url, token.getText().toString());
                    queue.add(stringRequest);
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
                        try {
                            JSONArray teste = new JSONArray(response);
                            //startActivity(new Intent(getApplicationContext(), DisplayProductsActivity.class));
//                            Log.d("first", ""+teste.getJSONObject(0).getDouble("price"));
//                            Log.d("first", ""+teste.getJSONObject(0).getString("name"));    Log.d("first", ""+teste.getJSONObject(0).getDouble("price"));
//                            Log.d("first", ""+teste.getJSONObject(0).getInt("id"));
                            // TODO: save products on db and start the new activity
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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
}

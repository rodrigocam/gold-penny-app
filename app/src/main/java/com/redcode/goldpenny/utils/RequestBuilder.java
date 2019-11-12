package com.redcode.goldpenny.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.redcode.goldpenny.activities.MainActivity;
import com.redcode.goldpenny.model.Product;
import com.redcode.goldpenny.model.Token;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RequestBuilder {
    private Token token;

    // Constant with server IP for requests. Used here and in Webservice
    public static final String SERVER_URL ="http://157.245.218.107/";

    public RequestBuilder(Token token){
        this.token = token;
    }

    /**
     * Creates a Volley string request to post a list of sold products,
     * representing a sale, after printing tickets.
     * @param context Context to create Toasts in case of errors.
     * @param products List of products that have been sold.
     * @return a StringRequest to be used in Volley Queue
     */
    public StringRequest postProductsRequest(Context context, ArrayList<Product> products) {
        String url = SERVER_URL + "api/v1/products/sell/";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("API","Successfully sold products, request success.");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("API", "Error sending products to API, request failed.");
                        Log.e("API", error.toString());
                    }
                }) {

            @Override
            public String getBodyContentType(){
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                JSONArray jsonProducts = new JSONArray();
                for(Product product: products){
                    jsonProducts.put(getJsonObject(product));
                }
                Log.d("JsonProducts", jsonProducts.toString());
                return getBytes(jsonProducts);
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("Authorization", "Token " + token.getToken());
                params.put("Content-Type", "application/json");

                Log.d("Token", token.getToken());
                Log.d("Headers",params.toString());
                return params;
            }
        };
        return postRequest;
    }

    /**
     * Convert a Product into JSONObject to be send to the API.
     * API only need the id and amount of each product sold.
     * @param product Product to be converted
     * @return Product as JSON
     */
    @NonNull
    private JSONObject getJsonObject(Product product) {
        JSONObject productJSON = new JSONObject();
        try {
            productJSON.put("id", product.getApiId());
            productJSON.put("amount", product.getQuantity());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return productJSON;
    }

    /**
     * Request body is a byte[], this method converts a JSONArray of JsonProducts into bytearray.
     * @param jsonProducts JSONArray made of JSONobjects with products made with getJsonObject.
     * @return bytearray codified in utf-8 or null in case of exceptions.
     */
    private byte[] getBytes(JSONArray jsonProducts) {
        JSONObject orders = new JSONObject();
        try {
            orders.put("orders", jsonProducts);
            return (orders.toString()).getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        } catch (JSONException e){
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Creates a Volley String Request to obtain an authentication token
     * using a registered username and password in our API.
     * This token needs to be saved for further requests of available products and sales.
     * @param activity MainActivity needs to receive back the token after response.
     * @param username a Registered API username.
     * @param password a Registered API password.
     * @return StringRequest to be used in Volley Queue
     */
    public static StringRequest getTokenRequest(MainActivity activity, String username, String password) {
        String url = SERVER_URL + "get-token/";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Response", response);
                        try{
                            JSONObject responseJson = new JSONObject(response);
                            String token = responseJson.getString("token");
                            activity.createToken(token);
                        }catch(JSONException e){
                            e.printStackTrace();
                            Log.e("Response", "/get-token/ API response was not a json");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override

                    public void onErrorResponse(VolleyError error) {
                        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                            Toast.makeText(activity.getApplicationContext(),
                                    "Sem Conexão, Timeout.",
                                    Toast.LENGTH_LONG).show();
                        } else if (error instanceof AuthFailureError) {
                            Toast.makeText(activity.getApplicationContext(),
                                    "Authentication failure",
                                    Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ServerError) {
                            Toast.makeText(activity.getApplicationContext(),
                                    "Usuário ou senha inválidos.",
                                    Toast.LENGTH_SHORT).show();
                        } else if (error instanceof NetworkError) {
                            Toast.makeText(activity.getApplicationContext(),
                                    "Erro de conexão.",
                                    Toast.LENGTH_SHORT).show();
                        } else if (error instanceof ParseError) {
                            Toast.makeText(activity.getApplicationContext(),
                                    "Erro de Parsing da resposta.",
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

}

package com.code.red.playvendas.activities;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.code.red.playvendas.R;
import com.code.red.playvendas.model.Product;
import com.code.red.playvendas.model.Token;
import com.code.red.playvendas.viewmodel.ProductViewModel;
import com.code.red.playvendas.viewmodel.TokenViewModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.BindsOptionalOf;
import dagger.android.AndroidInjection;

public class DisplayProductsActivity extends AppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private ProductViewModel productViewModel = null;
    private TokenViewModel tokenViewModel = null;

    private Button printBtn;
    private RecyclerView productList;
    private TextView total;
    private PrinterManager printerManager;
    private Token token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_products);
        total = (TextView) findViewById(R.id.total);

        this.configureDagger();

        printerManager = new PrinterManager(this);

        RequestQueue queue = Volley.newRequestQueue(this);

        productList = (RecyclerView) findViewById(R.id.product_list);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        productList.setLayoutManager(manager);

        tokenViewModel = ViewModelProviders.of(this, viewModelFactory).get(TokenViewModel.class);
        tokenViewModel.init();
        productViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductViewModel.class);

        setUpPrintButton(queue);
        tokenViewModel.getToken().observe(this, token -> {
            this.token = token;
            productViewModel.init(token);
            refreshProducts(productList);
        });
    }

    private void refreshProducts(RecyclerView productList) {
        productViewModel.getProducts().observe(this, products -> {
            refreshProductList(productList, products);
            updateTotalPrice();
        });
    }

    public void updateTotalPrice() {
        double totalPrice = 0;
        double price;
        for (Product product : getProductListHolders()) {
            if (product.getQuantity() >0 ) {
                totalPrice += product.getPrice() * product.getQuantity();
            }
        }
        this.total.setText("R$ " + totalPrice);
    }

    private void refreshProductList(RecyclerView productList, List<Product> products) {
        Log.d("Teste", products.toString());
        productList.setAdapter(new ProductListAdapter(this, this, products));
    }


    private ArrayList<Product> getProductListHolders() {
        ArrayList<Product> products = new ArrayList<Product>();
        ProductListAdapter.ViewHolder product = null;
        ProductListAdapter adapter = (ProductListAdapter) productList.getAdapter();
        for (int i = 0; i < productList.getChildCount(); i++) {
            products.add(adapter.getProduct(i,productList));
        }
        return products;
    }

    private ArrayList<Product> getSelectedProducts() {
        ArrayList<Product> products = new ArrayList<Product>();

        for (Product product : getProductListHolders()) {
            Log.d("Productcu", product.toString());
            if (product.getQuantity() > 0) {
                products.add(product);
            }
        }
        Log.d("HUE#", products.toString());
        return products;
    }

    private void setUpPrintButton(RequestQueue queue) {
        this.printBtn = findViewById(R.id.printBtn);
        this.printBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Product> products = getSelectedProducts();
                //printerManager.print(products);
                refreshProducts(productList);
                Log.d("RODRIGOLIXO", products.toString());
                StringRequest productsRequest = postProductsRequest(v.getContext(),products);
                Log.d("RODRIGOLIXO22222", productsRequest.toString());

                queue.add(productsRequest);
            }
        });
    }

    private void configureDagger() {
        AndroidInjection.inject(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        printerManager.start();
        updateTotalPrice();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        printerManager.destroy();
    }

    public StringRequest postProductsRequest(Context context, ArrayList<Product> products) {
        String url = "http://206.189.123.66/api/v1/products/sell/";
        StringRequest postRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("AUSHUSA","SADISIJD");
                        Log.d("Response", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override

                    public void onErrorResponse(VolleyError error) {
                        Log.e("ARGH", error.toString());
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
                    JSONObject productJSON = new JSONObject();
                    Log.d("CU", product.toString());
                    try {
                        productJSON.put("id", product.getApiId());
                        productJSON.put("amount", product.getQuantity());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    jsonProducts.put(productJSON);
                }
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
}

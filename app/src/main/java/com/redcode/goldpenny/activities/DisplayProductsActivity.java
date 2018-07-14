package com.redcode.goldpenny.activities;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.redcode.goldpenny.R;
import com.redcode.goldpenny.model.Product;
import com.redcode.goldpenny.utils.RequestBuilder;
import com.redcode.goldpenny.viewmodel.ProductViewModel;
import com.redcode.goldpenny.viewmodel.TokenViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class DisplayProductsActivity extends AppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    // ViewModels to access information on DB/API
    private ProductViewModel productViewModel = null;
    private TokenViewModel tokenViewModel = null;

    // Managers
    private RequestBuilder requestBuilder;
    private PrinterManager printerManager;

    // Layout items
    private TextView total;
    private Button printBtn;
    private RecyclerView productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_products);

        //Activate dependency injection
        this.configureDagger();

        //Printer Manager uses bluetooth and print products.
        printerManager = new PrinterManager(this);

        total = (TextView) findViewById(R.id.total);
        productList = (RecyclerView) findViewById(R.id.product_list);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        productList.setLayoutManager(manager);

        // Get View Models to access user token and retrieve products on the API
        tokenViewModel = ViewModelProviders.of(this, viewModelFactory).get(TokenViewModel.class);
        tokenViewModel.init();
        productViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductViewModel.class);

        // Define print button action on click to print
        setUpPrintButton();

        // Initialize product view model and products using user token
        tokenViewModel.getToken().observe(this, token -> {
            this.requestBuilder = new RequestBuilder(token);
            productViewModel.init(token);
            refreshProducts();
        });
    }

    /**
     * Refresh Products on Recycler view list based on products from view model.
     */
    private void refreshProducts() {
        productViewModel.getProducts().observe(this, products -> {
            productList.setAdapter(new ProductListAdapter(this,this, products));
            updateTotalPrice();
        });
    }

    /**
     * Update the total price shown on the screen
     * by adding all products prices on Recycler View adapter viewholders
     */
    public void updateTotalPrice() {
        double totalPrice = 0;
        ProductListAdapter adapter = (ProductListAdapter) productList.getAdapter();
        if(adapter != null){
            for (Product product : adapter.getProducts()) {
                if (product.getQuantity() > 0) {
                    totalPrice += product.getPrice() * product.getQuantity();
                }
            }
        }
        this.total.setText("R$ " + totalPrice);
    }


    /**
     * Get products by filtering RecyclerView products where quantity > 0;
     * These selected products can be considered a sale.
     *
     * @return ArrayList of products selected by the user;
     */
    private ArrayList<Product> getSelectedProducts() {
        ArrayList<Product> products = new ArrayList<Product>();
        ProductListAdapter adapter = (ProductListAdapter) productList.getAdapter();

        for (Product product : adapter.getProducts()) {
            if (product.getQuantity() > 0) {
                products.add(product.clone());
            }
        }

        Log.d("RecyclerView", "Selected Products: " + products.toString());
        return products;
    }

    /**
     * Set up button action to print selected products when clicked
     * After being pressed a sale is completed, API is called and products need to be reseted.
     */
    private void setUpPrintButton() {
        RequestQueue queue = Volley.newRequestQueue(this);

        this.printBtn = findViewById(R.id.printBtn);
        this.printBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Print all products in this sale.
                ArrayList<Product> products = getSelectedProducts();
                printerManager.print(products);

                Log.d("Sale", "Products: " + products.toString());


                // Sends the sale to the API
                StringRequest productsRequest = requestBuilder.postProductsRequest(v.getContext(), products);
                queue.add(productsRequest);
                // Reset all products quantity to 0
                refreshProducts();
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
}

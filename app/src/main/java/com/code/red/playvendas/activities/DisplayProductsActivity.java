package com.code.red.playvendas.activities;

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

import com.code.red.playvendas.R;
import com.code.red.playvendas.model.Product;
import com.code.red.playvendas.viewmodel.ProductViewModel;
import com.code.red.playvendas.viewmodel.TokenViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class DisplayProductsActivity extends AppCompatActivity {

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private ProductViewModel productViewModel = null;
    private TokenViewModel tokenViewModel = null;

    /* Buttons */
    private Button printBtn;
    private Button gerarTotal;

    private TextView total;
    private PrinterManager printerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_products);
        total = (TextView) findViewById(R.id.total);
        gerarTotal = (Button) findViewById(R.id.gerarTotal);
        gerarTotal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateTotalPrice();
            }
        });

        this.configureDagger();

        printerManager = new PrinterManager(this);

        RecyclerView productList = (RecyclerView) findViewById(R.id.product_list);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        productList.setLayoutManager(manager);

        tokenViewModel = ViewModelProviders.of(this, viewModelFactory).get(TokenViewModel.class);
        tokenViewModel.init();
        productViewModel = ViewModelProviders.of(this, viewModelFactory).get(ProductViewModel.class);

        setUpPrintButton();
        tokenViewModel.getToken().observe(this, token -> {
            productViewModel.init(token);
            productViewModel.getProducts().observe(this, products -> {
                refreshProductList(productList, products);
            });
        });
    }

    private void updateTotalPrice() {
        double totalPrice = 0;
        double price;
        for (ProductListAdapter.ViewHolder product : getProductListHolders()) {
            if (product.subtotal.getText().toString() != "") {
                price = Double.parseDouble(product.subtotal.getText().toString());
                totalPrice += price;
            }
        }
        this.total.setText("R$ " + totalPrice + "0");
    }

    private void refreshProductList(RecyclerView productList, List<Product> products) {
        productList.setAdapter(new ProductListAdapter(this, products.toArray(new Product[products.size()])));
    }


    private ArrayList<ProductListAdapter.ViewHolder> getProductListHolders() {
        RecyclerView productList = (RecyclerView) findViewById(R.id.product_list);
        ArrayList<ProductListAdapter.ViewHolder> listItems = new ArrayList<ProductListAdapter.ViewHolder>();
        ProductListAdapter.ViewHolder product = null;
        for (int i = 0; i < productList.getChildCount(); i++) {
            View child = productList.getChildAt(i);
            product = (ProductListAdapter.ViewHolder) productList.getChildViewHolder(child);
            listItems.add(product);
        }
        return listItems;
    }

    private ArrayList<Product> getSelectedProducts() {
        ArrayList<Product> products = new ArrayList<Product>();
        for (ProductListAdapter.ViewHolder product : getProductListHolders()) {
            Log.d("Product", product.toString());
            if (product.actualQuantity > 0) {
                String name = product.nameText.getText().toString();
                double price = Double.parseDouble(product.priceText.getText().toString());
                int quantity = product.actualQuantity;
                products.add(new Product(product.getAdapterPosition(), name, price, quantity));
            }
        }
        return products;
    }


    private void setUpPrintButton() {
        this.printBtn = findViewById(R.id.printBtn);
        this.printBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printerManager.print(getSelectedProducts());
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
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        printerManager.destroy();
    }
}

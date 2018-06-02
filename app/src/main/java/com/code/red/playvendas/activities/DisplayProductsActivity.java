package com.code.red.playvendas.activities;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.code.red.playvendas.R;
import com.code.red.playvendas.bluetooth.BluetoothService;
import com.code.red.playvendas.exceptions.BluetoothConnectionException;
import com.code.red.playvendas.exceptions.SendDataException;
import com.code.red.playvendas.model.Product;
import com.code.red.playvendas.utils.EscPosDriver.EscPosDriver;
import com.code.red.playvendas.viewmodel.ProductViewModel;
import com.code.red.playvendas.viewmodel.TokenViewModel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class DisplayProductsActivity extends AppCompatActivity {

    private BluetoothService btService = null;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

    private ProductViewModel productViewModel = null;
    private TokenViewModel tokenViewModel = null;

    /* Buttons */
    private Button printBtn;

    /* EscPos driver to parse xml template to esc/pos commands */
    private EscPosDriver escPosDriver;

    /* Data that will be printed on thermal printer */
    private byte [] printData;

    /* XML Template with the print template */
    private InputStream xmlFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_products);

        this.configureDagger();

        this.btService = new BluetoothService();

        /* We need this to open the xml template from res/raw folder */
        this.xmlFile = getResources().openRawResource(R.raw.print_template);
        this.escPosDriver = new EscPosDriver(this.xmlFile);
        /* Creating a esc/pos driver with the given xmlfile */

        RecyclerView productList = (RecyclerView) findViewById(R.id.product_list);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        productList.setLayoutManager(manager);

        tokenViewModel = ViewModelProviders.of(this, viewModelFactory).get(TokenViewModel.class);
        tokenViewModel.init();
        productViewModel = ViewModelProviders.of(this,viewModelFactory).get(ProductViewModel.class);


        setUpPrintButton();
        tokenViewModel.getToken().observe(this, token -> {
            productViewModel.init(token);
            productViewModel.getProducts().observe(this, products->{
                refreshProductList(productList, products);
            });
        });

    }

    private void refreshProductList(RecyclerView productList, List<Product> products) {
        productList.setAdapter(new ProductListAdapter(this, products.toArray(new Product[products.size()])));
    }

    private void print() {
        ArrayList<Product> selectedProducts = getSelectedProducts();

        for(Product product:selectedProducts){
            this.escPosDriver.setLineText("product", product.getName());
            this.escPosDriver.setLineText("price",  "R$ "+ product.getPrice() +"0");
            this.escPosDriver.setLineText("date", "Data: "+ new Date().toString());

            for(int i = 0; i < product.getQuantity(); i++){
                sendProductToPrint(this.escPosDriver.xmlToEsc());
            }
        }
    }

    private ArrayList<ProductListAdapter.ViewHolder> getProductListHolders(){
        RecyclerView productList = (RecyclerView) findViewById(R.id.product_list);
        ArrayList<ProductListAdapter.ViewHolder> listItems = new ArrayList<ProductListAdapter.ViewHolder>();
        ProductListAdapter.ViewHolder product = null;
        for(int i=0; i< productList.getChildCount();i++) {
            View child = productList.getChildAt(i);
            product = (ProductListAdapter.ViewHolder) productList.getChildViewHolder(child);
            listItems.add(product);
        }
        return listItems;
    }

    private ArrayList<Product> getSelectedProducts(){
        ArrayList<Product> products = new ArrayList<Product>();
        for(ProductListAdapter.ViewHolder product: getProductListHolders()){
            Log.d("Product", product.toString());
            if(product.actualQuantity > 0){
                String name = product.nameText.getText().toString();
                double price = Double.parseDouble(product.priceText.getText().toString());
                int quantity = product.actualQuantity;
                products.add(new Product(product.getAdapterPosition(),name,price,quantity));
            }
        }
        return products;
    }

    private void sendProductToPrint(byte[] productData){
        try{
            this.btService.sendByteData(productData);
        }catch(SendDataException e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Failed to print",
                            Toast.LENGTH_SHORT);
                }
    }

    private void setUpPrintButton(){
    this.printBtn = findViewById(R.id.printBtn);
        this.printBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                print();
            }
        });
    }

    private void configureDagger(){
        AndroidInjection.inject(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        try {
            this.btService.startConnection();
        }catch(BluetoothConnectionException e){
            /* alert dialog saying that
            bluetooth connection failed and return
            to previous activity */
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try{
            this.btService.closeConnection();
        }catch(BluetoothConnectionException e){
            Toast.makeText(this, "FAILED TO CLOSE BLUETOOTH CONNECTION", Toast.LENGTH_SHORT);
        }
    }
}

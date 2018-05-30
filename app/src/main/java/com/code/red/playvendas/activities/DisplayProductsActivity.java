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
import com.code.red.playvendas.exceptions.SendDataException;
import com.code.red.playvendas.model.Product;
import com.code.red.playvendas.utils.EscPosDriver.EscPosDriver;
import com.code.red.playvendas.viewmodel.TokenViewModel;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class DisplayProductsActivity extends AppCompatActivity {

    private BluetoothService btService = null;

    @Inject
    ViewModelProvider.Factory viewModelFactory;

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

        //this.btService = new BluetoothService();

        /* We need this to open the xml template from res/raw folder */
        //this.xmlFile = getResources().openRawResource(R.raw.print_template);

        RecyclerView productList = (RecyclerView) findViewById(R.id.product_list);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        productList.setLayoutManager(manager);

        tokenViewModel = ViewModelProviders.of(this, viewModelFactory).get(TokenViewModel.class);
        tokenViewModel.init();
        ArrayList<Product> products = new ArrayList<Product>();
        products.add(new Product(0,"HEINEKEN", 16.80));
        products.add(new Product(1,"Viagra", 22.50));
        products.add(new Product(2,"Água", 4.50));
        products.add(new Product(3,"Vinho Branco", 22.50));
        products.add(new Product(4,"Rodrigo Lixo", 1.0));

        refreshProductList(productList, products);

        products.add(new Product(5,"Rodrigo Lixo", 1.0));
        products.add(new Product(6,"Rodrigo Lixo", 1.0));
        products.add(new Product(7,"Rodrigo Lixo", 1.0));
        products.add(new Product(8,"Rodrigo Lixo", 1.0));

        //print_stuff();
        tokenViewModel.getToken().observe(this, token -> {
            Log.d("AAAA",token.getToken());
            Log.d("AAAA","AAAAA\n\n\n\n\n\n\n\n\n");
            products.set(0, new Product(0, token.getToken(), 10.0));
            refreshProductList(productList, products);
        });

    }

    private void refreshProductList(RecyclerView productList, ArrayList<Product> products) {
        productList.setAdapter(new ProductListAdapter(this, products.toArray(new Product[products.size()])));
    }

    private void print_stuff() {
        /* Creating a esc/pos driver with the given xmlfile */
        this.escPosDriver = new EscPosDriver(this.xmlFile);

        /* Customizing the text of some lines */
        this.escPosDriver.setLineText("product", "HEINEKEN");
        this.escPosDriver.setLineText("price", "7.00");
        this.escPosDriver.setLineText("date", "Data: "+ new Date().toString());

        /* Get print commands from xml */
        this.printData = this.escPosDriver.xmlToEsc();

        this.printBtn = findViewById(R.id.printBtn);
        this.printBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    DisplayProductsActivity.this.btService.sendByteData(
                            DisplayProductsActivity.this.printData
                    );
                }catch(SendDataException e){
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(),
                            "Failed to print",
                            Toast.LENGTH_SHORT);
                }
            }
        });
    }

    private void configureDagger(){
        AndroidInjection.inject(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //try {
            //this.btService.startConnection();
        //}catch(BluetoothConnectionException e){
            /* alert dialog saying that
            bluetooth connection failed and return
            to previous activity */
        //}
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //try{
        //    this.btService.closeConnection();
        //}catch(BluetoothConnectionException e){
        //    Toast.makeText(this, "FAILED TO CLOSE BLUEETOOTH CONNECTION", Toast.LENGTH_SHORT);
        //}
    }
}

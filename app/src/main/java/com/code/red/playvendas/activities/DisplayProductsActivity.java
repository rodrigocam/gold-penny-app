package com.code.red.playvendas.activities;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.code.red.playvendas.R;
import com.code.red.playvendas.bluetooth.BluetoothService;
import com.code.red.playvendas.exceptions.BluetoothConnectionException;
import com.code.red.playvendas.exceptions.SendDataException;
import com.code.red.playvendas.model.Product;
import com.code.red.playvendas.utils.EscPosDriver.EscPosDriver;

import java.io.InputStream;
import java.util.Date;
import java.util.List;

public class DisplayProductsActivity extends AppCompatActivity {

    private BluetoothService btService = null;

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

        //this.btService = new BluetoothService();

        /* We need this to open the xml template from res/raw folder */
        //this.xmlFile = getResources().openRawResource(R.raw.print_template);

        RecyclerView productList = (RecyclerView) findViewById(R.id.product_list);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(this);
        productList.setLayoutManager(manager);


        Product[] products = new Product[9];
        products[0] = new Product("Heineken", 16.80);
        products[1] = new Product("Viagra", 22.50);
        products[2] = new Product("Água", 4.50);
        products[3] = new Product("Vinho Branco", 22.50);
        products[4] = new Product("Rodrigo Lixo", 1.0);

        productList.setAdapter(new ProductListAdapter(this, products));

        products[5] = new Product("Rodrigo Lixo", 1.0);
        products[6] = new Product("Rodrigo Lixo", 1.0);
        products[7] = new Product("Rodrigo Lixo", 1.0);
        products[8] = new Product("Rodrigo Lixo", 1.0);

        //print_stuff();


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

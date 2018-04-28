package com.code.red.playvendas.activities;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.code.red.playvendas.R;
import com.code.red.playvendas.bluetooth.BluetoothService;
import com.code.red.playvendas.exceptions.BluetoothConnectionException;
import com.code.red.playvendas.exceptions.SendDataException;
import com.code.red.playvendas.utils.EscPosDriver.EscPosDriver;

import java.io.InputStream;

public class DisplayProductsActivity extends AppCompatActivity {

    private BluetoothService btService = null;

    /* Buttons */
    private Button printBtn;

    /* EscPos driver to parse xml template to esc/pos commands */
    private EscPosDriver escPosDriver = new EscPosDriver();

    /* Data that will be printed on thermal printer */
    private byte [] printData;

    /* XML Template with the print template */
    private InputStream xmlFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_products);

        this.btService = new BluetoothService();

        /* We need this to open the xml template from res/raw folder */
        this.xmlFile = getResources().openRawResource(R.raw.print_template);

        this.printData = this.escPosDriver.xmlToEsc(this.xmlFile);

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
            Toast.makeText(this, "FAILED TO CLOSE BLUEETOOTH CONNECTION", Toast.LENGTH_SHORT);
        }
    }
}

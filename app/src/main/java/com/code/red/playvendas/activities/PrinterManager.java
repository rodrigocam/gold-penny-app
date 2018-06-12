package com.code.red.playvendas.activities;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.code.red.playvendas.R;
import com.code.red.playvendas.bluetooth.BluetoothService;
import com.code.red.playvendas.exceptions.BluetoothConnectionException;
import com.code.red.playvendas.exceptions.SendDataException;
import com.code.red.playvendas.model.Product;
import com.code.red.playvendas.utils.EscPosDriver.EscPosDriver;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

public class PrinterManager {
    private BluetoothService btService = null;
    private Context context;
    /* EscPos driver to parse xml template to esc/pos commands */
    private EscPosDriver escPosDriver;

    /* Data that will be printed on thermal printer */
    private byte[] printData;

    /* XML Template with the print template */
    private InputStream xmlFile;

    public PrinterManager(Activity activity) {
        this.btService = new BluetoothService();

        this.context = activity.getApplicationContext();

        /* We need this to open the xml template from res/raw folder */
        this.xmlFile = activity.getResources().openRawResource(R.raw.print_template);

        /* Creating a esc/pos driver with the given xmlfile */
        this.escPosDriver = new EscPosDriver(this.xmlFile);

    }

    public void print(ArrayList<Product> products) {
        ArrayList<Product> selectedProducts = products;

        for (Product product : selectedProducts) {
            this.escPosDriver.setLineText("product", product.getName());
            this.escPosDriver.setLineText("price", "R$ " + product.getPrice() + "0");
            this.escPosDriver.setLineText("date", "Data: " + new Date().toString());

            for (int i = 0; i < product.getQuantity(); i++) {
                sendProductToPrint(this.escPosDriver.xmlToEsc());
            }
        }
    }

    private void sendProductToPrint(byte[] productData) {
        try {
            this.btService.sendByteData(productData);
        } catch (SendDataException e) {
            e.printStackTrace();
            Toast.makeText(context,
                    "Failed to print",
                    Toast.LENGTH_SHORT);
        }
    }

    public void start() {
        try {
            this.btService.startConnection();
        } catch (BluetoothConnectionException e) {
            /* alert dialog saying that
            bluetooth connection failed and return
            to previous activity */
        }
    }

    public void destroy() {
        try {
            this.btService.closeConnection();
        } catch (BluetoothConnectionException e) {
            Toast.makeText(context, "FAILED TO CLOSE BLUETOOTH CONNECTION", Toast.LENGTH_SHORT);
        }
    }
}

package com.code.red.playvendas.activities;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.code.red.playvendas.R;
import com.code.red.playvendas.bluetooth.BluetoothService;
import com.code.red.playvendas.exceptions.BluetoothConnectionException;
import com.code.red.playvendas.exceptions.SendDataException;
import com.code.red.playvendas.model.Product;

import com.redcode.escposxml.EscPosDriver;
import java.io.InputStream;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class PrinterManager {
    private BluetoothService btService = null;
    private Context context;
    /* EscPos driver to parse xml template to esc/pos commands */
    private EscPosDriver escPosDriver;

    /* Data that will be printed on thermal printer */
    private byte[] printData;

    /* XML Template with the print template */
    private InputStream xmlFile;

    private NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));

    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");

    public PrinterManager(Activity activity) {
        this.btService = new BluetoothService();

        this.context = activity.getApplicationContext();

        /* We need this to open the xml template from res/raw folder */
        this.xmlFile = activity.getResources().openRawResource(R.raw.print_template);

        /* Creating a esc/pos driver with the given xmlfile */
        this.escPosDriver = new EscPosDriver(this.xmlFile);

    }

    /**
     * Transforms a sale into byte array to print using a xml template.
     * @param products Product list from a sale
     */
    public void print(ArrayList<Product> products) {
        ArrayList<Product> selectedProducts = products;
        this.simpleDateFormat.setTimeZone(TimeZone.getTimeZone("GMT-3"));

        for (Product product : selectedProducts) {
            this.escPosDriver.setTemplateText("product", product.getName());
            this.escPosDriver.setTemplateText("price", this.numberFormat.format(product.getPrice()));
            this.escPosDriver.setTemplateText("date", this.simpleDateFormat.format(new Date()));

            for (int i = 0; i < product.getQuantity(); i++) {
                sendProductToPrint(this.escPosDriver.getBytes());
            }
        }
    }

    /**
     * Send a sale to the bluetooth printer
     * @param productData Sale data
     */
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
        } catch (RuntimeException e ){
            /* No btService
            * */
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

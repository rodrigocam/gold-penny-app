package com.code.red.playvendas;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity{
    private BluetoothManager bluetoothManager;

    public void manageBluetooth(){
        if(!bluetoothManager.isBluetoothOn()){

            Intent intent = bluetoothManager.activateBluetooth();
            if(intent != null){
                startActivityForResult(intent, bluetoothManager.REQUEST_ENABLE_BT);
            }
        }
    }

    @Override
    protected void onActivityResult(int code, int result , Intent i){
        Log.d("BluetoothStatus", "Code: " + code + " - Result:" + result + " - Intent: "+ i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bluetoothManager = new BluetoothManager();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Button bluetoothStatusButton = (Button) findViewById(R.id.bluetoothStatusButton);
        Button printButton = (Button) findViewById(R.id.printButton);
        Button startBluetoothButton = (Button) findViewById(R.id.startBluetoothButton);

        bluetoothStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, bluetoothManager.isBluetoothOn() + "", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bluetoothManager.writeTextInPrinter(getApplicationContext(), "AE");
                Snackbar.make(view, bluetoothManager.isBluetoothOn() + "", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        startBluetoothButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manageBluetooth();
                Snackbar.make(view, bluetoothManager.isBluetoothOn() + "", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}

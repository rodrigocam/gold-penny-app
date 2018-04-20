package com.code.red.playvendas;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity{

    public void manageBluetooth(){
        BluetoothManager b = new BluetoothManager();
        Intent intent = b.activateBluetooth();
        if(intent != null){
            startActivityForResult(intent, b.REQUEST_ENABLE_BT);
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manageBluetooth();
                BluetoothManager b = new BluetoothManager();
                Snackbar.make(view, b.getBluetoothStatus() + "", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        FloatingActionButton fab2 = (FloatingActionButton) findViewById(R.id.fab2);
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothManager b = new BluetoothManager();
                b.startConnection();
                Snackbar.make(view, b.getBluetoothStatus() + "", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        FloatingActionButton fab4 = (FloatingActionButton) findViewById(R.id.fab4);
        fab4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BluetoothManager b = new BluetoothManager();

                String mensagem = "BIRL CARALHO";
                Snackbar.make(view, b.getBluetoothStatus() + "", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}

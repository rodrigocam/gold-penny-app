package com.code.red.playvendas;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

public class BluetoothManager {

    final int REQUEST_ENABLE_BT;
    public BluetoothManager(){
        REQUEST_ENABLE_BT = 10;
    }

    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    public Boolean getBluetoothStatus() throws Error{
        if(bluetoothAdapter != null){
            if(bluetoothAdapter.isEnabled()) {
                return true;
            }else{
                return false;
            }
        }else {
            throw new Error("Bluetooth not available");
        }
    }

    public Intent activateBluetooth(){
        if(!getBluetoothStatus()){
            Intent enablebtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            return enablebtIntent;
        }else{
            return null;
        }
    }


}

package com.code.red.playvendas;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;

import java.util.Set;

public class BluetoothManager {

    final int REQUEST_ENABLE_BT;
    public BluetoothManager(){
        REQUEST_ENABLE_BT = 10;
    }

    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    Set<BluetoothDevice> pairedDevices = bluetoothAdapter.getBondedDevices();

    public void hasPairedDevices(){
        if (pairedDevices.size() > 0) {
            // Loop through paired devices
            for (BluetoothDevice device : pairedDevices) {
                // Add the name and address to an array adapter to show in a ListView
                //mArrayAdapter.add(device.getName() + "\n" + device.getAddress());
                Log.d("BLUETOOTH STATUS", device.getName() + "\n" + device.getAddress());
            }
        }
    }

    public Boolean getBluetoothStatus() throws Error{
        if(bluetoothAdapter != null){
            if(bluetoothAdapter.isEnabled()) {
                hasPairedDevices();
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

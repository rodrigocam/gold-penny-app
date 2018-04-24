package com.code.red.playvendas;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.util.Log;

import java.util.Set;

public class BluetoothManager {
    public final int REQUEST_ENABLE_BT = 10;
    private ConnectThread ct;
    private final String LOG_TAG = "Bluetooth Manager";
    
    BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    Set<BluetoothDevice> pairedDevices;

    public void updatePairedDevices() {
        pairedDevices = bluetoothAdapter.getBondedDevices();
        Log.d(LOG_TAG, pairedDevices.size() + " paired devices");
    }

    public Boolean isBluetoothOn() throws Error {
        if (bluetoothAdapter != null) {
            if (bluetoothAdapter.isEnabled()) {
                updatePairedDevices();
                Log.d(LOG_TAG, "Bluetooth on");
                return true;
            } else {
                Log.d(LOG_TAG, "Bluetooth off");
                return false;
            }
        } else {
            Log.e(LOG_TAG, "Bluetooth not available");
            throw new Error("Bluetooth not available");
        }
    }

    public Intent activateBluetooth() {
        if (!isBluetoothOn()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            return enableBtIntent;
        } else {
            return null;
        }
    }
    public void writeTextInPrinter(String text) throws Error{
        startConnection();
        if(ct != null){
            ct.write(text);
        }
        closeConnection();
    }

    private void startConnection() {
        // Disables discovery for faster connection
        bluetoothAdapter.cancelDiscovery();

        BluetoothDevice device = null;
        try {
            device = getPrinter();
        }catch(Error e){
            Log.e(LOG_TAG, "Printer not found in paired devices");
        }
        try{
            ct = new ConnectThread(device);
            ct.connect();
        }catch (Error e){
            Log.e(LOG_TAG, "No device paired, cant create Connection");
        }
    }

    private void closeConnection(){
        if(ct!= null){
         ct.disconnect();
        }
        // Reactivate discovery after connection
        bluetoothAdapter.startDiscovery();
    }

    private BluetoothDevice getPrinter() throws Error{
        updatePairedDevices();

        // TODO: Verify if this is enough validation to find a printer;
        BluetoothDevice printer = null;
        for(BluetoothDevice device : pairedDevices){
            if(device.getName().toLowerCase().contains("printer")){
                Log.d(LOG_TAG, device.getName() + " found");
                return device;
            }
        }
        if(printer == null){
            throw new Error("Printer not found");
        }
        return null;
    }
}

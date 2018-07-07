package com.code.red.playvendas;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.redcode.escposxml.EscPosDriver;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Set;

public class BluetoothManager {

    public final int REQUEST_ENABLE_BT = 10;
    private final String LOG_TAG = "Bluetooth Service";
    private boolean CONNECTED = false;
    
    private BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    private BluetoothDevice bluetoohtDevice = null;
    private BluetoothSocket bluetoothSocket = null;
    private OutputStream socketOutput = null;
    private Set<BluetoothDevice> pairedDevices;

    private EscPosDriver escPosDriver = new EscPosDriver();

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

    public void writeTextInPrinter(Context ctx, String text) throws Error{
        if(!CONNECTED){
            startConnection();
        }
        escPosDriver.setTemplate(ctx.getResources()
                .openRawResource(R.raw.template));
        byte [] data = escPosDriver.getBytes();
        try{
            socketOutput.write(data);
            data = null;
            closeConnection();
        }catch(IOException e){
            throw new Error("Failed to print");
        }
    }

    private void startConnection() {

        // Disables discovery for faster connection
        bluetoothAdapter.cancelDiscovery();

        try {
            bluetoohtDevice = getPrinter();

            Method m = bluetoohtDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
            bluetoothSocket = (BluetoothSocket) m.invoke(bluetoohtDevice, 1);
            bluetoothSocket.connect();
            CONNECTED = true;
            socketOutput = bluetoothSocket.getOutputStream();
        }catch(Exception e){
            Log.e(LOG_TAG, e.getMessage());
            e.printStackTrace();
        }catch(Error e){
            Log.e(LOG_TAG, e.getMessage());
        }
    }

    private void closeConnection(){
        if(bluetoothSocket != null){
            try{
                bluetoothSocket.close();
                CONNECTED = false;
            }catch(IOException e){
                Log.e(LOG_TAG, "FAILED TO CLOSE SOCKET");
                e.printStackTrace();
            }
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

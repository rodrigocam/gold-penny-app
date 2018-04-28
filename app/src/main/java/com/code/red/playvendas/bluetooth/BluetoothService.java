package com.code.red.playvendas.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import com.code.red.playvendas.exceptions.BluetoothConnectionException;
import com.code.red.playvendas.exceptions.FindPrinterException;
import com.code.red.playvendas.exceptions.SendDataException;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.util.Set;

public class BluetoothService {

    /* Constants*/
    private final String LOG_TAG = "Bluetooth Service";

    private boolean CONNECTED = false;

    /* Bluetooth */
    private BluetoothAdapter bluetoothAdapter = null;
    private BluetoothDevice bluetoohtDevice = null;
    private BluetoothSocket bluetoothSocket = null;

    /* Output to send print commands */
    OutputStream socketOutput = null;


    public BluetoothService(){
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    public void startConnection() throws BluetoothConnectionException{
        if(!CONNECTED){
            // Disables discovery for faster connection
            bluetoothAdapter.cancelDiscovery();

            try {
                bluetoohtDevice = getPrinter();

                Method m = bluetoohtDevice.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                bluetoothSocket = (BluetoothSocket) m.invoke(bluetoohtDevice, 1);
                bluetoothSocket.connect();
                socketOutput = bluetoothSocket.getOutputStream();
                CONNECTED = true;
            }catch(Exception e){
                Log.e(LOG_TAG, "BLUETOOTH CONNECTION FAILED!!");
                e.printStackTrace();
                throw new BluetoothConnectionException();
            }
        }
    }

    public void closeConnection() throws BluetoothConnectionException{
        if(CONNECTED){
            try{
                this.bluetoothSocket.close();
                CONNECTED = false;
            }catch(IOException e){
                Log.e(LOG_TAG, "FAILED TO CLOSE CONNECTION!");
                throw new BluetoothConnectionException();
            }
        }
    }

    public void sendByteData(byte[] data) throws SendDataException {
        if(CONNECTED){
            try {
                this.socketOutput.write(data);
                this.socketOutput.flush();
            }catch(IOException e){
                Log.e(LOG_TAG, "FAILED TO SEND DATA!");
                e.printStackTrace();
                throw new SendDataException();
            }
        }else{
            Log.e(LOG_TAG, "CAN'T SEND DATA, DEVICE NOT CONNECTED!");
            throw new SendDataException();
        }
    }

    private BluetoothDevice getPrinter() throws FindPrinterException{

        BluetoothDevice printer = null;
        Set<BluetoothDevice> pairedDevices = this.bluetoothAdapter.getBondedDevices();

        for(BluetoothDevice device : pairedDevices){
            if(device.getName().toLowerCase().contains("printer")){
                Log.d(LOG_TAG, device.getName() + " found");
                printer = device;
            }
        }
        if(printer == null){
            throw new FindPrinterException();
        }
        return printer;
    }
}

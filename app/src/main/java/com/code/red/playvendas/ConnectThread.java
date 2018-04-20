package com.code.red.playvendas;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;


public class ConnectThread extends Thread {
    private final BluetoothSocket mmSocket;
    private final BluetoothDevice mmDevice;
    private OutputStream os = null;
    private final UUID key = UUID.randomUUID();

    public ConnectThread(BluetoothDevice device) {
        // Use a temporary object that is later assigned to mmSocket,
        // because mmSocket is final
        BluetoothSocket tmp = null;
        mmDevice = device;

        // Get a BluetoothSocket to connect with the given BluetoothDevice
        try {
            // MY_UUID is the app's UUID string, also used by the server code
            tmp = device.createRfcommSocketToServiceRecord(device.getUuids()[0].getUuid());
            Log.d("porra", ""+ tmp.isConnected());
            os = tmp.getOutputStream();
        } catch (IOException e) {
            Log.d("falha de output/socket", "Falhou meu brother");
        }
        mmSocket = tmp;

    }

    public void run() {
        // Cancel discovery because it will slow down the connection
        //mBluetoothAdapter.cancelDiscovery();

        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            mmSocket.connect();
            try{
                byte[] data = new byte[]{27,64,27,33,8,27,97,1,66,73,82,76,32,67,65,82,65,76,72,79,27,100,5,29,86,1};
                os.write(data);
            }catch(IOException exception){
                exception.printStackTrace();
                Log.d("output fail","Não rolou, meu brother...");
            }
        } catch (IOException connectException) {
            connectException.printStackTrace();
            Log.d("output fail","Não rolou, meu Parça...");
            // Unable to connect; close the socket and get out
            try {
                mmSocket.close();
            } catch (IOException closeException) { }
            return;
        }

        // Do work to manage the connection (in a separate thread)
        //manageConnectedSocket(mmSocket);
    }

    /** Will cancel an in-progress connection, and close the socket */
    public void cancel() {
        try {
            mmSocket.close();
        } catch (IOException e) { }
    }
    public void write(byte[] text){

    }
}
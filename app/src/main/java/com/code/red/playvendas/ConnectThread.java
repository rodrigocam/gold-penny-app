package com.code.red.playvendas;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.IOException;
import java.io.OutputStream;
import java.util.UUID;


public class ConnectThread extends Thread {
    private final BluetoothSocket socket;
    private OutputStream os = null;
    private final String LOG_TAG = "Connect Thread";

    public ConnectThread(BluetoothDevice device) {
        BluetoothSocket tmp = null;
        if(device != null){

            try {
                UUID uuid = device.getUuids()[0].getUuid();
                tmp = device.createRfcommSocketToServiceRecord(uuid);
                // TODO: verify is status false means that the device is really disconnected or it means more things.
                Log.i(LOG_TAG, "Socket created, status: "+ getSocketStatus(tmp));

                os = tmp.getOutputStream();
                Log.i(LOG_TAG, "Output Stream available");
            } catch (IOException e) {
                Log.e(LOG_TAG, "Failed to obtain outputstream/socket with device, probably this device its not paired");
            }
            socket = tmp;
        }else{
            throw new Error("Cannot create ConnectionThread with null Device");
        }
    }

    public void connect() {
        try {
            // Connect the device through the socket. This will block
            // until it succeeds or throws an exception
            socket.connect();
        } catch (IOException connectException) {
            Log.e(LOG_TAG,"Failed to activate socket connection");
            Log.i(LOG_TAG,"Socket Status: " +  getSocketStatus(socket));
            disconnect();
        }
    }

    /** Will cancel an in-progress connection, and close the socket */
    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(LOG_TAG, "Failed to close the socket");
        }
    }

    public void write(String text){
        if(socket.isConnected()){
            try{
                byte[] data = convertString(text);
                os.write(data);
                os.flush();
            }catch(IOException exception){
                exception.printStackTrace();
                Log.e(LOG_TAG,"Write text failed, output stream not available");
            }
        }
    }

    private byte[] convertString(String text) {
        // TODO: Convert text into byte[]
        return new byte[]{27,64,27,33,8,27,97,1,66,73,82,76,32,67,65,82,65,76,72,79,27,100,5,29,86,1};
    }

    @NonNull
    private String getSocketStatus(BluetoothSocket tmp) {
        return (tmp.isConnected())?"Device connected":"Device Disconnected";
    }
}
package com.example.mrtdandroidapp;


import static android.content.ContentValues.TAG;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.example.mrtdandroidapp.Crt288xurDrv.Crt288x;

import net.sf.scuba.smartcards.CardService;
import net.sf.scuba.smartcards.CardServiceException;
import net.sf.scuba.smartcards.CommandAPDU;
import net.sf.scuba.smartcards.ResponseAPDU;

class CardServiceAdapterException extends Exception {
    final String message;

    CardServiceAdapterException(String message) {
        this.message = message;
    }
}

public class CardServiceAdapter extends CardService {
    private final Crt288x crt288x = Crt288x.getInstance();

    private boolean isOpened = false;

    final String ACTION_USB_PERMISSION = "com.example.android_crt288br.USB_PERMISSION";

    int DeviceType = 0;




    protected BroadcastReceiver usbReceicer = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

        }
    }

    @Override
    public void open() throws CardServiceException {
        crt288x.OpenDevice()

    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public ResponseAPDU transmit(CommandAPDU commandAPDU) throws CardServiceException {
        try {
            byte[] inputChipData = commandAPDU.getBytes();
            int[] intOutChipDataLength = new int[3];
            byte[] byteOutChipData = new byte[4048];
            int ulInChipDataLength = inputChipData.length;
            int[] cardStatus = new int[1];
        } catch () {
        }
        return null;
    }

    @Override
    public byte[] getATR() throws CardServiceException {
        return new byte[0];
    }

    @Override
    public void close() {
        int closeValue = crt288x.CloseDevice();
        Log.d(TAG, "close: " + closeValue);
    }

    @Override
    public boolean isConnectionLost(Exception e) {
        return !isOpened;
    }
}

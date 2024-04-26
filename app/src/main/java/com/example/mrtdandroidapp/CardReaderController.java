package com.example.mrtdandroidapp;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.util.Log;

import com.example.mrtdandroidapp.Crt288xurDrv.Crt288x;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;

public class CardReaderController {

    private static CardReaderController deviceConnection = null;

    public static CardReaderController getInstance() {
        if (deviceConnection == null) deviceConnection = new CardReaderController();

        return deviceConnection;
    }

    private final Crt288x crt288x = Crt288x.getInstance();

    final String ACTION_USB_PERMISSION = "com.example.android_crt288br.USB_PERMISSION";

    public boolean buttonOpened = false;


    int DeviceType = 0;

    int fd = 0;



    public void handleUsbConnection(UsbDeviceConnection connection) {
        final String TAG = "CCHandleUSBConnection";
        if (connection != null) {
            fd = connection.getFileDescriptor();
            int responseCode = 0;
            if (DeviceType == 1) {
                responseCode = crt288x.OpenUsbDevice(fd);

            } else if (DeviceType == 2) {
                responseCode = crt288x.OpenHidDevice(fd);
            }
            if (responseCode == 0) {
                buttonOpened = true;
                Log.d(TAG, "Open USB successfully");
            } else {
                Log.d(TAG, "Failed to open USB " + responseCode);
            }
            Log.i(TAG, "onReceive---fd=" + fd + "---responseCode=" + responseCode);
        }
    }

    @SuppressLint("UnspecifiedRegisterReceiverFlag")
    public void connect(Context context, BroadcastReceiver receiver) {
        final String TAG = "CCConnect";
        UsbManager manager = (UsbManager) context.getSystemService(Context.USB_SERVICE);
        HashMap<String, UsbDevice> deviceList = manager.getDeviceList();
        Iterator<UsbDevice> deviceIterator = deviceList.values().iterator();
        Log.d(TAG, deviceIterator.toString());

        PendingIntent mPermissionIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), PendingIntent.FLAG_MUTABLE);
        IntentFilter filter = new IntentFilter(ACTION_USB_PERMISSION);
        context.registerReceiver(receiver, filter);

        while (deviceIterator.hasNext()) {
            UsbDevice device = deviceIterator.next();

            if ((0x23d8 == device.getVendorId())) {
                if ((0x285 == device.getProductId()) || (0x603 == device.getProductId())) {
                    Log.i(TAG, device.getDeviceName() + " " + Integer.toHexString(device.getVendorId()) + " " + Integer.toHexString(device.getProductId()));
                    manager.requestPermission(device, mPermissionIntent);
                    if (0x285 == device.getProductId()) {
                        DeviceType = 1;
                    } else {
                        DeviceType = 2;
                    }
                    break;
                }
            }
        }

    }

    public void init() throws UnsupportedEncodingException {
        final String TAG = "CCInit";
        byte[] versionMes = new byte[256];
        int response = crt288x.InitDev(1, versionMes);
        if (response == 0) {
            String version = new String(versionMes, "UTF-8");
            Log.d(TAG, version);
            Log.d(TAG, "Device initialized successfully");
        }
        Log.d(TAG, "Device initialization failed");
    }

    public void close() {
        final String TAG = "CCClose";
        final int closeValue = crt288x.CloseDevice();
        if (closeValue == 0) {
            Log.d(TAG, "close: " + closeValue);
            Log.d(TAG, "Close connection success");
            return;
        }
        Log.d(TAG, "close: " + closeValue);
        Log.d(TAG, "An error happened when trying to close connection");
    }

    public void getCardType() {
        final String TAG = "CCGetCardType";
        final int requestValue = crt288x.GetRFType();
        Log.d(TAG, "getCardType: " + requestValue);
        if (requestValue == 120) {
            Log.d(TAG, "Success ID Card, maybe TD1");
        }
        Log.d(TAG, "Success ID Card " + requestValue);
    }


    public void power() {
        final String TAG = "CCPower";

        byte[] byteOutChipData = new byte[256];
        int[] iOutChipDataLength = new int[256];
        /// iType = card Type response 120 - Type A TD1
        final int responseValue = crt288x.ChipPower(120, 0x02, byteOutChipData, iOutChipDataLength);
        Log.d(TAG, "power: " + responseValue);
        if (responseValue == 0) {
            StringBuilder response = new StringBuilder();
            for (int i = 0; i < iOutChipDataLength[0]; i++) {
                int ch = byteOutChipData[i];
                int ch1 = (ch >> 4) & 0x0000000f;
                int ch2 = ch & 0x0000000f;
                response.append(Integer.toHexString(ch1)).append(Integer.toHexString(ch2));
            }
            Log.d(TAG, "Success RF Card Chip Power succeeded " + response);

        } else {
            Log.d(TAG, "Failed RF Card Chip Power");
        }
    }

    public void commandAPDU(String input) {
        final String TAG = "CCCommandAPDU";

        if (input.isEmpty()) {
            Log.d(TAG, "NO COMMAND");
            return;
        }
        byte[] inputChipData = input.getBytes();
        int[] intOutChipDataLength = new int[128];
        byte[] byteOutChipData = new byte[1024];
        int ulInChipDataLength = inputChipData.length;
        final int response = crt288x.ChipIO(1, ulInChipDataLength, inputChipData, intOutChipDataLength, byteOutChipData);

        if (response == 0) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < intOutChipDataLength[0]; i++) {
                int ch = byteOutChipData[i];
                int ch1 = (ch >> 4) & 0x0000000f;
                int ch2 = ch & 0x0000000f;
                builder.append(Integer.toHexString(ch1)).append(Integer.toHexString(ch2));
            }

            Log.d(TAG, String.valueOf(builder));

        } else {
            Log.d(TAG, "Cannot process command");
        }
    }

}

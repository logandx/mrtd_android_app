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
import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;

public class UsbDeviceHelper {
	public static final int ERR_FIND_DEVICE      = -2;
	public static final int ERR_DEVICE_OPEN 	 = -4;
	public static final int ERR_DEVICE_PERMISSION = -5;

	private static final String TAG = "UsbDeviceHelper";
	private static UsbManager mUsbManager = null;
	private static UsbDevice mUsbDevice = null;
	private static UsbDeviceConnection mDeviceConnection = null;

	private static PendingIntent mPermissionIntent;
	private final Context mContext;
	private static String usbDevicePath = "";
	private static final String ACTION_USB_DEVICE_PERMISSION = "com.android.usb.USB_PERMISSION";

	@SuppressLint("UnspecifiedRegisterReceiverFlag")
	public UsbDeviceHelper(Context context) {
		mContext = context;

		mPermissionIntent = PendingIntent.getBroadcast(mContext, 0, new Intent(
				"com.example.android_crt288br.USB_PERMISSION"), PendingIntent.FLAG_IMMUTABLE);

		IntentFilter filter = new IntentFilter("com.example.android_crt288br.USB_PERMISSION");
		
		filter.addAction(ACTION_USB_DEVICE_PERMISSION);
		filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
		filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
		mContext.registerReceiver(mUsbReceiver, filter);
	}

	public void ReleaseUsbDeviceHelper() {
		if (mContext != null)
			mContext.unregisterReceiver(mUsbReceiver);
	}

	private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
		public void onReceive(Context context, Intent intent) {
			if(intent == null)
				return;
			UsbDevice usbDevice = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
			String action = intent.getAction();
			if (TextUtils.isEmpty(action))
				return;
			switch (action) {
				case UsbManager.ACTION_USB_DEVICE_ATTACHED:
					assert usbDevice != null;
					Log.d(TAG, "UsbDevice (VID:" + usbDevice.getVendorId()
							+ ",PID:" + usbDevice.getProductId()
							+ ") DEVICE_ATTACHED");

					//requestUserPermission(usbDevice);

					break;
				case UsbManager.ACTION_USB_DEVICE_DETACHED:
					assert usbDevice != null;
					Log.d(TAG, "UsbDevice (VID:" + usbDevice.getVendorId()
							+ ",PID:" + usbDevice.getProductId()
							+ ") DEVICE_DETACHED");

					break;
				case ACTION_USB_DEVICE_PERMISSION:
				case "com.example.android_crt288br.USB_PERMISSION":
					// 获取连接设备的权限
					boolean isGranted = intent.getBooleanExtra(
							UsbManager.EXTRA_PERMISSION_GRANTED, false);
					if (!isGranted) {
						// 用户未授权
						requestUserPermission(usbDevice);
					}
//					else {
//						// 用户已授权
//					}
					Log.d(TAG, "permission device " + isGranted);
					break;
				default:
					Log.d(TAG, "permission device no where");
					break;
			}
		}
	};

     private void requestUserPermission(UsbDevice usbDevice) {
    	mUsbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
        if (mUsbManager.hasPermission(usbDevice)){
            return;
        }

        PendingIntent mPendingIntent = PendingIntent.getBroadcast(mContext, 0,
                new Intent(ACTION_USB_DEVICE_PERMISSION), PendingIntent.FLAG_IMMUTABLE);

        mUsbManager.requestPermission(usbDevice, mPendingIntent);
    }
 
	public int getUsbFileDescriptor(int VendorId, int ProductID) {
		int UsbFileDescriptor = 0;
		mUsbManager = (UsbManager) mContext.getSystemService(Context.USB_SERVICE);
		HashMap<String, UsbDevice> deviceList = mUsbManager.getDeviceList();
		if (deviceList.size() <= 0) {
			return ERR_FIND_DEVICE;
		} else {
			mUsbDevice = null;
			for (UsbDevice device : deviceList.values()) {
				usbDevicePath = device.getDeviceName();
				if ((device.getVendorId() == VendorId)
						&& (device.getProductId() == ProductID)) {
					mUsbDevice = device;
					if (!mUsbManager.hasPermission(mUsbDevice)) {
						mUsbManager.requestPermission(mUsbDevice, mPermissionIntent);
					}
					break;
				}
			}
			if (mUsbDevice != null) {
				UsbDeviceConnection conn = null;
				if (mUsbManager.hasPermission(mUsbDevice)) {
					Log.d(TAG, "has permission");
					conn = mUsbManager.openDevice(mUsbDevice);
				} else {
					Log.d(TAG, "no permission");
					return ERR_DEVICE_PERMISSION;
				}
				if (conn == null) {
					return ERR_DEVICE_OPEN;
				}

				UsbFileDescriptor = conn.getFileDescriptor();

				Log.d(TAG, " getFileDescriptor: " + UsbFileDescriptor);

				mDeviceConnection = conn;

				return UsbFileDescriptor;
			}

			return ERR_FIND_DEVICE;
		}
	}

	public String getUsbDevPath() {
		return usbDevicePath;
	}

	public boolean isOpen() {
		return (mUsbManager != null) && (mUsbDevice != null) &&
				(mDeviceConnection != null) && (mUsbManager.hasPermission(mUsbDevice));
	}
}

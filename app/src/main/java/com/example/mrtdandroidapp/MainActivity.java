package com.example.mrtdandroidapp;


import androidx.fragment.app.Fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbManager;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mrtdandroidapp.Crt288xurDrv.Crt288x;

public class MainActivity extends AppCompatActivity {

    public static final String ERROR = "Error";
    CardReaderController cardReaderController = CardReaderController.getInstance();

    Crt288x mCrt288x = Crt288x.getInstance();

    private final static String TAG = "CRT288bru";

    Button btnOpen, btnOpenUsb, btnClose, btnInit, btnGetCardType, btnSamReset, btnSamCommond, btnChipPower, btn_RepAct, btn_RFCommand, btn_PBOC;
    Button btn_gettrack, btn_cleartrack, btn_opentrack, btn_enteriap;

    EditText editsPort, editBaudRate, editsSend, editResetmode;

    private TextView tvLogs;

    private Context mContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = MainActivity.this;
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().setReorderingAllowed(true).add(
                    R.id.container, PlaceholderFragment.class, null
            ).commit();
        }
    }

    protected void onStart() {
        super.onStart();

        btnOpenUsb = (Button) this.findViewById(R.id.buttonOpenUsbDev);
        btnClose = (Button) this.findViewById(R.id.buttonCloseDev);
        btnInit = (Button) this.findViewById(R.id.buttonInitDev);
        btnGetCardType = (Button) this.findViewById(R.id.buttonGetCardType);
        btnChipPower = (Button) this.findViewById(R.id.btnChipPower);
        btn_RepAct = (Button) this.findViewById(R.id.btn_RepAct);
        btn_RFCommand = (Button) this.findViewById(R.id.btn_RFCommand);
        btnSamReset = (Button) this.findViewById(R.id.btn_SamReset);
        btnSamCommond = (Button) this.findViewById(R.id.btn_SamCommand);
        btn_gettrack = (Button) this.findViewById(R.id.btn_getalltrack);
        btn_cleartrack = (Button) this.findViewById(R.id.btn_clearalltrack);
        btn_opentrack = (Button) this.findViewById(R.id.btn_opentrack);
        btn_enteriap = (Button) this.findViewById(R.id.btn_enteriap);
        btn_PBOC = (Button) this.findViewById(R.id.btn_PBOC);

        btnOpenUsb.setOnClickListener(myOnClickListener);
        btnClose.setOnClickListener(myOnClickListener);
        btnInit.setOnClickListener(myOnClickListener);
        btnGetCardType.setOnClickListener(myOnClickListener);
        btnChipPower.setOnClickListener(myOnClickListener);
        btn_RepAct.setOnClickListener(myOnClickListener);
        btn_RFCommand.setOnClickListener(myOnClickListener);
        btnSamReset.setOnClickListener(myOnClickListener);
        btnSamCommond.setOnClickListener(myOnClickListener);
        btn_gettrack.setOnClickListener(myOnClickListener);
        btn_cleartrack.setOnClickListener(myOnClickListener);
        btn_opentrack.setOnClickListener(myOnClickListener);
        btn_enteriap.setOnClickListener(myOnClickListener);
        btn_PBOC.setOnClickListener(myOnClickListener);

        tvLogs = (TextView) findViewById(R.id.txt_logs);
        editsSend = (EditText) this.findViewById(R.id.editSend);
        editResetmode = (EditText) this.findViewById(R.id.editResetmode);
    }

    int fd = 0;
    public boolean buttonOpened = false;
    int deviceType = 0;

    protected BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            String ACTION_USB_PERMISSION = "com.example.android_crt288br.USB_PERMISSION";
            if (ACTION_USB_PERMISSION.equals(action)) {
                synchronized (this) {


                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                        UsbManager manager = (UsbManager) getSystemService(Context.USB_SERVICE);
                        UsbDevice device = (UsbDevice) intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                        UsbDeviceConnection connection = manager.openDevice(device);
//                        cardReaderController.handleUsbConnection(connection);
                        if (connection != null) {
                            fd = connection.getFileDescriptor();
                            int iRet = 0;
                            if (deviceType == 1) {
                                iRet = mCrt288x.OpenUsbDevice(fd);
                            } else if (deviceType == 2) {
                                iRet = mCrt288x.OpenHidDevice(fd);
                            }

                            if (iRet == 0) {
                                buttonOpened = true;
                                Log.i(TAG, "Open USB successfully");
                            } else {
                                Log.e(ERROR, "Failed to open USB " + iRet);
                            }
                            Log.i(TAG, "onReceive---fd=" + fd + "---iRet=" + iRet);
                             Toast.makeText(MainActivity.this, "ádasd", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.i(TAG, "UsbManager openDevice failed");
                        }
                    }


                }

            }
        }
    };

    private final View.OnClickListener myOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {


            if (v == btnOpenUsb) {
                try {
                    cardReaderController.connect(mContext, mUsbReceiver);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(ERROR, String.valueOf(e));
                }
            } else if (v == btnInit) {
                try {
                    cardReaderController.init();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(ERROR, String.valueOf(e));

                }
            } else if (v == btnGetCardType) {
                try {
                    cardReaderController.getCardType();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(ERROR, String.valueOf(e));
                }
            } else if (v == btn_RFCommand) {
                try {
                    cardReaderController.commandAPDU(editsSend.getText().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(ERROR, String.valueOf(e));
                }
            }
        }
    };

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
            super();
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }

}
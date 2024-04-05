package com.example.mrtdandroidapp.Crt288xurDrv;
import android.R.integer;
import android.util.Log;

public class Crt288x {

	private static Crt288x mCrt288x = null;

	public static Crt288x getInstance() {
		if (mCrt288x == null)
			mCrt288x = new Crt288x();

		return mCrt288x;
	}
	public native  int OpenDevice(char sPort[], int iBaudRate);
	public native  int OpenUsbDevice(int fd);
	public native  int CloseDevice();
	public native  int ExeCommand(int iSendDataLen, byte bySendData[], int iRecvDataLen[], byte byRecvData[], byte byStCode[]);
	public native  int GetDllVersion(char VersMes[]);
	public native  int InitDev(int iType, byte VersMes[]);
	public native  int GetCardStatus();
	public native  int GetICType();
	public native  int GetRFType();
	public native  int ChipPower(int iType, int wChipPower, byte byOutChipData[], int iOutChipDatalen[]);
	public native  int ChipIO(int wChipProtocol, int ulInChipDataLength , byte lpInbChipData[], int ulOutChipDataLength[] , byte lpOutbChipData[]);
	public native  int ReadIDCardInfo(byte szIDInfo[]);
	public native  int ReadIdCardImg(byte byImgData[]);
	public native  int ReadALLIDCardInfo(int iType[], byte szIDInfo[]);

	public native  int SAMSlotChange(int iSamNum);

//	type=0  5V EMV
//	type=1  5V ISO7816
//	type=2  3V ISO7816
	public native  int SAMActivation(int type,byte atr[]);
	public native  int SAMPowerOff();
	public native  int GetSAMStatus();

	//Type T=0 Type=0 .T=1 Type=1 . Type=2 Auto
	public native  int SAMApdu(int Type,int InDataLength , byte InData[], int OutDataLength[] , byte OutData[]);
	public native  int SetReaderMagType(int iReadMode, int iDataMode, int bAutoUpload);
	public native  int ReadAllTracks(char szTrack1Data[], char szTrack2Data[], char szTrack3Data[]);
	public native  int ReadTrack(int iTrackNum, char szTrackData[]);
	public native  int ClearTrackData();

	public native  int MifareKeyProcess(int iMode, int iKs, int iSn, int uDataLength, byte lpData[]);
	public native  int MifareCardProcess(int iMode, int iSn, int iBn, int iLc, int uDataLength[], byte lpData[]);

	public native  int SetLogModel(int iLogModel);
    public native  int BeeControl(int iTimeCnt, int iTimes);

	public native  int EnterIAP();
	public native  int OpenHidDevice(int fd);
	public native  int download(String path);

	public native int PBOCExeCommand(int iSendDataLen, byte bySendData[], int iRecvDataLen[], byte byRecvData[]);
    public native int PBOCDealConfigure(int iTagLen, byte byTag[]);
    public native int PBOCTerminalCAPK(byte tsEMVCAPK[], int iCAPKLenth);
    public native int PBOCTerminalAPP(byte tsEMVAPP[], int iAPPLenth);
    public native int PBOCGetTLV(byte byTag[], int iValueLenth[], byte byValue[]);
    public native int PBOCGetCAPK(int iCount, byte tsEMVCAPK[], int iCAPKLenth[]);
    public native int PBOCGetAPP(int iCount, byte tsEMVAPP[], int iAPPLenth[]);
    public native int PBOCDoTransaction(int iINTLVsLenth, byte byINTLVs[], int iOUTTLVsLenth[], byte byOUTTLVs[]);
    public native int PBOCUpdateScript(int iINTLVsLenth, byte byINTLVs[], int iOUTTLVsLenth[], byte byOUTTLVs[]);


    static
	{
        try {  

        	System.loadLibrary("crt288xur_drv");	
        }  

        catch (UnsatisfiedLinkError ule) {  
        	Log.e("crt288x", "Load crt288xur_drv Error!");
        }  
	}

}

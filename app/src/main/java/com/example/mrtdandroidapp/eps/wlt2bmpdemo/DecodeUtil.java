package com.example.mrtdandroidapp.eps.wlt2bmpdemo;

public class DecodeUtil {

	public native static int Wlt2Bmp(String wltPath, String bmpPath);
	
	static{
		System.loadLibrary("Wlt2BmpDemo");
	}
}

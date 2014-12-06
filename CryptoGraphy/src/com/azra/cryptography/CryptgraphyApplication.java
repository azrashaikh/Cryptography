package com.azra.cryptography;

import android.app.Application;
import android.content.Context;

public class CryptgraphyApplication extends Application {
	private static Context context;
	
	@Override
	public void onCreate() {
		super.onCreate();
		CryptgraphyApplication.context = this.getApplicationContext();
		
	}
	
	public static Context getAppContext() {
		return CryptgraphyApplication.context;
	}

}
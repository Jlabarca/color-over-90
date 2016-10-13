package com.jlabarca.kflame.android;

import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.jlabarca.kflame.MainJuego;

public class AndroidLauncher extends AndroidApplication {
	
	//protected GameHelper mHelper;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new MainJuego(), config);
	}
}

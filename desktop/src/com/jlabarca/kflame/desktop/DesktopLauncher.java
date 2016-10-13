package com.jlabarca.kflame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.graphics.Color;
import com.jlabarca.kflame.MainJuego;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.initialBackgroundColor = Color.BLACK;
		cfg.width = 320;
	    cfg.height = 480;
	    cfg.width = 500;
	    cfg.height = 750;
	    // fullscreen
	    //cfg.fullscreen = true;
	    // vSync
	    cfg.vSyncEnabled = true;
		new LwjglApplication(new MainJuego(), cfg);
	}
}

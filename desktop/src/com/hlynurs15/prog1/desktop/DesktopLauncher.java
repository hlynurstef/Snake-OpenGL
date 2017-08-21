package com.hlynurs15.prog1.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.hlynurs15.prog1.Prog1Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		config.title = "Prog1"; // or whatever you like
		config.width = 800;  //experiment with
		config.height = 600;  //the window size
		
		new LwjglApplication(new Prog1Game(), config);
	}
}

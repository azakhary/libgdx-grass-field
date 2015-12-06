package com.underwater.littleplanet.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.underwater.littleplanet.MainGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Little Planet";
		config.useGL30 = false;
		config.width = 1200;
		config.height = 800;
		new LwjglApplication(new MainGame(), config);
	}
}

package com.mygdx.ttsgame.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.ttsgame.TTSGame;
import com.mygdx.ttsgame.utils.Constants;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new TTSGame(), config);
		config.height = Constants.APP_HEIGHT;
		config.width = Constants.APP_WIDTH;
	}
}

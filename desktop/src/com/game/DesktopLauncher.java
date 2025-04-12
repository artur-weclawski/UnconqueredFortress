package com.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

import static com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration.getDisplayMode;

//https://kottke.org/plus/type/silkscreen/index.html
//Link to font licence
//Music: The Sleeping Prophet by Jesse Gallagher - YouTube licence

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setForegroundFPS(60);
		config.setResizable(false);
		config.setTitle("Unconquered Fortress");
		if(getDisplayMode().width > 1920 && getDisplayMode().height > 1080)
		{
			config.setWindowedMode(1920,1080);
			config.setDecorated(true);
			config.setMaximized(true);
		}else{
			config.setFullscreenMode(getDisplayMode());
		}
		config.setWindowIcon("assets/icons/gameIcon.png");
		new Lwjgl3Application(new Main(), config);
	}
}

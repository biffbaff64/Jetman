package com.richikin.jetman.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.tools.texturepacker.TexturePacker;
import com.richikin.jetman.config.Version;
import com.richikin.jetman.core.MainGame;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.utils.google.PlayServicesDesktop;

public class DesktopLauncher
{
	// Set _rebuildAtlas to TRUE to rebuild all atlases. This is only really necessary
	// when new images are added to a folder, but can be left as true by default
	// if you prefer to rebuild every time.
	// NB: Bear in mind that, if you are testing builds other than the desktop
	// build and you add new images to a folder, YOU MUST build and run the desktop
	// version so that the atlases are rebuilt. If you don't do this then you will
	// experience errors when the program tries to access the new images.
	private static final boolean _rebuildAtlas          = true;
	private static final boolean _drawDebugLines        = false;
	private static final boolean _removeDuplicateImages = false;

	private DesktopLauncher()
	{
	}

	public static void main(String[] args)
	{
		if (_rebuildAtlas)
		{
			TexturePacker.Settings settings = new TexturePacker.Settings();

			settings.maxWidth  = 2048;        // Maximum Width of final atlas image
			settings.maxHeight = 2048;        // Maximum Height of final atlas image
			settings.pot       = true;
			settings.debug     = _drawDebugLines;
			settings.alias     = _removeDuplicateImages;

			//
			// Build the Atlases from the specified parameters :-
			// - configuration settings
			// - source folder
			// - destination folder
			// - name of atlas, without extension (the extension '.atlas' will be added automatically)
			TexturePacker.process(settings, "data/packedimages/objects", "data/packedimages/output", "objects");
			TexturePacker.process(settings, "data/packedimages/animations", "data/packedimages/output", "animations");
			TexturePacker.process(settings, "data/packedimages/achievements", "data/packedimages/output", "achievements");
			TexturePacker.process(settings, "data/packedimages/input", "data/packedimages/output", "buttons");
			TexturePacker.process(settings, "data/packedimages/text", "data/packedimages/output", "text");
		}

		//
		// Application configuration settings
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.addIcon("data/icon32x32.png", Files.FileType.Internal);

		config.title         = Version.getDisplayVersion();
		config.width         = Gfx._DESKTOP_WIDTH;
		config.height        = Gfx._DESKTOP_HEIGHT;
		config.backgroundFPS = (int) Gfx._FPS;
		config.foregroundFPS = (int) Gfx._FPS;
		config.resizable     = true;
		config.vSyncEnabled  = true;
		config.fullscreen    = false;
		config.useGL30       = false;
		config.forceExit     = false;

		//
		// Create the Google Play Services handler
		PlayServicesDesktop playServicesDesktop = new PlayServicesDesktop();

		new LwjglApplication(new MainGame(playServicesDesktop), config);
	}
}

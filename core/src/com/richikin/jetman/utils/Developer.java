package com.richikin.jetman.utils;

import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.config.Settings;
import com.richikin.jetman.core.App;

public abstract class Developer
{
    public static  boolean developerPanelActive = false;

    private static boolean _DEVMODE             = false;
    private static boolean _ANDROID_ON_DESKTOP  = false;
    private static App app;

    /**
     * Set _DEVMODE from the _DEV_MODE Environment variable.
     */
    public static void setMode(App _app)
    {
        app = _app;

        if (AppConfig.isDesktopApp())
        {
            _DEVMODE = "TRUE".equals(System.getenv("_DEV_MODE").toUpperCase());
        }

        if (AppConfig.isAndroidApp())
        {
            _DEVMODE = false;
        }
    }

    public static boolean isDevMode()
    {
        return _DEVMODE;
    }

    public static boolean isGodMode()
    {
        return app.settings.isEnabled(Settings._GOD_MODE);
    }

    public static boolean isAndroidOnDesktop()
    {
        return app.settings.isEnabled(Settings._ANDROID_ON_DESKTOP);
    }
}

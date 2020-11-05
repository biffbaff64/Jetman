package com.richikin.utilslib;

import com.richikin.jetman.config.Settings;
import com.richikin.jetman.core.App;

public abstract class Developer
{
    public static boolean developerPanelActive = false;

    // TODO: 05/11/2020
    private static boolean _ANDROID_ON_DESKTOP = false;
    private static boolean _GOD_MODE = false;
    private static boolean _DEVMODE = false;

    /**
     * Set _DEVMODE from the _DEV_MODE Environment variable.
     */
    public static void setMode()
    {
        if (AppSystem.isDesktopApp())
        {
            _DEVMODE = "TRUE".equals(System.getenv("_DEV_MODE").toUpperCase());
        }
    }

    public static void setDevMode(boolean _state)
    {
        _DEVMODE = _state;
    }

    public static void setGodMode(boolean _state)
    {
        _GOD_MODE = _state;
    }

    public static void setAndroidOnDesktop(boolean _state)
    {
        _ANDROID_ON_DESKTOP = _state;
    }

    public static boolean isDevMode()
    {
        return !AppSystem.isAndroidApp() && _DEVMODE;
    }

    public static boolean isGodMode()
    {
        return !AppSystem.isAndroidApp() && _GOD_MODE;
    }

    public static boolean isAndroidOnDesktop()
    {
        return _ANDROID_ON_DESKTOP;
    }
}

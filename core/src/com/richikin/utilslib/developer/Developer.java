package com.richikin.utilslib.developer;

import com.richikin.utilslib.config.AppSystem;

public abstract class Developer
{
    public static boolean developerPanelActive = false;

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

    public void setDevMode(boolean _state)
    {
        _DEVMODE = _state;
    }

    public void setGodMode(boolean _state)
    {
        _GOD_MODE = _state;
    }

    public void setAndroidOnDesktop(boolean _state)
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

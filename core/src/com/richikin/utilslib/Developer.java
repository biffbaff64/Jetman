package com.richikin.utilslib;

public abstract class Developer
{
    public static boolean developerPanelActive = false;

    private static boolean isAndroidOnDesktop = false;
    private static boolean isGodMode          = false;
    private static boolean isDevMode          = false;

    /**
     * Set _DEVMODE from the _DEV_MODE Environment variable.
     */
    public static void setMode()
    {
        if (AppSystem.isDesktopApp())
        {
            isDevMode = "TRUE".equals(System.getenv("_DEV_MODE").toUpperCase());
        }
    }

    public static void setDevMode(boolean _state)
    {
        isDevMode = _state;
    }

    public static void setGodMode(boolean _state)
    {
        isGodMode = _state;
    }

    public static void setAndroidOnDesktop(boolean _state)
    {
        isAndroidOnDesktop = _state;
    }

    public static boolean isDevMode()
    {
        return !AppSystem.isAndroidApp() && isDevMode;
    }

    public static boolean isGodMode()
    {
        return !AppSystem.isAndroidApp() && isGodMode;
    }

    public static boolean isAndroidOnDesktop()
    {
        return isAndroidOnDesktop;
    }
}

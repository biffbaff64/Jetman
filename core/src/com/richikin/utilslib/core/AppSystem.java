package com.richikin.utilslib.core;

import com.richikin.utilslib.input.Switch;

public class AppSystem
{
    public static Switch fullScreenButton;
    public static Switch systemBackButton;

    public static void initialise()
    {
        fullScreenButton = new Switch();
        systemBackButton = new Switch();
    }
}

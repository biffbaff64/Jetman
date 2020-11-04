
package com.richikin.utilslib.input.controllers;

import com.badlogic.gdx.controllers.PovDirection;

public class ControllerData
{
    // =================================================================
    //
    public static int           controllerButtonCode;
    public static int           controllerAxisCode;
    public static float         controllerAxisValue;

    public static PovDirection  controllerPovDirection;
    public static int           controllerPovCode;

    public static boolean       controllerStartPressed;
    public static boolean       controllerFirePressed;
    public static boolean       controllerAPressed;
    public static boolean       controllerBPressed;
    public static boolean       controllerXPressed;
    public static boolean       controllerYPressed;
    public static boolean       controllerLBPressed;
    public static boolean       controllerRBPressed;
    public static boolean       controllerBackPressed;
    public static boolean       controllerLeftFirePressed;
    public static boolean       controllerRightFirePressed;

    public static boolean       controllerUpPressed;
    public static boolean       controllerDownPressed;
    public static boolean       controllerLeftPressed;
    public static boolean       controllerRightPressed;

    public static void setup()
    {
        controllerAPressed          = false;
        controllerBPressed          = false;
        controllerXPressed          = false;
        controllerYPressed          = false;
        controllerLBPressed         = false;
        controllerRBPressed         = false;
        controllerStartPressed      = false;
        controllerBackPressed       = false;
        controllerLeftFirePressed   = false;
        controllerRightFirePressed  = false;
        controllerFirePressed       = false;

        controllerButtonCode        = -1;
    }
}

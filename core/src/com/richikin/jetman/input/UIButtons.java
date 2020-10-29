
package com.richikin.jetman.input;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.PovDirection;
import com.richikin.jetman.core.App;
import com.richikin.utilslib.input.Switch;

// TODO: 12/12/2019 - This class confuses me, WHY do i have this?

public class UIButtons
{
    // =================================================================
    // DEFAULT Keyboard options.
    //
    public static final int defaultValueUp          = Input.Keys.W;
    public static final int defaultValueDown        = Input.Keys.S;
    public static final int defaultValueLeft        = Input.Keys.A;
    public static final int defaultValueRight       = Input.Keys.D;
    public static final int defaultValueA           = Input.Keys.NUMPAD_2;
    public static final int defaultValueB           = Input.Keys.NUMPAD_6;
    public static final int defaultValueX           = Input.Keys.NUMPAD_1;
    public static final int defaultValueY           = Input.Keys.NUMPAD_5;
    public static final int defaultValueHudInfo     = Input.Keys.F9;
    public static final int defaultValuePause       = Input.Keys.ESCAPE;
    public static final int defaultValueSettings    = Input.Keys.F10;

    // =================================================================
    //
    public static Switch fullScreenButton;
    public static Switch systemBackButton;

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

    public static void setup(App app)
    {
        fullScreenButton = new Switch();
        systemBackButton = new Switch();

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

    public static void releaseAll()
    {
        if (fullScreenButton != null)   { fullScreenButton.release(); }
        if (systemBackButton != null)   { systemBackButton.release(); }
    }
}

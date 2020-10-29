
package com.richikin.utilslib.input;

public abstract interface GDXButton
{
    void update();

    boolean checkPress(int touchX, int touchY);

    boolean checkRelease(int touchX, int touchY);

    void press();

    boolean isPressed();

    boolean isDisabled();

    void setDisabled(boolean _state);

    boolean isDrawable();

    void setDrawable(boolean _state);

    void release();

    void toggleDisabled();

    void togglePressed();
}

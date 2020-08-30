
package com.richikin.jetman.input.buttons;

public abstract interface GDXButton
{
    void update();

    void press();

    boolean isPressed();

    boolean isDisabled();

    void setDisabled(boolean _state);

    boolean isVisible();

    void setVisible(boolean _state);

    void release();

    void toggleDisabled();

    void togglePressed();
}

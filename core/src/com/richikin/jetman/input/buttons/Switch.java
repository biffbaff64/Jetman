
package com.richikin.jetman.input.buttons;

/**
 * A Simple ON/OFF Switch class
 */
public class Switch implements GDXButton
{
    private boolean _isPressed;
    private boolean _isDisabled;
    private int     _pointer;

    public Switch()
    {
        _isPressed  = false;
        _isDisabled = false;
    }

    @Override
    public void update()
    {
    }

    @Override
    public void press()
    {
        if (!_isDisabled)
        {
            _isPressed = true;
        }
    }

    @Override
    public void release()
    {
        _isPressed = false;
    }

    @Override
    public boolean isPressed()
    {
        return _isPressed;
    }

    @Override
    public boolean isDisabled()
    {
        return _isDisabled;
    }

    @Override
    public void setDisabled(boolean _state)
    {
        _isDisabled = _state;
    }

    @Override
    public void setVisible(boolean _state)
    {
    }

    @Override
    public boolean isVisible()
    {
        return false;
    }

    @Override
    public void toggleDisabled()
    {
        _isDisabled = !_isDisabled;
    }

    @Override
    public void togglePressed()
    {
        _isPressed = !_isPressed;
    }
}

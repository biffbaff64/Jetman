
package com.richikin.utilslib.input;

/**
 * A Simple ON/OFF Switch class
 */
public class Switch implements IGDXButton
{
    protected boolean _isPressed;
    protected boolean _isDisabled;

    public Switch()
    {
        _isPressed  = false;
        _isDisabled = false;
    }

    @Override
    public boolean checkPress(int touchX, int touchY)
    {
        return false;
    }

    @Override
    public boolean checkRelease(int touchX, int touchY)
    {
        return false;
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
    public void pressConditional(boolean condition)
    {
        _isPressed = condition;
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
    public void setDrawable(boolean _state)
    {
    }

    @Override
    public boolean isDrawable()
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

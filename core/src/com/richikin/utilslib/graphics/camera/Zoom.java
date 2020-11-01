
package com.richikin.utilslib.graphics.camera;

import com.richikin.utilslib.physics.Movement;

public class Zoom
{
    public static final float _DEFAULT_ZOOM = 1.0f;
    public static final float _INITIAL_ZOOM = 0.0f;

    private       int     direction;
    private       float   zoomValue;
    private       float   resetValue;
    private       float   target;
    private final boolean bounce;

    public Zoom()
    {
        this.zoomValue  = _INITIAL_ZOOM;
        this.resetValue = _INITIAL_ZOOM;
        this.target     = _DEFAULT_ZOOM;
        this.bounce     = false;
        this.direction  = Movement._DIRECTION_STILL;
    }

    public boolean update(float zoom)
    {
        boolean done = false;

        if (direction == Movement._DIRECTION_UP)
        {
            if (zoomValue < target)
            {
                zoomValue += zoom;
            }
            else
            {
                if (bounce)
                {
                    direction = Movement._DIRECTION_DOWN;
                }

                done = true;
            }
        }
        else if (direction == Movement._DIRECTION_DOWN)
        {
            if (zoomValue > target)
            {
                zoomValue -= zoom;
            }
            else
            {
                if (bounce)
                {
                    direction = Movement._DIRECTION_UP;
                }

                done = true;
            }
        }

        return done;
    }

    public void stop()
    {
        zoomValue = resetValue;
    }

    public void in(float zoom)
    {
        zoomValue += zoom;
    }

    public void out(float zoom)
    {
        zoomValue -= zoom;
    }

    public float getZoomValue()
    {
        return zoomValue;
    }

    public void setZoomValue(float _zoom)
    {
        zoomValue = _zoom;
    }

    public void setTarget(final float _target)
    {
        this.target = _target;
    }

    public void setResetValue(float _reset)
    {
        resetValue = _reset;
    }
}

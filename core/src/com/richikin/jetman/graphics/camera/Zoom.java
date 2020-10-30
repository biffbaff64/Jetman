
package com.richikin.jetman.graphics.camera;

import com.richikin.jetman.graphics.Gfx;
import com.richikin.utilslib.physics.Movement;

public class Zoom
{
    private static final float _DEFAULT_ZOOM = 0.0f;

    private       int     direction;
    private       float   zoomValue;
    private       float   resetValue;
    private       float   target;
    private final boolean bounce;

    public Zoom()
    {
        this.zoomValue  = _DEFAULT_ZOOM;
        this.resetValue = _DEFAULT_ZOOM;
        this.target     = Gfx._DEFAULT_ZOOM;
        this.bounce     = false;
        this.direction  = Movement._DIRECTION_STILL;
    }

    public boolean update(float zoom)
    {
        boolean done = false;

        if(direction == Movement._DIRECTION_UP)
        {
            if(zoomValue < target)
            {
                zoomValue += zoom;
            }
            else
            {
                if(bounce)
                {
                    direction = Movement._DIRECTION_DOWN;
                }

                done = true;
            }
        }
        else if(direction == Movement._DIRECTION_DOWN)
        {
            if(zoomValue > target)
            {
                zoomValue -= zoom;
            }
            else
            {
                if(bounce)
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

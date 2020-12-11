
package com.richikin.jetman.physics;

import com.richikin.utilslib.maths.SimpleVec2F;

public class Speed extends SimpleVec2F
{
    public Speed()
    {
        super();
    }

    public Speed(float x, float y)
    {
        super(x, y);
    }

    public Speed(Speed speed)
    {
        set(speed);
    }
}

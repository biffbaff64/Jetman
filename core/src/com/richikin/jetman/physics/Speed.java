
package com.richikin.jetman.physics;

import com.richikin.utilslib.maths.SimpleVec2F;
import org.jetbrains.annotations.NotNull;

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

    public Speed(Speed _speed)
    {
        set(_speed);
    }

    public void set(@NotNull Speed refSpeed)
    {
        this.x = refSpeed.x;
        this.y = refSpeed.y;
    }
}

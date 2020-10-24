package com.richikin.utilslib.physics;

import com.richikin.utilslib.maths.SimpleVec2;
import org.jetbrains.annotations.NotNull;

public class Direction extends SimpleVec2
{
    public Direction()
    {
        super();
    }

    public Direction(int _x, int _y)
    {
        super(_x, _y);
    }

    public Direction(Direction _direction)
    {
        this.x = _direction.x;
        this.y = _direction.y;
    }

    public void set(Direction _direction)
    {
        this.x = _direction.x;
        this.y = _direction.y;
    }

    public void standStill()
    {
        this.x = Movement._DIRECTION_STILL;
        this.y = Movement._DIRECTION_STILL;
    }

    public boolean hasDirection()
    {
        return (this.x != Movement._DIRECTION_STILL) || (this.y != Movement._DIRECTION_STILL);
    }

    public int getFlippedX()
    {
        return this.x * -1;
    }

    public int getFlippedY()
    {
        return this.y * -1;
    }

    public void toggle()
    {
        if (this.x != Movement._DIRECTION_STILL)
        {
            toggleX();
        }

        if (this.y != Movement._DIRECTION_STILL)
        {
            toggleY();
        }
    }

    public void toggleX()
    {
        this.x *= -1;
    }

    public void toggleY()
    {
        this.y *= -1;
    }

    @NotNull
    @Override
    public String toString()
    {
        return Movement.getAliasX(x) + ", " + Movement.getAliasY(y);
    }
}

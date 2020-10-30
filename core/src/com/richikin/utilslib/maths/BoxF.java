
package com.richikin.utilslib.maths;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

@SuppressWarnings({"WeakerAccess"})
public class BoxF
{
    public float x;
    public float y;
    public float width;
    public float height;

    public BoxF()
    {
        this(0, 0, 0, 0);
    }

    public BoxF(BoxF box)
    {
        this(box.x, box.y, box.width, box.height);
    }

    public BoxF(Rectangle rectangle)
    {
        this((int) rectangle.x, (int) rectangle.y, (int) rectangle.width, (int) rectangle.height);
    }

    public BoxF(float _width, float _height)
    {
        this(0, 0, _width, _height);
    }

    public BoxF(float _x, float _y, float _width, float _height)
    {
        this.x = _x;
        this.y = _y;
        this.width = _width;
        this.height = _height;
    }

    public void set(float _x, float _y, float _width, float _height)
    {
        this.x = _x;
        this.y = _y;
        this.width = _width;
        this.height = _height;
    }

    public boolean contains(float _x, float _y)
    {
        return (this.x <= _x) && ((this.x + this.width) >= _x)
            && (this.y <= _y) && ((this.y + this.height) >= _y);
    }

    public boolean contains(Vector2 point)
    {
        return contains(point.x, point.y);
    }

    public boolean contains(Rectangle rectangle)
    {
        float xmin = rectangle.x;
        float xmax = xmin + rectangle.width;

        float ymin = rectangle.y;
        float ymax = ymin + rectangle.height;

        return (((xmin > x) && (xmin < (x + width))) && ((xmax > x) && (xmax < (x + width))))
            && (((ymin > y) && (ymin < (y + height))) && ((ymax > y) && (ymax < (y + height))));
    }

    public Rectangle getRectangle()
    {
        return new Rectangle(x, y, width, height);
    }

    @Override
    public String toString()
    {
        return "[" + x + "," + y + "," + width + "," + height + "]";
    }
}


package com.richikin.utilslib.maths;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public class Box
{
    public int x;
    public int y;
    public int width;
    public int height;

    public Box()
    {
        this(0, 0, 0, 0);
    }

    public Box(Box box)
    {
        this(box.x, box.y, box.width, box.height);
    }

    public Box(Rectangle _rectangle)
    {
        this.x = (int) _rectangle.x;
        this.y = (int) _rectangle.y;
        this.width = (int) _rectangle.width;
        this.height = (int) _rectangle.height;
    }

    public Box(int _width, int _height)
    {
        this(0, 0, _width, _height);
    }

    public Box(int _x, int _y, int _width, int _height)
    {
        this.x = _x;
        this.y = _y;
        this.width = _width;
        this.height = _height;
    }

    public void set(int _x, int _y, int _width, int _height)
    {
        this.x = _x;
        this.y = _y;
        this.width = _width;
        this.height = _height;
    }

    public void set(Box _box)
    {
        this.x = _box.x;
        this.y = _box.y;
        this.width = _box.width;
        this.height = _box.height;
    }

    public boolean contains(float _x, float _y)
    {
        return (this.x <= _x) && ((this.x + this.width) >= _x)
            && (this.y <= _y) && ((this.y + this.height) >= _y);
    }

    public boolean contains(Vector2 point)
    {
        return contains((int) point.x, (int) point.y);
    }

    public boolean contains(Rectangle _rectangle)
    {
        float xmin = _rectangle.x;
        float xmax = xmin + _rectangle.width;

        float ymin = _rectangle.y;
        float ymax = ymin + _rectangle.height;

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

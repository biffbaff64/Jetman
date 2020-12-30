package com.richikin.utilslib.pooling;

import com.badlogic.gdx.math.Rectangle;
import com.richikin.enumslib.GraphicID;

public interface IObjectPoolFactory<T>
{
    T createObject();

    T createObject(Rectangle _rectangle);

    T createObject(int x, int y, int width, int height, GraphicID type);
}


package com.richikin.utilslib.pooling;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.richikin.enumslib.GraphicID;

public class ObjectPool<T>
{
    private final Array<T>              freeObjects;
    private final IObjectPoolFactory<T> factory;
    private final int                   maxSize;

    public ObjectPool(IObjectPoolFactory<T> factory, int maxSize)
    {
        this.factory = factory;
        this.maxSize = maxSize;
        this.freeObjects = new Array<>(maxSize);
    }

    public T newObject()
    {
        T object;

        if (freeObjects.size == 0)
        {
            object = factory.createObject();
        }
        else
        {
            object = freeObjects.removeIndex(freeObjects.size - 1);
        }

        return object;
    }

    public T newObject(Rectangle rectangle)
    {
        T object;

        if (freeObjects.size == 0)
        {
            object = factory.createObject(rectangle);
        }
        else
        {
            object = freeObjects.removeIndex(freeObjects.size - 1);
        }

        return object;
    }

    public T newObject(int x, int y, int width, int height, GraphicID graphicID)
    {
        T object;

        if (freeObjects.size == 0)
        {
            object = factory.createObject(x, y, width, height, graphicID);
        }
        else
        {
            object = freeObjects.removeIndex(freeObjects.size - 1);
        }

        return object;
    }

    public void free(T object)
    {
        if (object != null)
        {
            if (freeObjects.size < maxSize)
            {
                freeObjects.add(object);
            }
        }
    }
}

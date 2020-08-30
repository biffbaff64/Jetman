
package com.richikin.jetman.utils.pooling;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.richikin.jetman.graphics.GraphicID;

public class ObjectPool<T>
{
    public interface ObjectPoolFactory<T>
    {
        T createObject();

        T createObject(Rectangle rectangle);

        T createObject(int x, int y, int width, int height, GraphicID type);
    }

    private final Array<T>             freeObjects;
    private final ObjectPoolFactory<T> factory;
    private final int                  maxSize;

    public ObjectPool(ObjectPoolFactory<T> factory, int maxSize)
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

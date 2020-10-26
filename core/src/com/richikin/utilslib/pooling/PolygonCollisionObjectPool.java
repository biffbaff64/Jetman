
package com.richikin.utilslib.pooling;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.utilslib.LibApp;

public class PolygonCollisionObjectPool<T>
{
    public interface ObjectPoolFactory<T>
    {
        T createObject(LibApp _app);

        T createObject(Rectangle _rectangle, LibApp _app);

        T createObject(int x, int y, int width, int height, GraphicID type, LibApp _app);

        void finaliseObject();
    }

    private final Array<T>             freeObjects;
    private final ObjectPoolFactory<T> factory;
    private final int                  maxSize;

    public PolygonCollisionObjectPool(ObjectPoolFactory<T> _factory, int _maxSize)
    {
        this.factory = _factory;
        this.maxSize = _maxSize;
        this.freeObjects = new Array<>(_maxSize);
    }

    public T newObject(LibApp _app)
    {
        T object;

        if (freeObjects.size == 0)
        {
            object = factory.createObject(_app);
        }
        else
        {
            object = freeObjects.removeIndex(freeObjects.size - 1);
        }

        return object;
    }

    public T newObject(Rectangle _rectangle, LibApp _app)
    {
        T object;

        if (freeObjects.size == 0)
        {
            object = factory.createObject(_rectangle, _app);
        }
        else
        {
            object = freeObjects.removeIndex(freeObjects.size - 1);
        }

        return object;
    }

    public T newObject(int x, int y, int width, int height, GraphicID type, LibApp _app)
    {
        T object;

        if (freeObjects.size == 0)
        {
            object = factory.createObject(x, y, width, height, type, _app);
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
            factory.finaliseObject();

            if (freeObjects.size < maxSize)
            {
                freeObjects.add(object);
            }
        }
    }
}

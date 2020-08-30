
package com.richikin.jetman.utils.pooling;

public class ObjectMapEntry<T>
{
    public final String key;
    public final T objectClass;

    public ObjectMapEntry(String key, T objectClass)
    {
        this.key = key;
        this.objectClass = objectClass;
    }
}

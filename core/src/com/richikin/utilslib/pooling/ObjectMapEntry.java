
package com.richikin.utilslib.pooling;

// TODO: 03/12/2020 - Why did i create this and why is it in Pooling??
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

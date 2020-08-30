package com.richikin.jetman.maps;

public class ObjectTileProperties
{
    public boolean isSizeBoxNeeded;
    public boolean hasDirection;
    public boolean hasDistance;
    public boolean hasSpeed;
    public boolean isLinked;

    public ObjectTileProperties()
    {
        isSizeBoxNeeded = false;
        hasDirection    = false;
        hasDistance     = false;
        hasSpeed        = false;
        isLinked        = false;
    }
}

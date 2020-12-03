package com.richikin.jetman.graphics;

import com.richikin.enumslib.GraphicID;

public class GraphicIndex
{
    public final GraphicID graphicID;
    public       int value;

    public GraphicIndex(GraphicID _gid, int _value)
    {
        this.graphicID = _gid;
        this.value     = _value;
    }
}

package com.richikin.utilslib.graphics;

import com.richikin.enumslib.GraphicID;

public class GraphicIndex
{
    public final GraphicID graphicID;
    public       int value;

    public GraphicIndex(GraphicID gid, int value)
    {
        this.graphicID = gid;
        this.value     = value;
    }
}

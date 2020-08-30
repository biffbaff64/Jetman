
package com.richikin.jetman.entities.objects;

import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.maps.TileID;

public class EntityDef
{
    public String    objectName;
    public GraphicID graphicID;
    public TileID    tileID;
    public String    asset;
    public int       frames;
    public GraphicID type;

    public EntityDef()
    {
        this.objectName = "";
        this.graphicID  = GraphicID.G_NO_ID;
        this.type       = GraphicID.G_NO_ID;
        this.tileID     = TileID._DEFAULT_TILE;
        this.asset      = "";
        this.frames     = 0;
    }

    public EntityDef(String _objectName,
                     GraphicID _graphicID,
                     TileID _tileID,
                     String _asset,
                     int _frames,
                     GraphicID _type)
    {
        this.objectName = _objectName;
        this.graphicID  = _graphicID;
        this.type       = _type;
        this.tileID     = _tileID;
        this.asset      = _asset;
        this.frames     = _frames;
    }
}


package com.richikin.jetman.assets;

import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.maps.TileID;
import com.richikin.jetman.maths.SimpleVec2;

/**
 * Asset description class.
 */
public class AssetDef
{
    public String     asset;        // The asset filename
    public GraphicID  graphicID;    // Asset Identity
    public GraphicID  type;         // Asset Type - _ENTITY, _OBSTACLE, etc
    public TileID     tileID;       // Associated marker tile
    public SimpleVec2 size;         // Asset size, or Frame size for multiple frame assets
    public int        frames;       // Number of animation frames

    public AssetDef()
    {
        this.graphicID  = GraphicID.G_NO_ID;
        this.type       = GraphicID.G_NO_ID;
        this.tileID     = TileID._DEFAULT_TILE;
        this.asset      = "";
        this.size       = new SimpleVec2();
        this.frames     = 0;
    }

    public AssetDef(String _asset,
                    GraphicID _graphicID,
                    GraphicID _type,
                    TileID _tileID,
                    int _width, int _height,
                    int _frames)
    {
        this.asset      = _asset;
        this.graphicID  = _graphicID;
        this.type       = _type;
        this.tileID     = _tileID;
        this.size       = new SimpleVec2(_width, _height);
        this.frames     = _frames;
    }
}

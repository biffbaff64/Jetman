
package com.richikin.jetman.assets;

import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.maths.SimpleVec2;

/**
 * Matches graphics asset width & height
 * to a Graphic ID.
 */
public class AssetSize
{
    public final GraphicID  graphicID;
    public final SimpleVec2 size;

    public AssetSize(GraphicID _gid, int _width, int _height)
    {
        this.graphicID  = _gid;
        this.size       = new SimpleVec2(_width, _height);
    }
}

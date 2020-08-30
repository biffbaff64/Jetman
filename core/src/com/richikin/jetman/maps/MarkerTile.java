
package com.richikin.jetman.maps;

import com.badlogic.gdx.utils.Disposable;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.maths.Box;
import com.richikin.jetman.maths.SimpleVec2F;
import com.richikin.jetman.physics.Direction;
import com.richikin.jetman.physics.Speed;
import com.richikin.jetman.utils.logging.Trace;

@SuppressWarnings("NonConstantFieldWithUpperCaseName")
//@formatter:off
public class MarkerTile implements Disposable
{
    public int         _X;         // X Pos of tile, in TileWidth units
    public int         _Y;         // Y Pos of tile, in TileWidth units
    public GraphicID   _GID;       // The GraphicID of the entity this tile applies to
    public Direction   _DIR;       //
    public SimpleVec2F _DIST;      //
    public Speed       _SPEED;     //
    public int         _LINK;      //
    public Box         _BOX;       // TODO: 05/01/2019 - To be Removed
    public TileID      _TILE;      // TODO: 05/01/2019 - To be Removed
    public String      _ASSET;     // TODO: 05/01/2019 - To be Removed
    public int         _INDEX;     // TODO: 05/01/2019 - To be Removed

    /**
     * Default Constructor
     */
    public MarkerTile()
    {
        this(0, 0, TileID._NO_ACTION_TILE, GraphicID.G_NO_ID, 0, null);
    }

    /**
     * Constructor.
     * Creates a marker tile with the specified GraphicID
     *
     * @param _gid The required {@link GraphicID}
     */
    public MarkerTile(GraphicID _gid)
    {
        this(0, 0, TileID._DEFAULT_TILE, _gid, 0, null);
    }

    /**
     * Constructor.
     * Creates a marker tile with the specified information
     *
     * @param x      X Position of the tile
     * @param y      Y Position of the tile
     * @param tileID The required tile ID
     * @param _gid   The required Graphic ID
     * @param index  This tiles position in the MarkerTile map
     * @param asset  The asset to use for the created entity
     */
    public MarkerTile(int x, int y, TileID tileID, GraphicID _gid, int index, String asset)
    {
        this._X     = x;
        this._Y     = y;
        this._TILE  = tileID;
        this._GID   = _gid;
        this._ASSET = asset;
        this._INDEX = index;
        this._LINK  = 0;
    }

    /**
     * Dump debug information to Logcat
     */
    public void debug()
    {
        Trace.__FILE_FUNC_WithDivider();

        Trace.dbg("_X     : " + _X);
        Trace.dbg("_Y     : " + _Y);
        Trace.dbg("_GID   : " + _GID);
        Trace.dbg("_TILE  : " + _TILE);
        Trace.dbg("_INDEX : " + _INDEX);
        Trace.dbg("_ASSET : " + _ASSET);

        if (_DIR != null)
        {
            Trace.dbg("_DIR   : " + _DIR.toString());
        }

        if (_DIST != null)
        {
            Trace.dbg("_DIST  : " + _DIST.toString());
        }

        if (_SPEED != null)
        {
            Trace.dbg("_SPEED : " + _SPEED.toString());
        }
    }

    @Override
    public void dispose()
    {
        _GID   = null;
        _TILE  = null;
        _ASSET = null;
        _DIR   = null;
        _DIST  = null;
        _SPEED = null;
    }
}

package com.richikin.jetman.entities.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.richikin.enumslib.GraphicID;
import com.richikin.enumslib.TileID;
import com.richikin.utilslib.maths.Box;
import com.richikin.utilslib.maths.SimpleVec2;
import com.richikin.utilslib.maths.SimpleVec2F;
import com.richikin.utilslib.maths.SimpleVec3;
import com.richikin.utilslib.physics.Direction;
import com.richikin.utilslib.logging.Trace;

/**
 * Used for storing relevant information for
 * creating, placing and initialising sprites.
 */
// TODO: 06/02/2021 - Fix naming issues
public class SpriteDescriptor
{
    public String             _NAME;         // MUST Match the name assigned in TiledMap.
    public GraphicID          _GID;          // ID. See GraphicID class for options.
    public TileID             _TILE;         //
    public String             _ASSET;        // The initial image asset.
    public int                _FRAMES;       // Number of frames in the asset above.
    public GraphicID          _TYPE;         // _MAIN, _INTERACTIVE, _PICKUP etc
    public SimpleVec3         _POSITION;     // X Pos of tile, in TileWidth units
                                             // Y Pos of tile, in TileWidth units
                                             // Z-Sort value.
    public SimpleVec2         _SIZE;         // Width and Height.
    public int                _INDEX;        // This entities position in the entity map.
    public Animation.PlayMode _PLAYMODE;     // Animation playmode for the asset frames above.
    public float              _ANIM_RATE;    // Animation speed
    public GdxSprite          _PARENT;       // Parent GDXSprite (if applicable).
    public int                _LINK;         // Linked GDXSprite (if applicable).
    public Direction          _DIR;          // Initial direction of travel.
    public SimpleVec2F        _DIST;         // Initial travel distance. Useful for moving blocks etc.
    public SimpleVec2F        _SPEED;        // Initial speed.
    public Box                _BOX;          //
    public boolean            _ENEMY;        //

    public SpriteDescriptor()
    {
        this._GID       = GraphicID.G_NO_ID;
        this._TYPE      = GraphicID.G_NO_ID;
        this._POSITION  = new SimpleVec3();
        this._SIZE      = new SimpleVec2();
        this._INDEX     = 0;
        this._FRAMES    = 0;
        this._PLAYMODE  = Animation.PlayMode.NORMAL;
        this._ANIM_RATE = 1.0f;
        this._NAME      = "";
        this._ASSET     = "";
        this._LINK      = 0;
        this._TILE      = TileID._DEFAULT_TILE;
        this._PARENT    = null;
        this._DIR       = null;
        this._DIST      = null;
        this._SPEED     = null;
        this._BOX       = null;
        this._ENEMY     = false;
    }

    public SpriteDescriptor(String objectName,
                            GraphicID graphicID,
                            GraphicID type,
                            String asset,
                            int frames,
                            TileID tileID)
    {
        this();

        this._NAME   = objectName;
        this._GID    = graphicID;
        this._TILE   = tileID;
        this._ASSET  = asset;
        this._FRAMES = frames;
        this._TYPE   = type;
    }

    public SpriteDescriptor(String objectName,
                            GraphicID graphicID,
                            GraphicID type,
                            String asset,
                            int frames,
                            Animation.PlayMode playMode,
                            TileID tileID)
    {
        this(objectName, graphicID, type, asset, frames, tileID);
        this._PLAYMODE = playMode;
    }

    public SpriteDescriptor(SpriteDescriptor descriptor)
    {
        this._GID       = descriptor._GID;
        this._TYPE      = descriptor._TYPE;
        this._POSITION  = descriptor._POSITION;
        this._SIZE      = descriptor._SIZE;
        this._INDEX     = descriptor._INDEX;
        this._FRAMES    = descriptor._FRAMES;
        this._PLAYMODE  = descriptor._PLAYMODE;
        this._ANIM_RATE = descriptor._ANIM_RATE;
        this._NAME      = descriptor._NAME;
        this._ASSET     = descriptor._ASSET;
        this._LINK      = descriptor._LINK;
        this._TILE      = descriptor._TILE;
        this._PARENT    = descriptor._PARENT;
        this._DIR       = descriptor._DIR;
        this._DIST      = descriptor._DIST;
        this._SPEED     = descriptor._SPEED;
        this._BOX       = descriptor._BOX;
        this._ENEMY     = descriptor._ENEMY;
    }

    public void debug()
    {
        Trace.__FILE_FUNC_WithDivider();
        Trace.dbg("_GID            : " + _GID);
        Trace.dbg("_TYPE           : " + _TYPE);
        Trace.dbg("_POSITION       : " + (_POSITION != null ? _POSITION.toString() : "NOT SET"));
        Trace.dbg("_SIZE           : " + (_SIZE != null ? _SIZE.toString() : "NOT SET"));
        Trace.dbg("_INDEX          : " + _INDEX);
        Trace.dbg("_FRAMES         : " + _FRAMES);
        Trace.dbg("_PLAYMODE       : " + _PLAYMODE);
        Trace.dbg("_ANIM_RATE      : " + _ANIM_RATE);
        Trace.dbg("_NAME           : " + (_NAME != null ? _NAME : "NOT SET"));
        Trace.dbg("_ASSET          : " + (_ASSET != null ? _ASSET : "NOT SET"));
        Trace.dbg("_LINK           : " + _LINK);
        Trace.dbg("_TILE           : " + _TILE);
        Trace.dbg("_PARENT         : " + _PARENT);
        Trace.dbg("_DIR            : " + (_DIR != null ? _DIR.toString() : "NOT SET"));
        Trace.dbg("_DIST           : " + (_DIST != null ? _DIST.toString() : "NOT SET"));
        Trace.dbg("_SPEED          : " + (_SPEED != null ? _SPEED.toString() : "NOT SET"));
        Trace.dbg("_BOX            : " + (_BOX != null ? _BOX.toString() : "NOT SET"));
        Trace.dbg("_ENEMY          : " + _ENEMY);
    }
}

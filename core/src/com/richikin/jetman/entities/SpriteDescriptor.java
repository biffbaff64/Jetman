package com.richikin.jetman.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.maps.TileID;
import com.richikin.jetman.maths.Box;
import com.richikin.jetman.maths.SimpleVec2;
import com.richikin.jetman.maths.SimpleVec2F;
import com.richikin.jetman.maths.SimpleVec3;
import com.richikin.jetman.physics.Direction;
import com.richikin.jetman.physics.Speed;

/**
 * Used for storing relevant information for
 * creating, placing and initialising sprites.
 */
public class SpriteDescriptor
{
    public String             _NAME;         //
    public GraphicID          _GID;          // ID
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
    public Speed              _SPEED;        // Initial speed.
    public Box                _BOX;          //

    public SpriteDescriptor()
    {
        this._GID       = GraphicID.G_NO_ID;
        this._TYPE      = GraphicID.G_NO_ID;
        this._POSITION  = new SimpleVec3();
        this._SIZE      = new SimpleVec2();
        this._INDEX     = 0;
        this._FRAMES    = 0;
        this._PLAYMODE  = Animation.PlayMode.NORMAL;
        this._ANIM_RATE = 0f;
        this._NAME      = "";
        this._ASSET     = "";
        this._LINK      = 0;
        this._TILE      = TileID._DEFAULT_TILE;
        this._PARENT    = null;
        this._DIR       = null;
        this._DIST      = null;
        this._SPEED     = null;
        this._BOX       = null;
    }

    public SpriteDescriptor(String _objectName,
                            GraphicID _graphicID,
                            GraphicID _type,
                            String _asset,
                            int _frames,
                            TileID _tileID)
    {
        this();

        this._NAME   = _objectName;
        this._GID    = _graphicID;
        this._TILE   = _tileID;
        this._ASSET  = _asset;
        this._FRAMES = _frames;
        this._TYPE   = _type;
    }

    public SpriteDescriptor(SpriteDescriptor _descriptor)
    {
        this._GID       = _descriptor._GID;
        this._TYPE      = _descriptor._TYPE;
        this._POSITION  = _descriptor._POSITION;
        this._SIZE      = _descriptor._SIZE;
        this._INDEX     = _descriptor._INDEX;
        this._FRAMES    = _descriptor._FRAMES;
        this._PLAYMODE  = _descriptor._PLAYMODE;
        this._ANIM_RATE = _descriptor._ANIM_RATE;
        this._NAME      = _descriptor._NAME;
        this._ASSET     = _descriptor._ASSET;
        this._LINK      = _descriptor._LINK;
        this._TILE      = _descriptor._TILE;
        this._PARENT    = _descriptor._PARENT;
        this._DIR       = _descriptor._DIR;
        this._DIST      = _descriptor._DIST;
        this._SPEED     = _descriptor._SPEED;
        this._BOX       = _descriptor._BOX;
    }
}

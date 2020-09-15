package com.richikin.jetman.entities.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.richikin.jetman.entities.GdxSprite;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.maths.SimpleVec2;
import com.richikin.jetman.maths.SimpleVec2F;
import com.richikin.jetman.physics.Direction;
import com.richikin.jetman.physics.Speed;
import com.richikin.jetman.utils.logging.Trace;

public class EntityDescriptor
{
    public GraphicID          _GID;             // ID
    public int                _X;               // X Coordinate.
    public int                _Y;               // Y Coordinate.
    public int                _Z;               // Z Coordinate.
    public int                _INDEX;           // This entities position in the entity map.
    public TextureRegion      _ASSET;           // The initial image asset.
    public int                _FRAMES;          // Number of frames in the asset above.
    public Animation.PlayMode _PLAYMODE;        // Animation playmode for the asset frames above.
    public float              _ANIM_RATE;       // Animation speed
    public GdxSprite          _PARENT;          // Parent GDXSprite (if applicable).
    public SimpleVec2         _SIZE;            // Width and Height.
    public int                _LINK;            // Linked GDXSprite (if applicable).
    public Direction          _DIR;             // Initial direction of travel.
    public SimpleVec2F        _DIST;            // Initial travel distance. Useful for moving blocks etc.
    public Speed              _SPEED;           // Initial speed.

    public EntityDescriptor()
    {
        _GID       = GraphicID.G_NO_ID;
        _X         = 0;
        _Y         = 0;
        _Z         = 0;
        _INDEX     = 0;
        _ASSET     = null;
        _FRAMES    = 0;
        _PLAYMODE  = Animation.PlayMode.NORMAL;
        _ANIM_RATE = 1.0f / 6f;
        _PARENT    = null;
        _SIZE      = null;
        _LINK      = 0;
        _DIR       = new Direction();
        _DIST      = new SimpleVec2F();
        _SPEED     = new Speed();
    }

    public EntityDescriptor(EntityDescriptor descriptor)
    {
        _GID       = descriptor._GID;
        _X         = descriptor._X;
        _Y         = descriptor._Y;
        _Z         = descriptor._Z;
        _INDEX     = descriptor._INDEX;
        _ASSET     = descriptor._ASSET;
        _FRAMES    = descriptor._FRAMES;
        _PLAYMODE  = descriptor._PLAYMODE;
        _ANIM_RATE = descriptor._ANIM_RATE;
        _PARENT    = descriptor._PARENT;
        _SIZE      = descriptor._SIZE;
        _LINK      = descriptor._LINK;
        _DIR       = descriptor._DIR;
        _DIST      = descriptor._DIST;
        _SPEED     = descriptor._SPEED;
    }

    public EntityDescriptor(int x, int y, int z, TextureRegion asset, int frames, Animation.PlayMode mode)
    {
        this();

        _X        = x;
        _Y        = y;
        _Z        = z;
        _ASSET    = asset;
        _FRAMES   = frames;
        _PLAYMODE = mode;
    }

    public EntityDescriptor(TextureRegion asset, int frames, Animation.PlayMode mode)
    {
        this();

        _ASSET    = asset;
        _FRAMES   = frames;
        _PLAYMODE = mode;
    }

    public EntityDescriptor(TextureRegion asset, int frames, Animation.PlayMode mode, SimpleVec2 size)
    {
        this();

        _ASSET    = asset;
        _FRAMES   = frames;
        _PLAYMODE = mode;
        _SIZE     = size;
    }

    public void debug()
    {
        Trace.__FILE_FUNC_WithDivider();
        Trace.dbg("_GID            : " + _GID);
        Trace.dbg("_X              : " + _X);
        Trace.dbg("_Y              : " + _Y);
        Trace.dbg("_Z              : " + _Z);
        Trace.dbg("_INDEX          : " + _INDEX);
        Trace.dbg("_ASSET          : " + _ASSET);
        Trace.dbg("_FRAMES         : " + _FRAMES);
        Trace.dbg("_PLAYMODE       : " + _PLAYMODE);
        Trace.dbg("_ANIM_RATE      : " + _ANIM_RATE);
        Trace.dbg("_PARENT         : " + _PARENT);
        Trace.dbg("_SIZE           : " + _SIZE);
        Trace.dbg("_LINK           : " + _LINK);
        Trace.dbg("_DIR            : " + _DIR.toString());
        Trace.dbg("_DIST           : " + _DIST.toString());
        Trace.dbg("_SPEED          : " + _SPEED.toString());
    }
}

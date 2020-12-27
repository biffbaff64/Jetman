
package com.richikin.jetman.physics.box2d;

import com.richikin.enumslib.GraphicID;
import com.richikin.jetman.entities.objects.GdxSprite;

public class BodyIdentity
{
    public final GraphicID gid;
    public final GraphicID type;
    public final GdxSprite entity;

    BodyIdentity(GdxSprite _entity, GraphicID _gid, GraphicID _type)
    {
        this.entity = _entity;
        this.gid = _gid;
        this.type = _type;
    }
}

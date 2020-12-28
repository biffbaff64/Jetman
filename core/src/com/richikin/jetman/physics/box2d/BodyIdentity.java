
package com.richikin.jetman.physics.box2d;

import com.richikin.enumslib.GraphicID;
import com.richikin.jetman.entities.objects.GdxSprite;

public class BodyIdentity
{
    public final GraphicID gid;
    public final GraphicID type;
    public final GdxSprite entity;

    public BodyIdentity(GdxSprite entity, GraphicID gid, GraphicID type)
    {
        this.entity = entity;
        this.gid = gid;
        this.type = type;
    }
}

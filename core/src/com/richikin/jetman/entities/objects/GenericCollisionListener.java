package com.richikin.jetman.entities.objects;

import com.richikin.enumslib.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.utilslib.physics.aabb.ICollisionListener;

public class GenericCollisionListener implements ICollisionListener
{
    private GdxSprite parent;

    public  GenericCollisionListener(GdxSprite _parent)
    {
        this.parent = _parent;
    }

    @Override
    public void onPositiveCollision(final GraphicID spriteHittingGid)
    {
        if ((spriteHittingGid == GraphicID.G_LASER) || (spriteHittingGid == GraphicID.G_ROVER_BULLET))
        {
            parent.setAction(ActionStates._KILLED);
        }
        else
        {
            if (spriteHittingGid == GraphicID.G_PLAYER)
            {
                if ((parent.gid != GraphicID.G_MISSILE_BASE) && (parent.gid != GraphicID.G_MISSILE_LAUNCHER)
                    && (parent.gid != GraphicID.G_DEFENDER))
                {
                    parent.setAction(ActionStates._HURT);
                }
            }
        }

        parent.collisionObject.setInvisibility(1000);
    }

    @Override
    public void onNegativeCollision()
    {
    }

    @Override
    public void dispose()
    {
        parent = null;
    }
}

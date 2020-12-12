package com.richikin.jetman.entities.objects;

import com.richikin.enumslib.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.jetman.physics.aabb.CollisionObject;
import com.richikin.jetman.physics.aabb.ICollisionListener;

public class GenericCollisionListener implements ICollisionListener
{
    private GdxSprite parent;

    public  GenericCollisionListener(GdxSprite _parent)
    {
        this.parent = _parent;
    }

    @Override
    public void onPositiveCollision(CollisionObject cobjHitting)
    {
//        if ((spriteHittingGid == GraphicID.G_LASER) || (spriteHittingGid == GraphicID.G_ROVER_BULLET))
        if (checkHittingPlayerWeapon(cobjHitting))
        {
            parent.setAction(ActionStates._KILLED);
        }
        else
        {
//            if (spriteHittingGid == GraphicID.G_PLAYER)
            if (checkHittingPlayer(cobjHitting))
            {
                parent.collisionObject.isHittingPlayer = true;

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

    private boolean checkHittingPlayerWeapon(CollisionObject cobjHitting)
    {
        boolean result = false;

        if (((cobjHitting.contactTop.gid == GraphicID.G_LASER) || (cobjHitting.contactTop.gid == GraphicID.G_ROVER_BULLET))
            || ((cobjHitting.contactBottom.gid == GraphicID.G_LASER) || (cobjHitting.contactBottom.gid == GraphicID.G_ROVER_BULLET))
            || ((cobjHitting.contactLeft.gid == GraphicID.G_LASER) || (cobjHitting.contactLeft.gid == GraphicID.G_ROVER_BULLET))
            || ((cobjHitting.contactRight.gid == GraphicID.G_LASER) || (cobjHitting.contactRight.gid == GraphicID.G_ROVER_BULLET)))
        {
            result = true;
        }

        return result;
    }

    private boolean checkHittingPlayer(CollisionObject cobjHitting)
    {
        boolean result = false;

        if ((cobjHitting.contactTop.gid == GraphicID.G_PLAYER)
            || (cobjHitting.contactBottom.gid == GraphicID.G_PLAYER)
            || (cobjHitting.contactLeft.gid == GraphicID.G_PLAYER)
            || (cobjHitting.contactRight.gid == GraphicID.G_PLAYER))
        {
            result = true;
        }

        return result;
    }
}

package com.richikin.jetman.entities.objects;

import com.badlogic.gdx.utils.Disposable;
import com.richikin.enumslib.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.jetman.physics.aabb.CollisionObject;
import com.richikin.jetman.physics.aabb.ICollisionListener;
import com.richikin.utilslib.logging.Trace;

public class GenericCollisionListener implements ICollisionListener, Disposable
{
    private GdxSprite parent;

    public  GenericCollisionListener(GdxSprite _parent)
    {
        this.parent = _parent;
    }

    @Override
    public void onPositiveCollision(CollisionObject cobjHitting)
    {
        if ((cobjHitting.gid == GraphicID.G_LASER) || (cobjHitting.gid == GraphicID.G_ROVER_BULLET))
        {
            parent.setAction(ActionStates._KILLED);
        }
        else
        {
            if (cobjHitting.gid == GraphicID.G_PLAYER)
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

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose()
    {
        Trace.__FILE_FUNC();

        parent = null;
    }
}

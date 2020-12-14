package com.richikin.jetman.physics.aabb;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.enumslib.ActionStates;

public class AABB implements Disposable
{
    private Rectangle   topRectangle;
    private Rectangle   midRectangle;
    private Rectangle   botRectangle;

    public AABB()
    {
        super();

        this.topRectangle = new Rectangle();
        this.midRectangle = new Rectangle();
        this.botRectangle = new Rectangle();
    }

    public boolean checkAABBBoxes(CollisionObject boxA)
    {
        boolean isHitting;
        boolean collisionDetected = false;

        if (AABBData.boxes().size > 0)
        {
            if (boxA.index > 0)
            {
                float boxHeight = boxA.rectangle.height / 3;

                topRectangle.set
                    (
                        (boxA.rectangle.x + (boxA.rectangle.width / 4)),
                        (boxA.rectangle.y + (boxHeight * 2)),
                        (boxA.rectangle.width / 2),
                        boxHeight
                    );

                midRectangle.set
                    (
                        boxA.rectangle.x,
                        (boxA.rectangle.y + boxHeight),
                        boxA.rectangle.width,
                        boxHeight
                    );

                botRectangle.set
                    (
                        (boxA.rectangle.x + (boxA.rectangle.width / 4)),
                        boxA.rectangle.y,
                        (boxA.rectangle.width / 2),
                        boxHeight
                    );

                isHitting = false;

                //
                // boxA is the sprite checking for collision
                // boxB is any sprite that boxA is in contact with
                // All collisionObjects have parentEntities.
                for (CollisionObject boxB : AABBData.boxes())
                {
                    if (boxB.index > 0)
                    {
                        if (((boxA.parentEntity.collidesWith & boxB.parentEntity.bodyCategory) != 0)
                            && ((boxB.parentEntity.collidesWith & boxA.parentEntity.bodyCategory) != 0)
                            && ((boxA.gid != boxB.gid) || (boxA.index != boxB.index))
                            && ((boxA.type != boxB.type) || (boxA.index != boxB.index)))
                        {
                            if (Intersector.overlaps(boxA.rectangle, boxB.rectangle))
                            {
                                if (Intersector.overlaps(topRectangle, boxB.rectangle))
                                {
                                    isHitting           = true;
                                    boxA.idTop          = boxB.gid;
                                    boxA.boxHittingTop  = boxB.index;
                                    boxA.contactMask    |= AABBData._TOP;
                                }

                                if (Intersector.overlaps(midRectangle, boxB.rectangle))
                                {
                                    if ((midRectangle.x >= boxB.rectangle.x)
                                        && (midRectangle.x <= (boxB.rectangle.x + boxB.rectangle.width))
                                        && ((midRectangle.x + midRectangle.width) > (boxB.rectangle.x + boxB.rectangle.width)))
                                    {
                                        isHitting           = true;
                                        boxA.idLeft         = boxB.gid;
                                        boxA.boxHittingLeft = boxB.index;
                                        boxA.contactMask    |= AABBData._LEFT;
                                    }

                                    if ((midRectangle.x < boxB.rectangle.x)
                                        && ((midRectangle.x + midRectangle.width) >= boxB.rectangle.x)
                                        && ((midRectangle.x + midRectangle.width) <= (boxB.rectangle.x + boxB.rectangle.width)))
                                    {
                                        isHitting           = true;
                                        boxA.idRight            = boxB.gid;
                                        boxA.boxHittingRight    = boxB.index;
                                        boxA.contactMask        |= AABBData._RIGHT;
                                    }
                                }

                                if (Intersector.overlaps(botRectangle, boxB.rectangle)
                                    && (boxB.rectangle.y <= botRectangle.y))
                                {
                                    isHitting               = true;
                                    boxA.idBottom           = boxB.gid;
                                    boxA.boxHittingBottom   = boxB.index;
                                    boxA.contactMask        |= AABBData._BOTTOM;
                                }
                            }
                        }

                        if (isHitting)
                        {
                            collisionDetected = true;
                            isHitting = false;

                            boxA.contactEntity      = boxB.parentEntity;
                            boxA.isContactObstacle  = boxB.isObstacle;
                            boxA.action             = ActionStates._COLLIDING;
                            boxB.action             = ActionStates._COLLIDING;
                        }
                    }
                }
            }
        }

        return collisionDetected;
    }

    @Override
    public void dispose()
    {
        topRectangle    = null;
        midRectangle    = null;
        botRectangle    = null;
    }
}

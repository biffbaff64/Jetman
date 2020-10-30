package com.richikin.jetman.physics.aabb;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.enumslib.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.objects.GameEntity;
import com.richikin.enumslib.GraphicID;

public class AABB implements Disposable
{
    private CollisionObject contactBox;
    private Rectangle       topRectangle;
    private Rectangle       midRectangle;
    private Rectangle       botRectangle;
    private App             app;

    public AABB(App _app)
    {
        super();

        this.app          = _app;
        this.contactBox   = app.collisionUtils.newObject();
        this.topRectangle = new Rectangle();
        this.midRectangle = new Rectangle();
        this.botRectangle = new Rectangle();
    }

    public boolean checkHittingBox(GameEntity _thisSprite)
    {
        boolean isHitting         = false;
        boolean collisionDetected = false;

        if (AABBData.boxes().size > 0)
        {
            Rectangle rectangle = _thisSprite.collisionObject.rectangle;

            rectangle.y--;
            rectangle.height++;

            float boxHeight = rectangle.height / 3;
            int   arraySize = AABBData.boxes().size;

            // Obtain rectangles for the middle and bottom
            // sections of the parent sprite...
            topRectangle.set(rectangle.x, (rectangle.y + (boxHeight * 2)), rectangle.width, boxHeight);
            midRectangle.set(rectangle.x, (rectangle.y + boxHeight), rectangle.width, boxHeight);
            botRectangle.set(rectangle.x, rectangle.y, rectangle.width, boxHeight);

            // ...and check for collision, skipping box 0
            for (int index = 1; index < arraySize; index++)
            {
                // Grab rectangle from the global data table
                contactBox = AABBData.boxes().get(index);

                _thisSprite.collisionObject.action = Actions._COLLIDABLE;

                if ((contactBox != null)
                    && (contactBox.parentEntity != null)
                    && ((_thisSprite.collidesWith & contactBox.parentEntity.bodyCategory) != 0)
                    && ((contactBox.parentEntity.collidesWith & _thisSprite.bodyCategory) != 0)
                    && (_thisSprite.collisionObject.index != contactBox.index))
                {
                    if (Intersector.overlaps(rectangle, contactBox.rectangle))
                    {
                        if (Intersector.overlaps(contactBox.rectangle, topRectangle))
                        {
                            isHitting                                 = true;
                            _thisSprite.collisionObject.idTop         = contactBox.gid;
                            _thisSprite.collisionObject.boxHittingTop = contactBox.index;
                        }

                        if (isValidSideObstacle(contactBox.gid) && Intersector.overlaps(contactBox.rectangle, midRectangle))
                        {
                            if ((midRectangle.x >= contactBox.rectangle.x)
                                && (midRectangle.x <= (contactBox.rectangle.x + contactBox.rectangle.width))
                                && ((midRectangle.x + midRectangle.width) > (contactBox.rectangle.x + contactBox.rectangle.width)))
                            {
                                isHitting                                  = true;
                                _thisSprite.collisionObject.idLeft         = contactBox.gid;
                                _thisSprite.collisionObject.boxHittingLeft = contactBox.index;
                            }

                            if ((midRectangle.x < contactBox.rectangle.x)
                                && ((midRectangle.x + midRectangle.width) >= contactBox.rectangle.x)
                                && ((midRectangle.x + midRectangle.width) <= (contactBox.rectangle.x + contactBox.rectangle.width)))
                            {
                                isHitting                                   = true;
                                _thisSprite.collisionObject.idRight         = contactBox.gid;
                                _thisSprite.collisionObject.boxHittingRight = contactBox.index;
                            }
                        }

                        if (Intersector.overlaps(contactBox.rectangle, botRectangle)
                            && (contactBox.rectangle.y <= botRectangle.y))
                        {
                            isHitting                                    = true;
                            _thisSprite.collisionObject.idBottom         = contactBox.gid;
                            _thisSprite.collisionObject.boxHittingBottom = contactBox.index;
                        }
                    }
                }

                if (isHitting)
                {
                    collisionDetected = true;
                    contactBox.action = Actions._COLLIDING;

                    _thisSprite.collisionObject.contactEntity = contactBox.parentEntity;
                    _thisSprite.collisionObject.action        = Actions._COLLIDING;

                    if ((_thisSprite.gid != GraphicID.G_PLAYER) && (contactBox.gid == GraphicID.G_PLAYER))
                    {
                        _thisSprite.collisionObject.isHittingPlayer = true;
                    }

                    isHitting = false;
                }
            }
        }

        return collisionDetected;
    }

    private boolean isValidSideObstacle(GraphicID gid)
    {
        return (gid != GraphicID._GROUND);
    }

    @Override
    public void dispose()
    {
        app          = null;
        contactBox   = null;
        topRectangle = null;
        midRectangle = null;
        botRectangle = null;
    }
}

package com.richikin.jetman.physics.AABB;

import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.jetman.core.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.GdxSprite;
import com.richikin.jetman.graphics.GraphicID;

public class AABB implements Disposable
{
    private CollisionObject contactBox;
    private Rectangle       topRectangle;
    private Rectangle       midRectangle;
    private Rectangle botRectangle;
    private App       app;

    public AABB(App _app)
    {
        super();

        this.app = _app;
        this.contactBox = app.collisionUtils.newObject();
        this.topRectangle = new Rectangle();
        this.midRectangle = new Rectangle();
        this.botRectangle = new Rectangle();
    }

    public boolean checkHittingBox(GdxSprite parent)
    {
        boolean isHitting         = false;
        boolean collisionDetected = false;

        if (AABBData.boxes().size > 0)
        {
            Rectangle rectangle = parent.getCollisionRectangle();

            rectangle.y--;
            rectangle.height++;

            float boxHeight = rectangle.height / 3;

            int arraySize = AABBData.boxes().size;

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

                parent.collisionObject.action = Actions._COLLIDABLE;

                if ((contactBox != null)
                    && (contactBox.parentSprite != null)
                    && ((parent.collidesWith & contactBox.parentSprite.bodyCategory) != 0)
                    && ((contactBox.parentSprite.collidesWith & parent.bodyCategory) != 0)
                    && (parent.collisionObject.index != contactBox.index))
                {
                    if (Intersector.overlaps(rectangle, contactBox.rectangle))
                    {
                        if (Intersector.overlaps(contactBox.rectangle, topRectangle))
                        {
                            isHitting = true;
                            parent.collisionObject.idTop = contactBox.gid;
                            parent.collisionObject.boxHittingTop = contactBox.index;
                        }

                        if (isValidSideObstacle(contactBox.gid) && Intersector.overlaps(contactBox.rectangle, midRectangle))
                        {
                            if ((midRectangle.x >= contactBox.rectangle.x)
                                && (midRectangle.x <= (contactBox.rectangle.x + contactBox.rectangle.width))
                                && ((midRectangle.x + midRectangle.width) > (contactBox.rectangle.x + contactBox.rectangle.width)))
                            {
                                isHitting = true;
                                parent.collisionObject.idLeft = contactBox.gid;
                                parent.collisionObject.boxHittingLeft = contactBox.index;
                            }

                            if ((midRectangle.x < contactBox.rectangle.x)
                                && ((midRectangle.x + midRectangle.width) >= contactBox.rectangle.x)
                                && ((midRectangle.x + midRectangle.width) <= (contactBox.rectangle.x + contactBox.rectangle.width)))
                            {
                                isHitting = true;
                                parent.collisionObject.idRight = contactBox.gid;
                                parent.collisionObject.boxHittingRight = contactBox.index;
                            }
                        }

                        if (Intersector.overlaps(contactBox.rectangle, botRectangle)
                            && (contactBox.rectangle.y <= botRectangle.y))
                        {
                            isHitting = true;
                            parent.collisionObject.idBottom = contactBox.gid;
                            parent.collisionObject.boxHittingBottom = contactBox.index;
                        }
                    }
                }

                if (isHitting)
                {
                    collisionDetected = true;
                    contactBox.action = Actions._COLLIDING;

                    parent.collisionObject.contactSprite = contactBox.parentSprite;
                    parent.collisionObject.action = Actions._COLLIDING;

                    if ((parent.gid != GraphicID.G_PLAYER) && (contactBox.gid == GraphicID.G_PLAYER))
                    {
                        parent.collisionObject.isHittingPlayer = true;
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
        app             = null;
        contactBox      = null;
        topRectangle    = null;
        midRectangle    = null;
        botRectangle    = null;
    }
}

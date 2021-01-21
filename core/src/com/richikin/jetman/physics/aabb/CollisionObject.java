
package com.richikin.jetman.physics.aabb;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.enumslib.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.jetman.entities.objects.BaseEntity;
import com.richikin.utilslib.logging.StopWatch;

import java.util.concurrent.TimeUnit;

// TODO: 12/12/2020 - Can this class be trimmed?
public class CollisionObject implements Disposable
{
    /*
     * Collision box statuc.
     *
     * _COLLIDABLE  -   Collidable but NOT in collision.
     * _INACTIVE    -   Permanently Invisible to any collidable objects.
     * _COLLIDING   -   In Collision.
     * _DEAD        -   To be removed from the list.
     */
    public ActionStates  action;
    public GraphicID     gid;               // ID of THIS object
    public GraphicID     type;              // _OBSTACLE or _ENTITY
    public short         contactMask;       //
    public CollisionRect rectangle;         // The actual collision rectangle
    public BaseEntity    parentEntity;      // The GdxSprite this collision object belongs to, if applicable.
    public BaseEntity    contactEntity;     // ID of contact object
    public int           index;             // This objects position in the collision object arraylist

    public GraphicID idTop;                 // ID of object hitting the top of this object
    public GraphicID idBottom;              // ID of object hitting the bottom of this object
    public GraphicID idLeft;                // ID of object hitting the left of this object
    public GraphicID idRight;               // ID of object hitting the right of this object

    public int boxHittingTop;
    public int boxHittingBottom;
    public int boxHittingLeft;
    public int boxHittingRight;

    public boolean isHittingPlayer;
    public boolean isObstacle;
    public boolean isContactObstacle;
    public boolean isInvisibilityAllowed;

    private StopWatch invisibilityTimer;
    private int       invisibilityDelay;    // How long this collision object is ignored for

    public CollisionObject()
    {
        this.rectangle = new CollisionRect(GraphicID.G_NO_ID);

        create();
    }

    public CollisionObject(Rectangle rectangle)
    {
        this.rectangle = new CollisionRect(rectangle, GraphicID.G_NO_ID);

        create();
    }

    public CollisionObject(int x, int y, int width, int height, GraphicID type)
    {
        rectangle = new CollisionRect(new Rectangle(x, y, width, height), type);

        create();
    }

    public boolean hasContactUp()
    {
        return (contactMask & AABBData._TOP) > 0;
    }

    public boolean hasContactDown()
    {
        return (contactMask & AABBData._BOTTOM) > 0;
    }

    public boolean hasContactLeft()
    {
        return (contactMask & AABBData._LEFT) > 0;
    }

    public boolean hasContactRight()
    {
        return (contactMask & AABBData._RIGHT) > 0;
    }

    public BaseEntity getParent()
    {
        return parentEntity;
    }

    public BaseEntity getContact()
    {
        return contactEntity;
    }

    private void create()
    {
        clearCollision();

        index                 = AABBData.boxes().size;
        isObstacle            = true;
        isContactObstacle     = false;
        gid                   = GraphicID.G_NO_ID;
        invisibilityTimer     = StopWatch.start();
        isInvisibilityAllowed = true;
    }

    public void kill()
    {
        action = ActionStates._DEAD;
    }

    public void clearCollision()
    {
        if (action != ActionStates._DEAD)
        {
            action          = ActionStates._COLLIDABLE;
            isHittingPlayer = false;

            contactEntity = null;
            contactMask   = 0;

            boxHittingTop    = 0;
            boxHittingBottom = 0;
            boxHittingLeft   = 0;
            boxHittingRight  = 0;

            idTop    = GraphicID.G_NO_ID;
            idBottom = GraphicID.G_NO_ID;
            idLeft   = GraphicID.G_NO_ID;
            idRight  = GraphicID.G_NO_ID;
        }
    }

    @Override
    public void dispose()
    {
        action            = null;
        gid               = null;
        type              = null;
        rectangle         = null;
        contactEntity     = null;
        parentEntity      = null;
        idTop             = null;
        idBottom          = null;
        idLeft            = null;
        idRight           = null;
        invisibilityTimer = null;
    }

    public void setInvisibility(int timeInMilliseconds)
    {
        action            = ActionStates._INVISIBLE;
        invisibilityDelay = timeInMilliseconds;
        invisibilityTimer.reset();
    }

    public void checkInvisibility()
    {
        if ((action != ActionStates._COLLIDABLE) && isInvisibilityAllowed)
        {
            if (invisibilityTimer.time(TimeUnit.MILLISECONDS) >= invisibilityDelay)
            {
                action = ActionStates._COLLIDABLE;
            }
        }
    }
}

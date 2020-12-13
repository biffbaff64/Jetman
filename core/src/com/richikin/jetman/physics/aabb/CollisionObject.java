
package com.richikin.jetman.physics.aabb;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.enumslib.ActionStates;
import com.richikin.jetman.entities.objects.GameEntity;
import com.richikin.enumslib.GraphicID;
import com.richikin.utilslib.logging.StopWatch;

import java.util.Arrays;
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
    public CollisionRect rectangle;         // The actual collision rectangle
    public GameEntity    parentEntity;      // The GdxSprite this collision object belongs to, if applicable.
    public int           index;             // This objects position in the collision object arraylist
    public boolean[]     hasContact;
    public GameEntity[]  contacts;          // The entities being collided with
    public short         contactFilterMask; // Combined mask of the BodyCategories of all contacts
    public boolean       isHittingPlayer;
    public boolean       isObstacle;
    public boolean       isContactObstacle; // ####

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
        return contacts[AABBData._CONTACT_TOP] != null;
    }

    public boolean hasContactDown()
    {
        return contacts[AABBData._CONTACT_BOTTOM] != null;
    }

    public boolean hasContactLeft()
    {
        return contacts[AABBData._CONTACT_LEFT] != null;
    }

    public boolean hasContactRight()
    {
        return contacts[AABBData._CONTACT_RIGHT] != null;
    }

    public GameEntity getParent()
    {
        return parentEntity;
    }

    private void create()
    {
        contacts = new GameEntity[AABBData._MAX_CONTACT_SIDES];
        hasContact = new boolean[AABBData._MAX_CONTACT_SIDES];

        clearCollision();

        index             = AABBData.boxes().size;
        isObstacle        = true;
        isContactObstacle = false;
        gid               = GraphicID.G_NO_ID;
        invisibilityTimer = StopWatch.start();
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

            Arrays.fill(hasContact, false);
        }
    }

    @Override
    public void dispose()
    {
        parentEntity = null;
        rectangle    = null;
        contacts     = null;
    }

    public void setInvisibility(int timeInMilliseconds)
    {
        action            = ActionStates._INVISIBLE;
        invisibilityDelay = timeInMilliseconds;
        invisibilityTimer.reset();
    }

    public void checkInvisibility()
    {
        if (action != ActionStates._COLLIDABLE)
        {
            if (invisibilityTimer.time(TimeUnit.MILLISECONDS) >= invisibilityDelay)
            {
                action = ActionStates._COLLIDABLE;
            }
        }
    }
}

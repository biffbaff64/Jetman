/*
 *  Copyright 01/05/2018 Red7Projects.
 *  <p>
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  <p>
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  <p>
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.richikin.jetman.physics.AABB;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.jetman.entities.GdxSprite;
import com.richikin.jetman.core.Actions;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.utils.logging.Trace;

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
    public Actions       action;
    public GraphicID     gid;               // ID of THIS object
    public GraphicID     contactGid;        // ID of contact object
    public GraphicID     type;              // _OBSTACLE or _ENTITY
    public GdxSprite     parentSprite;      // The GdxSprite this collision object belongs to, if applicable.
    public GdxSprite     contactSprite;     // ID of contact object
    public CollisionRect rectangle;         // The actual collision rectangle

    public GraphicID idTop;                 // ID of object hitting the top of this object
    public GraphicID idBottom;              // ID of object hitting the bottom of this object
    public GraphicID idLeft;                // ID of object hitting the left of this object
    public GraphicID idRight;               // ID of object hitting the right of this object

    public int boxHittingTop;
    public int boxHittingBottom;
    public int boxHittingLeft;
    public int boxHittingRight;
    public int index;                       // This objects position in the collision object arraylist

    public BodyDef bodyDef;
    public Body    b2dBody;
    public short   contactMask;
    public short   bodyCategory;
    public short   collidesWith;

    public boolean isHittingPlayer;
    public boolean isObstacle;
    public boolean isContactObstacle;

    public CollisionObject()
    {
        this.rectangle = new CollisionRect(GraphicID.G_NO_ID);

        create();
    }

    public CollisionObject(Rectangle _rectangle)
    {
        this.rectangle = new CollisionRect(_rectangle, GraphicID.G_NO_ID);

        create();
    }

    public CollisionObject(int x, int y, int width, int height, GraphicID _type)
    {
        rectangle = new CollisionRect(new Rectangle(x, y, width, height), _type);

        create();

        this.type = _type;
    }

    private void create()
    {
        clearCollision();

        index             = AABBData.boxes().size;
        isHittingPlayer   = false;
        isObstacle        = true;
        isContactObstacle = false;
        gid               = GraphicID.G_NO_ID;
        contactGid        = GraphicID.G_NO_ID;
        type              = GraphicID.G_NO_ID;
        action            = Actions._COLLIDABLE;
        contactMask       = 0;
        bodyCategory      = 0;
        collidesWith      = 0;
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

    public void addObjectToList()
    {
        AABBData.boxes().add(this);
    }

    public void removeObjectFromList()
    {
        AABBData.boxes().removeIndex(index);
        AABBData.rescan();
    }

    public void kill()
    {
        action = Actions._DEAD;
    }

    public void clearCollision()
    {
        action           = Actions._COLLIDABLE;
        isHittingPlayer  = false;
        contactMask      = 0;
        contactSprite    = null;
        boxHittingTop    = 0;
        boxHittingBottom = 0;
        boxHittingLeft   = 0;
        boxHittingRight  = 0;
        idTop            = GraphicID.G_NO_ID;
        idBottom         = GraphicID.G_NO_ID;
        idLeft           = GraphicID.G_NO_ID;
        idRight          = GraphicID.G_NO_ID;
    }

    @Override
    public void dispose()
    {
        contactSprite = null;
        parentSprite  = null;
        rectangle     = null;
        idTop         = null;
        idBottom      = null;
        idLeft        = null;
        idRight       = null;
    }

    public void debug()
    {
        Trace.__FILE_FUNC_WithDivider();
        Trace.dbg("GID   : " + gid);
        if (parentSprite != null)
        {
            Trace.dbg("PARENT: " + parentSprite.gid);
        }
        Trace.dbg("ACTION: " + action);
        Trace.dbg("RECT  : " + rectangle.toString());
    }
}

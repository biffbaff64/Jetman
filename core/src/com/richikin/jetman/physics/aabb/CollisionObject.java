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

package com.richikin.jetman.physics.aabb;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.enumslib.ActionStates;
import com.richikin.jetman.entities.objects.GameEntity;
import com.richikin.enumslib.GraphicID;
import com.richikin.utilslib.logging.StopWatch;

import java.util.concurrent.TimeUnit;

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
    public GraphicID     contactGid;        // ID of contact object
    public CollisionRect rectangle;         // The actual collision rectangle
    public GameEntity    parentEntity;      // The GdxSprite this collision object belongs to, if applicable.
    public GameEntity    contactEntity;     // ID of contact object
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

    private StopWatch invisibilityTimer;
    private int       invisibilityDelay;    // How long this collision object is ingored for

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
    }

    public GameEntity getParent()
    {
        return parentEntity;
    }

    public GameEntity getContact()
    {
        return contactEntity;
    }

    private void create()
    {
        clearCollision();

        index                 = AABBData.boxes().size;
        isHittingPlayer       = false;
        isObstacle            = true;
        isContactObstacle     = false;
        gid                   = GraphicID.G_NO_ID;
        contactGid            = GraphicID.G_NO_ID;
        action                = ActionStates._COLLIDABLE;
        invisibilityTimer     = StopWatch.start();
    }

    public void kill()
    {
        action = ActionStates._DEAD;
    }

    public void clearCollision()
    {
        if (action != ActionStates._DEAD)
        {
            action           = ActionStates._COLLIDABLE;
            isHittingPlayer  = false;
            contactEntity    = null;
            boxHittingTop    = 0;
            boxHittingBottom = 0;
            boxHittingLeft   = 0;
            boxHittingRight  = 0;
            idTop            = GraphicID.G_NO_ID;
            idBottom         = GraphicID.G_NO_ID;
            idLeft           = GraphicID.G_NO_ID;
            idRight          = GraphicID.G_NO_ID;
        }
    }

    @Override
    public void dispose()
    {
        contactEntity = null;
        parentEntity  = null;
        rectangle     = null;
        idTop         = null;
        idBottom      = null;
        idLeft        = null;
        idRight       = null;
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

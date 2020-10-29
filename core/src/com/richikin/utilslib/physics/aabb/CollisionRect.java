/*
 * *****************************************************************************
 *    Copyright 27/03/2017 See AUTHORS file.
 *    <p>
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *    <p>
 *    http://www.apache.org/licenses/LICENSE-2.0
 *    <p>
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *   ***************************************************************************
 *
 */

package com.richikin.utilslib.physics.aabb;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.physics.aabb.AABBData;
import com.richikin.jetman.physics.aabb.CollisionObject;
import org.jetbrains.annotations.NotNull;

public class CollisionRect extends Rectangle
{
    private GraphicID gid;
    public  Color     colour;

    public CollisionRect(GraphicID _gid)
    {
        super(new Rectangle(0, 0, 1, 1));

        this.gid = _gid;
        this.colour = Color.WHITE;
    }

	public CollisionRect(Rectangle rectangle, GraphicID _gid)
	{
		super(rectangle);

        this.gid = _gid;
        this.colour = Color.WHITE;
	}

	public CollisionRect(float _x, float _y, float _width, float _height, GraphicID _gid)
	{
		super(new Rectangle(_x, _y, _width, _height));

        this.gid = _gid;
        this.colour = Color.WHITE;
	}

    public CollisionRect set(CollisionRect rectangle, GraphicID _gid)
    {
        this.x      = rectangle.x;
        this.y      = rectangle.y;
        this.width  = rectangle.width;
        this.height = rectangle.height;
        this.gid    = _gid;
        this.colour = Color.WHITE;

        return rectangle;
    }

    public boolean isTouchingAnother(int parentIndex)
    {
        boolean isTouching = false;

        for (CollisionObject object : AABBData.boxes())
        {
            if (object.index != parentIndex)
            {
                if (Intersector.overlaps(this, object.rectangle))
                {
                    isTouching = true;
                }
            }
        }

        return isTouching;
    }

    public boolean isTouchingAnEntity(int parentIndex)
    {
        boolean isTouching = false;

        for (CollisionObject object : AABBData.boxes())
        {
            if ((object.index != parentIndex) && !object.isObstacle)
            {
                if (Intersector.overlaps(this, object.rectangle))
                {
                    isTouching = true;
                }
            }
        }

        return isTouching;
    }

    @Override
    @NotNull
    public String toString()
    {
        return "x: " + x + " y: " + y + " width: " + width + " height: " + height;
    }
}

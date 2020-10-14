
package com.richikin.jetman.entities.paths;

import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.maths.SimpleVec2;
import com.richikin.jetman.maths.SimpleVec2F;
import com.richikin.jetman.physics.Direction;
import com.richikin.jetman.physics.Movement;
import com.richikin.jetman.physics.Speed;

@SuppressWarnings("unused")
public class StairsPath
{
    private static class PathData
    {
        public final SimpleVec2F distance;
        public final Direction   direction;
        public final Speed       speed;

        PathData(SimpleVec2F _distance, Direction _direction, Speed _speed)
        {
            distance = new SimpleVec2F(_distance.x, _distance.y);
            direction = new Direction(_direction);
            speed = new Speed(_speed);
        }
    }

    private final PathData[] pathData =
        {
            new PathData(
                /* Distance  */ new SimpleVec2F(96, 0),
                /* Direction */ new Direction(Movement._DIRECTION_CUSTOM, Movement._DIRECTION_STILL),
                /* Speed     */ new Speed(2, 0)
            ),
            new PathData(
                /* Distance  */ new SimpleVec2F(0, 96),
                /* Direction */ new Direction(Movement._DIRECTION_STILL, Movement._DIRECTION_UP),
                /* Speed     */ new Speed(0, 2)
            ),
            new PathData(
                /* Distance  */ new SimpleVec2F(96, 0),
                /* Direction */ new Direction(Movement._DIRECTION_CUSTOM, Movement._DIRECTION_STILL),
                /* Speed     */ new Speed(2, 0)
            ),
            new PathData(
                /* Distance  */ new SimpleVec2F(0, 96),
                /* Direction */ new Direction(Movement._DIRECTION_STILL, Movement._DIRECTION_UP),
                /* Speed     */ new Speed(0, 2)
            ),
            new PathData(
                /* Distance  */ new SimpleVec2F(96, 0),
                /* Direction */ new Direction(Movement._DIRECTION_CUSTOM, Movement._DIRECTION_STILL),
                /* Speed     */ new Speed(2, 0)
            ),
            new PathData(
                /* Distance  */ new SimpleVec2F(0, 96),
                /* Direction */ new Direction(Movement._DIRECTION_STILL, Movement._DIRECTION_UP),
                /* Speed     */ new Speed(0, 2)
            ),
            new PathData(
                /* Distance  */ new SimpleVec2F(96, 0),
                /* Direction */ new Direction(Movement._DIRECTION_CUSTOM, Movement._DIRECTION_STILL),
                /* Speed     */ new Speed(2, 0)
            ),
            new PathData(
                /* Distance  */ new SimpleVec2F(0, 96),
                /* Direction */ new Direction(Movement._DIRECTION_STILL, Movement._DIRECTION_UP),
                /* Speed     */ new Speed(0, 2)
            ),
            new PathData(
                /* Distance  */ new SimpleVec2F(96, 0),
                /* Direction */ new Direction(Movement._DIRECTION_CUSTOM, Movement._DIRECTION_STILL),
                /* Speed     */ new Speed(2, 0)
            ),
            // ---------------------------------------------------------------
            new PathData(
                /* Distance  */ new SimpleVec2F(0, 96),
                /* Direction */ new Direction(Movement._DIRECTION_STILL, Movement._DIRECTION_DOWN),
                /* Speed     */ new Speed(0, 2)
            ),
            new PathData(
                /* Distance  */ new SimpleVec2F(96, 0),
                /* Direction */ new Direction(Movement._DIRECTION_CUSTOM, Movement._DIRECTION_STILL),
                /* Speed     */ new Speed(2, 0)
            ),
            new PathData(
                /* Distance  */ new SimpleVec2F(0, 96),
                /* Direction */ new Direction(Movement._DIRECTION_STILL, Movement._DIRECTION_DOWN),
                /* Speed     */ new Speed(0, 2)
            ),
            new PathData(
                /* Distance  */ new SimpleVec2F(96, 0),
                /* Direction */ new Direction(Movement._DIRECTION_CUSTOM, Movement._DIRECTION_STILL),
                /* Speed     */ new Speed(2, 0)
            ),
            new PathData(
                /* Distance  */ new SimpleVec2F(0, 96),
                /* Direction */ new Direction(Movement._DIRECTION_STILL, Movement._DIRECTION_DOWN),
                /* Speed     */ new Speed(0, 2)
            ),
            new PathData(
                /* Distance  */ new SimpleVec2F(96, 0),
                /* Direction */ new Direction(Movement._DIRECTION_CUSTOM, Movement._DIRECTION_STILL),
                /* Speed     */ new Speed(2, 0)
            ),
            new PathData(
                /* Distance  */ new SimpleVec2F(0, 96),
                /* Direction */ new Direction(Movement._DIRECTION_STILL, Movement._DIRECTION_DOWN),
                /* Speed     */ new Speed(0, 2)
            ),
            // ...And Repeat...
        };

    public       int        pathIndex;
    public final SimpleVec2 directionReset;

    public StairsPath()
    {
        pathIndex = 0;
        directionReset = new SimpleVec2(Movement._DIRECTION_RIGHT, Movement._DIRECTION_STILL);
    }

    public void setNextPathData(GdxSprite spriteObject)
    {
        if (pathIndex >= pathData.length)
        {
            pathIndex = 0;
        }

        spriteObject.distance.set(pathData[pathIndex].distance);
        spriteObject.direction.set(pathData[pathIndex].direction);
        spriteObject.speed.set(pathData[pathIndex].speed);

        if (spriteObject.direction.getX() == Movement._DIRECTION_CUSTOM)
        {
            spriteObject.direction.setX(directionReset.getX());
        }

        pathIndex++;
    }
}

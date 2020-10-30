package com.richikin.utilslib.physics.aabb;

import com.badlogic.gdx.math.Intersector;
import com.richikin.jetman.entities.objects.GameEntity;

public abstract class AABBUtils
{
    public static boolean contains(GameEntity _entityA, GameEntity _entityB)
    {
        return _entityA.getCollisionRectangle().contains(_entityB.getCollisionRectangle());
    }

    public static boolean overlaps(GameEntity _entityA, GameEntity _entityB)
    {
        return Intersector.overlaps(_entityA.getCollisionRectangle(), _entityB.getCollisionRectangle());
    }
}

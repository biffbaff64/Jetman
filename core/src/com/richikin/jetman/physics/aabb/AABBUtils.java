package com.richikin.jetman.physics.aabb;

import com.badlogic.gdx.math.Intersector;
import com.richikin.jetman.entities.objects.GameEntity;

import org.jetbrains.annotations.NotNull;

public enum AABBUtils
{
    ;

    public static boolean contains(@NotNull GameEntity entityA, @NotNull GameEntity entityB)
    {
        return entityA.getCollisionRectangle().contains(entityB.getCollisionRectangle());
    }

    public static boolean overlaps(@NotNull GameEntity entityA, @NotNull GameEntity entityB)
    {
        return Intersector.overlaps(entityA.getCollisionRectangle(), entityB.getCollisionRectangle());
    }
}

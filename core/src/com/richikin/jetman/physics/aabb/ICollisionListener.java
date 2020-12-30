package com.richikin.jetman.physics.aabb;

public interface ICollisionListener
{
    void onPositiveCollision(CollisionObject cobjHitting);

    void onNegativeCollision();

    void dispose();
}

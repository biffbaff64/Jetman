package com.richikin.jetman.physics.aabb;

import com.richikin.enumslib.GraphicID;

public interface ICollisionListener
{
    void onPositiveCollision(CollisionObject cobjHitting);

    void onNegativeCollision();

    void dispose();
}

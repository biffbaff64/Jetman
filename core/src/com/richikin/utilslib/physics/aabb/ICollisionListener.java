package com.richikin.utilslib.physics.aabb;

import com.richikin.jetman.graphics.GraphicID;

public interface ICollisionListener
{
    void onPositiveCollision(GraphicID graphicID);

    void onNegativeCollision();

    void dispose();
}

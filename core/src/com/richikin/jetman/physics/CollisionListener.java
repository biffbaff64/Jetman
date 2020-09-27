package com.richikin.jetman.physics;

import com.richikin.jetman.graphics.GraphicID;

public interface CollisionListener
{
    void onPositiveCollision(GraphicID graphicID);

    void onNegativeCollision();

    void dispose();
}

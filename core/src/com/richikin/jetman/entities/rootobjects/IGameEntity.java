package com.richikin.jetman.entities.rootobjects;

import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.physics.AABB.AABBData;

public interface IGameEntity
{
    void setCollisionObject(int _xPos, int _yPos);

    void setCollisionObject(float _xPos, float _yPos);
}

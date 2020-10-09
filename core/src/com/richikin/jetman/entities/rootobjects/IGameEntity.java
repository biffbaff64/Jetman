package com.richikin.jetman.entities.rootobjects;

import com.richikin.jetman.core.Actions;

public interface IGameEntity
{
    void setAction(Actions _action);

    void setCollisionObject(int _xPos, int _yPos);

    void setCollisionObject(float _xPos, float _yPos);
}

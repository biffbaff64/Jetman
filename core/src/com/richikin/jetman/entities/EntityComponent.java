package com.richikin.jetman.entities;

import com.badlogic.gdx.math.Rectangle;
import com.richikin.enumslib.ActionStates;

public interface EntityComponent
{
    void setAction(ActionStates _action);

    ActionStates getAction();

    void tidy(int _index);

    Rectangle getCollisionRectangle();

    void setCollisionObject(int _xPos, int _yPos);

    void setCollisionObject(float _xPos, float _yPos);

    float getTopEdge();

    float getRightEdge();
}

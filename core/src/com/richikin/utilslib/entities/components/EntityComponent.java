package com.richikin.utilslib.entities.components;

import com.badlogic.gdx.math.Rectangle;
import com.richikin.enumslib.Actions;

public interface EntityComponent
{
    void setAction(Actions _action);

    Actions getAction();

    void tidy(int _index);

    Rectangle getCollisionRectangle();

    void setCollisionObject(int _xPos, int _yPos);

    void setCollisionObject(float _xPos, float _yPos);

    float getTopEdge();

    float getRightEdge();
}

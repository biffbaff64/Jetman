package com.richikin.jetman.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.richikin.jetman.core.App;
import com.richikin.jetman.core.StateID;
import com.richikin.jetman.maths.SimpleVec2F;
import com.richikin.jetman.physics.Direction;
import com.richikin.jetman.physics.Speed;

public interface IUserInterfacePanel
{
    void initialise(TextureRegion _region, String _nameID, Object... args);

    void set(SimpleVec2F xy, SimpleVec2F distance, Direction direction, Speed speed);

    boolean update();

    void draw(App _app);

    boolean getActiveState();

    void activate();

    void deactivate();

    int getWidth();

    int getHeight();

    void setWidth(int _width);

    void setHeight(int _height);

    SimpleVec2F getPosition();

    void setPosition(float x, float y);

    void setPauseTime(int _time);

    void forceZoomOut();

    StateID getState();

    void setState(StateID _state);

    boolean nameExists(String _nameID);

    String getNameID();

    void dispose();
}
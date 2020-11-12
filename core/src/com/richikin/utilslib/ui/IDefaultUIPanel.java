
package com.richikin.utilslib.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.richikin.enumslib.StateID;
import com.richikin.utilslib.maths.SimpleVec2;
import com.richikin.utilslib.maths.SimpleVec2F;
import com.richikin.utilslib.physics.Direction;
import com.richikin.utilslib.physics.Speed;

public interface IDefaultUIPanel
{
    void open();

    void close();

    void initialise(TextureRegion _region, String _nameID, Object... args);

    void set(SimpleVec2F xy, SimpleVec2F distance, Direction direction, Speed speed);

    void setup();

    void draw(SpriteBatch spriteBatch);

    void activate();

    void deactivate();

    void populateTable();

    void setPosition(float x, float y);

    void setWidth(int _width);

    void setState(StateID _state);

    void forceZoomOut();

    void setPauseTime(int _time);

    void setHeight(int _height);

    boolean update();

    boolean getActiveState();

    boolean nameExists(String _nameID);

    SimpleVec2 getSize();

    SimpleVec2F getPosition();

    int getWidth();

    int getHeight();

    String getNameID();

    StateID getState();
}

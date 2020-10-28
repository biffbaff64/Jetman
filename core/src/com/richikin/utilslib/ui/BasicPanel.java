package com.richikin.utilslib.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.utilslib.states.StateID;
import com.richikin.utilslib.maths.SimpleVec2F;
import com.richikin.utilslib.physics.Direction;
import com.richikin.utilslib.physics.Speed;

/**
 * Basic UI Panel class.
 * All other panels should extend this class.
 */
public abstract class BasicPanel implements IUserInterfacePanel, Disposable
{
    protected TextureRegion textureRegion;
    protected String        nameID;

    private SimpleVec2F   position;
    private StateID       stateID;
    private int           panelWidth;
    private int           panelHeight;
    private boolean       isActive;

    public BasicPanel()
    {
        this.position      = new SimpleVec2F();
        this.stateID       = StateID._INACTIVE;
        this.textureRegion = null;
        this.panelWidth    = 10;
        this.panelHeight   = 10;
        this.isActive      = false;
        this.nameID        = "unnamed";
    }

    /**
     * Draw this panel.
     */
    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        spriteBatch.draw(textureRegion, position.x, position.y, panelWidth, panelHeight);
    }

    /**
     * Returns TRUE if this panel is the same as
     * the specified panel.
     */
    @Override
    public boolean nameExists(String _nameID)
    {
        return _nameID.equals(this.nameID);
    }

    @Override
    public void set(SimpleVec2F xy, SimpleVec2F distance, Direction direction, Speed speed)
    {
    }

    @Override
    public void setWidth(int _width)
    {
        panelWidth = _width;
    }

    @Override
    public int getWidth()
    {
        return panelWidth;
    }

    @Override
    public void setHeight(int _height)
    {
        panelHeight = _height;
    }

    @Override
    public int getHeight()
    {
        return panelHeight;
    }

    @Override
    public SimpleVec2F getPosition()
    {
        return position;
    }

    @Override
    public void setPosition(final float x, final float y)
    {
        position.set(x, y);
    }

    @Override
    public String getNameID()
    {
        return nameID;
    }

    @Override
    public boolean getActiveState()
    {
        return isActive;
    }

    @Override
    public void activate()
    {
        isActive = true;
    }

    @Override
    public void deactivate()
    {
        isActive = false;
    }

    @Override
    public StateID getState()
    {
        return stateID;
    }

    @Override
    public void setState(StateID _state)
    {
        stateID = _state;
    }

    @Override
    public void setPauseTime(final int _time)
    {
    }

    @Override
    public void forceZoomOut()
    {
    }

    @Override
    public void dispose()
    {
        position      = null;
        textureRegion = null;
        stateID       = null;
    }
}

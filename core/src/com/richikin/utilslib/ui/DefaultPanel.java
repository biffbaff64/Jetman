package com.richikin.utilslib.ui;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.enumslib.StateID;
import com.richikin.utilslib.LibApp;
import com.richikin.utilslib.logging.StateManager;
import com.richikin.utilslib.maths.SimpleVec2;
import com.richikin.utilslib.maths.SimpleVec2F;
import com.richikin.utilslib.physics.Direction;

/**
 * Basic UI Panel class.
 * All other panels should extend this class.
 */
public abstract class DefaultPanel implements IDefaultUIPanel, Disposable
{
    public Table      buffer;
    public Skin       skin;
    public ScrollPane scrollPane;

    protected TextureRegion textureRegion;
    protected String        nameID;
    protected SimpleVec2    size;
    protected SimpleVec2F   position;

    private int     panelWidth;
    private int     panelHeight;
    private boolean isActive;

    private final StateManager state;

    public DefaultPanel()
    {
        this.position      = new SimpleVec2F();
        this.state         = new StateManager();
        this.textureRegion = null;
        this.panelWidth    = 10;
        this.panelHeight   = 10;
        this.isActive      = false;
        this.nameID        = "unnamed";
    }

    @Override
    public void open()
    {
        setup();
    }

    @Override
    public void close()
    {
        dispose();
    }

    @Override
    public void draw()
    {
        if (isActive && (textureRegion != null))
        {
            LibApp.spriteBatch.draw(textureRegion, position.getX(), position.getY(), panelWidth, panelHeight);
        }
    }

    @Override
    public boolean nameExists(String _nameID)
    {
        return _nameID.equals(this.nameID);
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
    public SimpleVec2 getSize()
    {
        return size;
    }

    @Override
    public SimpleVec2F getPosition()
    {
        return position;
    }

    @Override
    public void setPosition(float x, float y)
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
        return state.peek();
    }

    @Override
    public void setState(StateID _state)
    {
        state.set(_state);
    }


    // -------------------------------------------------------------------
    // Empty methods from IDefaultPanel interface
    //@formatter:off
    @Override
    public void initialise(TextureRegion _region, String _nameID, Object... args) {}

    @Override
    public void set(SimpleVec2F xy, SimpleVec2F distance, Direction direction, SimpleVec2F speed) {}

    @Override
    public void setup() {}

    @Override
    public void populateTable() {}

    @Override
    public void setPauseTime(final int _time) {}

    @Override
    public void forceZoomOut() {}
    //@formatter:on

    // -------------------------------------------------------------------
    // From Disposable Interface
    @Override
    public void dispose()
    {
        position      = null;
        textureRegion = null;
    }
}

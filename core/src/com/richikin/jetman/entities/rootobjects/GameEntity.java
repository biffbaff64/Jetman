package com.richikin.jetman.entities.rootobjects;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.jetman.core.App;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.maths.SimpleVec2F;

public class GameEntity implements IGameEntity, Disposable
{
    public GraphicID   gid;
    public GraphicID   type;
    public Body        b2dBody;
    public BodyDef     bodyDef;
    public SimpleVec2F position;

    public int   zPosition;
    public short bodyCategory;
    public short collidesWith;
    public float frameWidth;
    public float frameHeight;

    protected App app;

    public GameEntity(App _app)
    {
        this.app = _app;
    }

    public GameEntity(GraphicID _gid, App _app)
    {
        this.app = _app;
        this.gid = _gid;
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose()
    {
        b2dBody = null;
        bodyDef = null;
        position = null;
    }
}

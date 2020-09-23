package com.richikin.jetman.entities.rootobjects;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.jetman.core.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.maths.SimpleVec2F;
import com.richikin.jetman.physics.AABB.CollisionRect;

public class GameEntity implements IGameEntity, Disposable
{
    public GraphicID   gid;
    public GraphicID   type;
    public SimpleVec2F position;
    public int         zPosition;
    public float       frameWidth;
    public float       frameHeight;

    public CollisionRect rectangle;
    public Body          b2dBody;
    public BodyDef       bodyDef;
    public short         bodyCategory;
    public short         collidesWith;
    public Actions       entityAction;

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
        b2dBody  = null;
        bodyDef  = null;
        position = null;
    }
}

package com.richikin.jetman.entities.rootobjects;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.jetman.core.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.maths.SimpleVec2F;
import com.richikin.jetman.physics.AABB.AABBData;
import com.richikin.jetman.physics.AABB.CollisionObject;

public class GameEntity implements IGameEntity, Disposable
{
    public GraphicID   gid;
    public GraphicID   type;
    public SimpleVec2F position;
    public int         zPosition;
    public float       frameWidth;
    public float       frameHeight;

    public CollisionObject collisionObject;
    public Body            b2dBody;
    public BodyDef         bodyDef;
    public short           bodyCategory;
    public short           collidesWith;
    public Actions         entityAction;

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

    @Override
    public void setCollisionObject(int _xPos, int _yPos)
    {
        setCollisionObject((float) _xPos, (float) _yPos);
    }

    @Override
    public void setCollisionObject(float _xPos, float _yPos)
    {
        collisionObject = app.collisionUtils.newObject
            (
                (int) _xPos,
                (int) _yPos,
                (int) frameWidth,
                (int) frameHeight,
                GraphicID._ENTITY
            );

        collisionObject.gid        = this.gid;
        collisionObject.isObstacle = false;

        if (this.gid != GraphicID.G_NO_ID)
        {
            AABBData.add(collisionObject);
        }
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose()
    {
        position = null;
    }
}

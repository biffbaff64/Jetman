package com.richikin.jetman.entities.objects;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.jetman.core.App;
import com.richikin.enumslib.Actions;
import com.richikin.utilslib.entities.components.EntityComponent;
import com.richikin.enumslib.GraphicID;
import com.richikin.utilslib.maths.SimpleVec2F;
import com.richikin.jetman.physics.aabb.AABBData;
import com.richikin.jetman.physics.aabb.CollisionObject;

public class GameEntity implements EntityComponent, Disposable
{
    public GraphicID       gid;
    public GraphicID       type;
    public SimpleVec2F     position;
    public int             zPosition;
    public float           frameWidth;
    public float           frameHeight;
    public CollisionObject collisionObject;
    public Body            b2dBody;
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

        collisionObject.gid          = this.gid;
        collisionObject.isObstacle   = false;
        collisionObject.parentEntity = this;

        if (this.gid != GraphicID.G_NO_ID)
        {
            AABBData.add(collisionObject);
        }
    }

    @Override
    public Rectangle getCollisionRectangle()
    {
        return collisionObject.rectangle;
    }

    @Override
    public void setAction(Actions _action)
    {
        this.entityAction = _action;
    }

    @Override
    public Actions getAction()
    {
        return this.entityAction;
    }

    @Override
    public void tidy(int _index)
    {
    }

    @Override
    public float getTopEdge()
    {
        return position.y + frameHeight;
    }

    @Override
    public float getRightEdge()
    {
        return position.x + frameWidth;
    }

    //
    // The most common sprite actions.
    public void setStanding()
    {
        this.entityAction = Actions._STANDING;
    }

    public void setRunning()
    {
        this.entityAction = Actions._RUNNING;
    }

    public void setFalling()
    {
        this.entityAction = Actions._FALLING;
    }

    public void setFlying()
    {
        this.entityAction = Actions._FLYING;
    }

    public void setHurt()
    {
        this.entityAction = Actions._HURT;
    }

    public void setKilled()
    {
        this.entityAction = Actions._KILLED;
    }

    public void setDying()
    {
        this.entityAction = Actions._DYING;
    }

    public void setExploding()
    {
        this.entityAction = Actions._EXPLODING;
    }

    public void setDead()
    {
        this.entityAction = Actions._DEAD;
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

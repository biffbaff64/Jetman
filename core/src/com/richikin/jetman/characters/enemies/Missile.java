package com.richikin.jetman.characters.enemies;

import com.badlogic.gdx.Gdx;
import com.richikin.enumslib.ActionStates;
import com.richikin.jetman.core.App;
import com.richikin.jetman.core.GameProgress;
import com.richikin.jetman.core.PointsManager;
import com.richikin.jetman.characters.managers.ExplosionManager;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.enumslib.GraphicID;
import com.richikin.jetman.graphics.camera.Shake;
import com.richikin.jetman.physics.aabb.CollisionObject;
import com.richikin.jetman.physics.aabb.ICollisionListener;
import com.richikin.jetman.developer.Developer;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.physics.Movement;

public class Missile extends GdxSprite implements ICollisionListener
{
    public Missile()
    {
        super();
    }

    @Override
    public void initialise(SpriteDescriptor entityDescriptor)
    {
        create(entityDescriptor);

        direction.setY(((entityDescriptor._INDEX & 1) == 1) ? Movement._DIRECTION_UP : Movement._DIRECTION_DOWN);
        direction.setX((sprite.getX() < App.getRover().sprite.getX()) ? Movement._DIRECTION_RIGHT : Movement._DIRECTION_LEFT);

        if (direction.getY() == Movement._DIRECTION_UP)
        {
            distance.set(Gfx._VIEW_WIDTH * 2, Gfx._VIEW_HEIGHT * 2);
            speed.set(12.0f, 8.0f);

            sprite.setPosition(sprite.getX(), (App.getRover().sprite.getY() + App.getRover().frameHeight) - 4);
            sprite.setRotation((90.0f - ((speed.getY() / speed.getX()) * 100)) * direction.getX());
        }
        else
        {
            sprite.setPosition(sprite.getX(), (App.getRover().sprite.getY() + App.getRover().frameHeight) - 4);

            distance.setX(Math.abs(App.getRover().sprite.getX() - sprite.getX()));
            distance.setY(Math.abs(sprite.getY() - (App.getRover().sprite.getY() + (App.getRover().frameHeight / 3f))));

            speed.setX(12.0f);
            speed.setY(distance.getY() / (distance.getX() / speed.getX()));
        }

        setAction(ActionStates._RUNNING);

        addDynamicPhysicsBody();
    }

    @Override
    public void update(int spriteNum)
    {
        switch (getAction())
        {
            case _PAUSED:
            case _RUNNING:
            {
                if (!App.mapData.mapBox.contains(getCollisionRectangle()) || (collisionObject.idBottom == GraphicID._GROUND))
                {
                    ExplosionManager explosionManager = new ExplosionManager();
                    explosionManager.createExplosion(GraphicID.G_EXPLOSION256, this);

                    setAction(ActionStates._EXPLODING);
                }
                else
                {
                    if (distance.isEmpty())
                    {
                        App.missileBaseManager.activeMissiles--;

                        if (App.missileBaseManager.activeMissiles <= 0)
                        {
                            reset();
                        }

                        setAction(ActionStates._DEAD);
                    }
                    else
                    {
                        if (getAction() != ActionStates._PAUSED)
                        {
                            sprite.translate((speed.getX() * direction.getX()), (speed.getY() * direction.getY()));

                            distance.subX(speed.getX());
                            distance.subY(speed.getY());

                            if (distance.getY() <= 0)
                            {
                                speed.setY(0);
                            }

                            wrap();
                        }
                    }
                }
            }
            break;

            case _KILLED:
            case _HURT:
            {
                explode();
            }
            break;

            case _EXPLODING:
            {
            }
            break;

            case _DYING:
            {
                reset();

                setAction(ActionStates._DEAD);
            }
            break;

            default:
            {
                Trace.__FILE_FUNC("Unsupported spriteAction: " + getAction());
            }
            break;
        }

        isFlippedX = (direction.getX() == Movement._DIRECTION_LEFT);

        animate();

        updateCommon();
    }

    @Override
    public void animate()
    {
        sprite.setRegion(App.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
        elapsedAnimTime += Gdx.graphics.getDeltaTime();
    }

    public void explode()
    {
        ExplosionManager explosionManager = new ExplosionManager();
        explosionManager.createExplosion(GraphicID.G_EXPLOSION64, this);

        if (getAction() == ActionStates._KILLED)
        {
            App.gameProgress.stackPush(GameProgress.Stack._SCORE, PointsManager.getPoints(gid));
        }

        setAction(ActionStates._EXPLODING);
    }

    private void reset()
    {
        App.missileBaseManager.activeMissiles = 0;
        App.missileBaseManager.isMissileActive = false;
    }

    @Override
    public void onPositiveCollision(CollisionObject cobjHitting)
    {
        if (cobjHitting.gid == GraphicID.G_ROVER)
        {
            Shake.start();

            ExplosionManager explosionManager = new ExplosionManager();
            explosionManager.createExplosion(GraphicID.G_EXPLOSION256, this);

            if (!Developer.isGodMode())
            {
                App.getRover().setAction(ActionStates._HURT);
            }

            setAction(ActionStates._EXPLODING);
        }
        else if (cobjHitting.gid == GraphicID.G_LASER)
        {
            setAction(ActionStates._KILLED);
        }
    }

    @Override
    public void onNegativeCollision()
    {
    }

    @Override
    public void tidy(int index)
    {
        if (!App.gameProgress.roverDestroyed
            && !App.gameProgress.baseDestroyed
            && (direction.getY() != Movement._DIRECTION_UP))
        {
            App.gameProgress.getTimeBar().setToMaximum();
            App.gameProgress.getFuelBar().setToMaximum();
            App.getHud().update();

            App.getBase().setAction(ActionStates._STANDING);
        }

        collisionObject.kill();
        App.entityData.removeEntity(index);
    }
}

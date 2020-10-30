package com.richikin.jetman.entities.characters;

import com.badlogic.gdx.Gdx;
import com.richikin.enumslib.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.core.PointsManager;
import com.richikin.jetman.entities.managers.ExplosionManager;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.enumslib.GraphicID;
import com.richikin.jetman.graphics.camera.Shake;
import com.richikin.utilslib.physics.aabb.ICollisionListener;
import com.richikin.utilslib.developer.Developer;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.physics.Movement;

public class Missile extends GdxSprite implements ICollisionListener
{
    public Missile(App _app)
    {
        super(_app);
    }

    @Override
    public void initialise(SpriteDescriptor entityDescriptor)
    {
        create(entityDescriptor);

        direction.setY(((entityDescriptor._INDEX & 1) == 1) ? Movement._DIRECTION_UP : Movement._DIRECTION_DOWN);
        direction.setX((sprite.getX() < app.getRover().sprite.getX()) ? Movement._DIRECTION_RIGHT : Movement._DIRECTION_LEFT);

        if (direction.getY() == Movement._DIRECTION_UP)
        {
            distance.set(Gfx._VIEW_WIDTH * 2, Gfx._VIEW_HEIGHT * 2);
            speed.set(12.0f, 8.0f);

            sprite.setPosition(sprite.getX(), (app.getRover().sprite.getY() + app.getRover().frameHeight) - 4);
            sprite.setRotation((90.0f - ((speed.getY() / speed.getX()) * 100)) * direction.getX());
        }
        else
        {
            sprite.setPosition(sprite.getX(), (app.getRover().sprite.getY() + app.getRover().frameHeight) - 4);

            distance.setX(Math.abs(app.getRover().sprite.getX() - sprite.getX()));
            distance.setY(Math.abs(sprite.getY() - (app.getRover().sprite.getY() + (app.getRover().frameHeight / 3))));

            speed.setX(12.0f);
            speed.setY(distance.getY() / (distance.getX() / speed.getX()));
        }

        setAction(Actions._RUNNING);

        addCollisionListener(this);
    }

    @Override
    public void update(int spriteNum)
    {
        switch (getAction())
        {
            case _PAUSED:
            case _RUNNING:
            {
                if (!app.mapData.mapBox.contains(getCollisionRectangle()) || (collisionObject.idBottom == GraphicID._GROUND))
                {
                    ExplosionManager explosionManager = new ExplosionManager();
                    explosionManager.createExplosion(GraphicID.G_EXPLOSION256, this, app);

                    setAction(Actions._EXPLODING);
                }
                else
                {
                    if (distance.isEmpty())
                    {
                        app.missileBaseManager.activeMissiles--;

                        if (app.missileBaseManager.activeMissiles <= 0)
                        {
                            reset();
                        }

                        setAction(Actions._DEAD);
                    }
                    else
                    {
                        if (getAction() != Actions._PAUSED)
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

                setAction(Actions._DEAD);
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
        sprite.setRegion(app.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
        elapsedAnimTime += Gdx.graphics.getDeltaTime();
    }

    public void explode()
    {
        ExplosionManager explosionManager = new ExplosionManager();
        explosionManager.createExplosion(GraphicID.G_EXPLOSION64, this, app);

        if (getAction() == Actions._KILLED)
        {
            app.gameProgress.score.add(PointsManager.getPoints(gid));
        }

        setAction(Actions._EXPLODING);
    }

    private void reset()
    {
        app.missileBaseManager.activeMissiles = 0;
        app.missileBaseManager.isMissileActive = false;
    }

    @Override
    public void onPositiveCollision(GraphicID graphicID)
    {
        if (graphicID == GraphicID.G_ROVER)
        {
            Shake.start(app);

            ExplosionManager explosionManager = new ExplosionManager();
            explosionManager.createExplosion(GraphicID.G_EXPLOSION256, this, app);

            if (!Developer.isGodMode())
            {
                app.getRover().setAction(Actions._HURT);
            }

            setAction(Actions._EXPLODING);
        }
        else if (graphicID == GraphicID.G_LASER)
        {
            setAction(Actions._KILLED);
        }
    }

    @Override
    public void onNegativeCollision()
    {
    }

    @Override
    public void tidy(int _index)
    {
        if (!app.gameProgress.roverDestroyed
            && !app.gameProgress.baseDestroyed
            && (direction.getY() != Movement._DIRECTION_UP))
        {
            app.getHud().getTimeBar().setToMaximum();
            app.getHud().getFuelBar().setToMaximum();
            app.getHud().update();

            app.getBase().setAction(Actions._STANDING);
        }

        collisionObject.kill();
        app.entityData.removeEntity(_index);
    }
}

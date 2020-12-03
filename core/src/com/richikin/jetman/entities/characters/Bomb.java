
package com.richikin.jetman.entities.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.richikin.enumslib.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.managers.CraterManager;
import com.richikin.jetman.entities.managers.ExplosionManager;
import com.richikin.jetman.entities.types.Carryable;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.core.PlayServicesID;
import com.richikin.jetman.physics.Movement;
import com.richikin.jetman.physics.aabb.ICollisionListener;

public class Bomb extends Carryable
{
    public Bomb()
    {
        super(GraphicID.G_BOMB);
    }

    @Override
    public void animate()
    {
        elapsedAnimTime += Gdx.graphics.getDeltaTime();
        sprite.setRegion(App.entityUtils.getKeyFrame(animation, elapsedAnimTime, false));
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        if (!App.teleportManager.teleportActive)
        {
            super.draw(spriteBatch);
        }
    }

    @Override
    public void explode()
    {
        ExplosionManager explosionManager = new ExplosionManager();
        explosionManager.createExplosion(GraphicID.G_EXPLOSION64, this);

        direction.setY(Movement._DIRECTION_STILL);
        speed.setY(0);

        setAction(ActionStates._EXPLODING);
        elapsedAnimTime = 0;

        CraterManager craterManager = new CraterManager();

        if (craterManager.canMakeCrater(this))
        {
            int x = (int) (sprite.getX() / Gfx.getTileWidth());
            int y = (int) (App.getPlayer().sprite.getY() / Gfx.getTileHeight()) - 1;

            craterManager.makeCrater(x, y);
        }
    }

    @Override
    public void setCollisionListener()
    {
        addCollisionListener(new ICollisionListener()
        {
            @Override
            public void onPositiveCollision(GraphicID graphicID)
            {
                if (getAction() == ActionStates._FALLING)
                {
                    GraphicID contactID = App.collisionUtils.getBoxHittingBottom(App.getBomb()).gid;

                    switch (contactID)
                    {
                        case _GROUND:
                        {
                            direction.setY(Movement._DIRECTION_STILL);
                            speed.setY(0);

                            float minX = App.entityData.defenceStations[0].sprite.getX();
                            float maxX = App.entityData.defenceStations[1].sprite.getX()
                                + App.entityData.defenceStations[1].frameWidth;

                            if ((sprite.getX() >= minX) && (sprite.getX() <= maxX))
                            {
                                explode();

                                App.getBase().setAction(ActionStates._HURT);
                            }
                            else if (releaseXY.getY() > (initXYZ.getY() + Gfx.getTileHeight()))
                            {
                                explode();
                            }
                            else
                            {
                                setAction(ActionStates._STANDING);
                            }
                        }
                        break;

                        case G_MISSILE_BASE:
                        case G_MISSILE_LAUNCHER:
                        case G_DEFENDER:
                        {
                            direction.setY(Movement._DIRECTION_STILL);
                            speed.setY(0);

                            explode();

                            App.getBase().setAction(ActionStates._HURT);
                        }
                        break;

                        case G_ROVER:
                        case G_ROVER_BOOT:
                        {
                            isAttachedToRover = true;
                            direction.setY(Movement._DIRECTION_STILL);
                            speed.setY(0);
                            setAction(ActionStates._STANDING);

                            App.googleServices.unlockAchievement(PlayServicesID.achievement_bomb_collector.getID());
                        }
                        break;
                    }
                }
            }

            @Override
            public void onNegativeCollision()
            {
                if ((sprite.getY() + frameHeight) < 0)
                {
                    explode();
                }
            }

            @Override
            public void dispose()
            {
            }
        });
    }
}

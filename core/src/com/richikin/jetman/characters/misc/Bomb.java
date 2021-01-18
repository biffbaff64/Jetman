
package com.richikin.jetman.characters.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.richikin.enumslib.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.jetman.core.App;
import com.richikin.jetman.characters.managers.CraterManager;
import com.richikin.jetman.characters.managers.ExplosionManager;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.characters.basetypes.Carryable;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.enumslib.PlayServicesID;
import com.richikin.jetman.physics.aabb.CollisionObject;
import com.richikin.utilslib.physics.Movement;
import com.richikin.jetman.physics.aabb.ICollisionListener;

public class Bomb extends Carryable
{
    public Bomb()
    {
        super(GraphicID.G_BOMB);
    }

    @Override
    public void initialise(SpriteDescriptor descriptor)
    {
        super.initialise(descriptor);

        bodyCategory = Gfx.CAT_PLAYER_WEAPON;
        collidesWith = Gfx.CAT_FIXED_ENEMY | Gfx.CAT_GROUND | Gfx.CAT_MISSILE_BASE | Gfx.CAT_VEHICLE;

        addDynamicPhysicsBody();
    }

    @Override
    public void update(int spriteNum)
    {
        super.update(spriteNum);

        if (getAction() == ActionStates._DEAD)
        {
            App.entityData.managerList.get(App.entityManager ._bombManagerIndex).free();
        }
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

            craterManager.makeCrater(x);
        }
    }

    @Override
    public void setCollisionListener()
    {
        addCollisionListener(new ICollisionListener()
        {
            @Override
            public void onPositiveCollision(CollisionObject cobjHitting)
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

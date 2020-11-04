/*
 *  Copyright 01/05/2018 Red7Projects.
 *  <p>
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  <p>
 *  http://www.apache.org/licenses/LICENSE-2.0
 *  <p>
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.richikin.jetman.entities.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.richikin.enumslib.ActionStates;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.Entities;
import com.richikin.jetman.entities.managers.CraterManager;
import com.richikin.jetman.entities.managers.ExplosionManager;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.enumslib.GraphicID;
import com.richikin.utilslib.google.PlayServicesID;
import com.richikin.utilslib.maths.SimpleVec2F;
import com.richikin.utilslib.physics.Movement;
import com.richikin.utilslib.physics.aabb.ICollisionListener;
import com.richikin.utilslib.logging.Trace;

public class Bomb extends GdxSprite
{
    public SimpleVec2F releaseXY;
    public boolean     isAttachedToRover;
    public boolean     isAttachedToPlayer;

    public Bomb()
    {
        super(GraphicID.G_BOMB);

        bodyCategory = Gfx.CAT_PLAYER_WEAPON;
        collidesWith = Gfx.CAT_GROUND | Gfx.CAT_FIXED_ENEMY | Gfx.CAT_MISSILE_BASE;
    }

    @Override
    public void initialise(SpriteDescriptor descriptor)
    {
        create(descriptor);

        initXYZ.set(sprite.getX(), sprite.getY(), zPosition);

        Entities.stand(this);

        releaseXY = new SimpleVec2F();
        distance.set(0, 0);

        isAttachedToRover  = false;
        isAttachedToPlayer = false;

        setCollisionListener();
    }

    @Override
    public void update(int spriteNum)
    {
        switch (getAction())
        {
            case _STANDING:
            {
                if (isAttachedToPlayer)
                {
                    if (App.getPlayer().isOnGround
                        || (App.getPlayer().sprite.getY() < (initXYZ.getY() + frameHeight)))
                    {
                        sprite.setPosition(App.getPlayer().sprite.getX(), App.getPlayer().sprite.getY());
                    }
                    else
                    {
                        sprite.setPosition(App.getPlayer().sprite.getX(), (App.getPlayer().sprite.getY() - (frameHeight / 2)));
                    }

                    isAttachedToRover = false;
                }
                else
                {
                    if (isAttachedToRover)
                    {
                        if (App.getRover().lookingAt.getX() == Movement._DIRECTION_LEFT)
                        {
                            sprite.setPosition(App.getRover().sprite.getX() + 194, App.getRover().sprite.getY() + (131 - 43));
                        }
                        else
                        {
                            sprite.setPosition(App.getRover().sprite.getX() + 67, App.getRover().sprite.getY() + (131 - 43));
                        }
                    }
                }
            }
            break;

            case _FALLING:
            {
                isAttachedToRover  = false;
                isAttachedToPlayer = false;

                direction.setY(Movement._DIRECTION_DOWN);
                speed.y += 0.2f;
            }
            break;

            case _HURT:
            {
                setAction(ActionStates._DEAD);
            }

            case _EXPLODING:
            {
            }
            break;

            case _DYING:
            {
                setAction(ActionStates._DEAD);
                releaseXY    = null;
            }
            break;

            default:
            {
                Trace.__FILE_FUNC("Unsupported spriteAction: " + getAction());
            }
            break;
        }

        sprite.translate((speed.getX() * direction.getX()), (speed.getY() * direction.getY()));

        animate();

        updateCommon();
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
    public void animate()
    {
        elapsedAnimTime += Gdx.graphics.getDeltaTime();
        sprite.setRegion(App.entityUtils.getKeyFrame(animation, elapsedAnimTime, false));
    }

    public void explode()
    {
        ExplosionManager explosionManager = new ExplosionManager();
        explosionManager.createExplosion(GraphicID.G_EXPLOSION64, this);

        direction.setY(Movement._DIRECTION_STILL);
        speed.setY(0);

        Entities.explode(this);
        elapsedAnimTime = 0;

        CraterManager craterManager = new CraterManager();

        if (craterManager.canMakeCrater(this, false))
        {
            int x = (int) (sprite.getX() / Gfx.getTileWidth());
            int y = (int) (App.getPlayer().sprite.getY() / Gfx.getTileHeight()) - 1;

            craterManager.makeCrater(x, y);
        }
    }

    private void setCollisionListener()
    {
        addCollisionListener(new ICollisionListener()
        {
            @Override
            public void onPositiveCollision(GraphicID graphicID)
            {
                if (getAction() == ActionStates._FALLING)
                {
                    GraphicID contactID = App.collisionUtils.getBoxHittingBottom(App.getBomb()).gid;

                    if (contactID == GraphicID._GROUND)
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
                    else if ((contactID == GraphicID.G_MISSILE_BASE)
                        || (contactID == GraphicID.G_MISSILE_LAUNCHER)
                        || (contactID == GraphicID.G_DEFENDER))
                    {
                        direction.setY(Movement._DIRECTION_STILL);
                        speed.setY(0);

                        explode();

                        App.getBase().setAction(ActionStates._HURT);
                    }
                    else if (contactID == GraphicID.G_ROVER_BOOT)
                    {
                        isAttachedToRover = true;
                        direction.setY(Movement._DIRECTION_STILL);
                        speed.setY(0);
                        setAction(ActionStates._STANDING);

                        App.googleServices.unlockAchievement(PlayServicesID.achievement_bomb_collector.getID());
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

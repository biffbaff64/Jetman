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
import com.badlogic.gdx.math.MathUtils;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.enumslib.ActionStates;
import com.richikin.jetman.core.App;
import com.richikin.jetman.core.GameProgress;
import com.richikin.jetman.core.PointsManager;
import com.richikin.jetman.entities.Entities;
import com.richikin.jetman.entities.managers.ExplosionManager;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.GenericCollisionListener;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.enumslib.GraphicID;
import com.richikin.utilslib.logging.StopWatch;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.physics.Movement;

import java.util.concurrent.TimeUnit;

public class ThreeBallsUFO extends GdxSprite
{
    private StopWatch stopWatch;
    private float restingTime;

    public ThreeBallsUFO()
    {
        super(GraphicID.G_3BALLS_UFO);
    }

    @Override
    public void initialise(SpriteDescriptor entityDescriptor)
    {
        create(entityDescriptor);

        initXYZ.set(sprite.getX(), sprite.getY(), zPosition);

        bodyCategory = Gfx.CAT_MOBILE_ENEMY;
        collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_PLAYER_WEAPON;

        distance.set(Gfx._VIEW_WIDTH * 6, 0);
        speed.set(1 + MathUtils.random(2), 0);

        if (sprite.getX() < App.getPlayer().sprite.getX())
        {
            direction.set(Movement._DIRECTION_RIGHT, Movement._DIRECTION_STILL);
        }
        else
        {
            direction.set(Movement._DIRECTION_LEFT, Movement._DIRECTION_STILL);
        }

        setAction(ActionStates._RUNNING);
        stopWatch = StopWatch.start();
        restingTime = (MathUtils.random(30, 50) * 100);

        addCollisionListener(new GenericCollisionListener(this));
    }

    @Override
    public void update(int spriteNum)
    {
        switch (getAction())
        {
            case _RUNNING:
            {
                if (App.entityUtils.isOnScreen(this)
                    && (stopWatch.time(TimeUnit.MILLISECONDS) > restingTime))
                {
                    shoot();

                    stopWatch.reset();
                    restingTime = (MathUtils.random(30, 50) * 100);
                }

                move();

                if (collisionObject.isHittingPlayer)
                {
                    setAction(ActionStates._HURT);
                    elapsedAnimTime = 0;
                }
            }
            break;

            case _KILLED:
            case _HURT:
            {
                ExplosionManager explosionManager = new ExplosionManager();
                explosionManager.createExplosion(GraphicID.G_EXPLOSION64, this);

                if (getAction() == ActionStates._KILLED)
                {
                    App.gameProgress.stackPush(GameProgress.Stack._SCORE, PointsManager.getPoints(gid));
                }

                setAction(ActionStates._EXPLODING);
            }
            break;

            case _EXPLODING:
            {
            }
            break;

            case _DYING:
            {
                setAction(ActionStates._DEAD);
            }
            break;

            default:
            {
                Trace.__FILE_FUNC("Unsupported spriteAction: " + getAction());
            }
            break;
        }

        animate();

        updateCommon();
    }

    @Override
    public void animate()
    {
        if (getAction() == ActionStates._RUNNING)
        {
            elapsedAnimTime += Gdx.graphics.getDeltaTime();
            sprite.setRegion(App.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
        }
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        sprite.setFlip(isFlippedX, false);

        super.draw(spriteBatch);
    }

    private float ticks = 0;

    private void move()
    {
        ticks++;

        float x = sprite.getX();
        float y;

        x += (speed.getX() * direction.getX());
        y = initXYZ.getY() + (float) (65 * Math.sin((ticks * 0.5 * Math.PI) / 25)) + 225;

        sprite.setPosition(x, y);

        wrap();

        isFlippedX = (direction.getX() == Movement._DIRECTION_RIGHT);
    }

    private void shoot()
    {
        SpriteDescriptor descriptor   = Entities.getDescriptor(GraphicID.G_UFO_BULLET);
        descriptor._SIZE              = GameAssets.getAssetSize(GraphicID.G_UFO_BULLET);
        descriptor._POSITION.x        = (int) ((sprite.getX() + frameWidth) / Gfx.getTileWidth());
        descriptor._POSITION.y        = (int) (sprite.getY() / Gfx.getTileHeight());
        descriptor._POSITION.z        = App.entityUtils.getInitialZPosition(GraphicID.G_UFO_BULLET);

        for (int i = 0; i < 15; i++)
        {
            descriptor._INDEX = App.entityData.entityMap.size;

            UfoWeapon weapon = new UfoWeapon();
            weapon.initialise(descriptor);
            App.entityData.addEntity(weapon);
        }
    }
}

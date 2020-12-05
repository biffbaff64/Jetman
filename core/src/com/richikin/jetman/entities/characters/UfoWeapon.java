/*
 *  Copyright 10/05/2018 Red7Projects.
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
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.richikin.enumslib.ActionStates;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.managers.ExplosionManager;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.enumslib.GraphicID;
import com.richikin.utilslib.logging.Trace;
import com.richikin.jetman.physics.Movement;

public class UfoWeapon extends GdxSprite
{
    private final Color[] colours = new Color[]
        {
            Color.WHITE,
            Color.BLUE,
            Color.RED,
            Color.YELLOW,
            Color.PURPLE,
            Color.ORANGE,
            Color.GREEN,
            Color.CYAN,
            Color.MAROON,
        };

    private int colourIndex;

    public UfoWeapon()
    {
        super(GraphicID.G_UFO_BULLET);
    }

    @Override
    public void initialise(SpriteDescriptor entityDescriptor)
    {
        create(entityDescriptor);

        initXYZ.set(sprite.getX(), sprite.getY(), zPosition);

        bodyCategory = Gfx.CAT_MOBILE_ENEMY;
        collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_PLAYER_WEAPON | Gfx.CAT_GROUND;

        int random = MathUtils.random(100);

        if (random < 33)
        {
            direction.set(Movement._DIRECTION_RIGHT, Movement._DIRECTION_DOWN);
        }
        else if (random < 66)
        {
            direction.set(Movement._DIRECTION_LEFT, Movement._DIRECTION_DOWN);
        }
        else
        {
            direction.set(Movement._DIRECTION_STILL, Movement._DIRECTION_DOWN);
        }

        float angle = 0.1f + MathUtils.random(0.05f);
        float speedMod = MathUtils.random(0.004f, 0.008f);

        speed.setX((Gfx._VIEW_WIDTH * speedMod) * angle);
        speed.setY(Gfx._VIEW_HEIGHT * speedMod);

        setAction(ActionStates._RUNNING);
        colourIndex = 0;
    }

    @Override
    public void update(int spriteNum)
    {
        switch (getAction())
        {
            case _RUNNING:
            {
                sprite.setColor(colours[colourIndex]);

                if (++colourIndex >= colours.length)
                {
                    colourIndex = 0;
                }

                sprite.translateX(speed.getX() * direction.getX());
                sprite.translateY(speed.getY() * direction.getY());
            }
            break;

            case _KILLED:
            case _HURT:
            {
                ExplosionManager explosionManager = new ExplosionManager();
                explosionManager.createExplosion(GraphicID.G_EXPLOSION12, this);
                setAction(ActionStates._EXPLODING);
                setAction(ActionStates._DEAD);
                bodyCategory = Gfx.CAT_NOTHING;
                collidesWith = Gfx.CAT_NOTHING;
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
    public void postUpdate(int spriteNum)
    {
        if (collisionObject.idBottom == GraphicID._GROUND)
        {
            setAction(ActionStates._HURT);
        }
    }

    @Override
    public void animate()
    {
        if (getAction() == ActionStates._RUNNING)
        {
            sprite.setRegion(App.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
            elapsedAnimTime += Gdx.graphics.getDeltaTime();
        }
    }

    @Override
    public void tidy(int _index)
    {
        collisionObject.kill();
        App.entityData.removeEntity(_index);
    }
}

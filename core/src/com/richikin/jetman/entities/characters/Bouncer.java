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
import com.badlogic.gdx.math.MathUtils;
import com.richikin.jetman.core.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.core.PointsManager;
import com.richikin.jetman.entities.managers.ExplosionManager;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.physics.aabb.ICollisionListener;
import com.richikin.utilslib.logging.StopWatch;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.physics.Movement;

public class Bouncer extends GdxSprite
{
    private static final float _BOUNCE_X_SPEED = 2.0f;
    private static final float _BOUNCE_Y_SPEED = -0.25f;
    private static final int _MAX_BOUNCE_SPEED = 10;
    private static final int _EXTRA_BOUCE_SPEED = 4;

    private StopWatch stopWatch;
    private float maxBounceSpeed;

    public Bouncer(GraphicID graphicID, App _app)
    {
        super(graphicID, _app);

        this.app = _app;
    }

    @Override
    public void initialise(SpriteDescriptor entityDescriptor)
    {
        create(entityDescriptor);

        initXYZ.set(sprite.getX(), sprite.getY(), zPosition);

        bodyCategory = Gfx.CAT_MOBILE_ENEMY;
        collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_PLAYER_WEAPON | Gfx.CAT_GROUND;

        speed.set(0, 0);
        distance.set(0, 0);
        direction.set(Movement._DIRECTION_STILL, Movement._DIRECTION_STILL);

        setAction(Actions._RUNNING);
        stopWatch = StopWatch.start();
        isRotating = true;

        maxBounceSpeed = _MAX_BOUNCE_SPEED + MathUtils.random(_EXTRA_BOUCE_SPEED);

        //
        // Add a custom CollisionListener
        addCollisionListener(new ICollisionListener()
        {
            @Override
            public void onPositiveCollision(final GraphicID graphicID)
            {
                if (graphicID == GraphicID.G_LASER)
                {
                    setAction(Actions._KILLED);
                }
                else
                {
//                    if (graphicID == GraphicID.G_PLAYER)
//                    {
//                        setAction(Actions._HURT);
//                    }
//                    else
                    {
                        if (graphicID == GraphicID._CRATER)
                        {
                            if (MathUtils.random(100) < 35)
                            {
                                direction.toggleX();
                                speed.set(_BOUNCE_X_SPEED, maxBounceSpeed);
                            }
                        }
                    }
                }

                collisionObject.setInvisibility(1000);
            }

            @Override
            public void onNegativeCollision()
            {
            }

            @Override
            public void dispose()
            {
            }
        });
    }

    @Override
    public void update(int spriteNum)
    {
        switch (getAction())
        {
            case _RUNNING:
            {
                elapsedAnimTime += Gdx.graphics.getDeltaTime();
                sprite.setRegion(app.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));

                rotateSpeed = -4.0f * direction.getX();

                moveBouncer();
            }
            break;

            case _KILLED:
            case _HURT:
            {
                ExplosionManager explosionManager = new ExplosionManager();
                explosionManager.createExplosion(GraphicID.G_EXPLOSION64, this, app);

                if (getAction() == Actions._KILLED)
                {
                    app.gameProgress.score.add(PointsManager.getPoints(gid));
                }

                setAction(Actions._EXPLODING);
            }
            break;

            case _EXPLODING:
            {
            }
            break;

            case _DYING:
            {
                setAction(Actions._DEAD);
            }
            break;

            default:
            {
                Trace.__FILE_FUNC("Unsupported spriteAction: " + getAction());
            }
            break;
        }

        updateCommon();
    }

    @Override
    public void animate()
    {
        switch (getAction())
        {
            case _STANDING:
            case _RUNNING:
            {
                if (animation != null)
                {
                    sprite.setRegion(app.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
                    elapsedAnimTime += Gdx.graphics.getDeltaTime();
                }
            }
            break;

            case _DYING:
            case _EXPLODING:
            default:
            {
            }
            break;
        }
    }

    private void moveBouncer()
    {
        if (direction.getY() == Movement._DIRECTION_STILL)
        {
            direction.set(Movement._DIRECTION_RIGHT, Movement._DIRECTION_UP);
            speed.set
                (
                    MathUtils.random(_BOUNCE_X_SPEED, _BOUNCE_X_SPEED + 1.5f),
                    maxBounceSpeed
                );
        }
        else if ((direction.getY() == Movement._DIRECTION_UP) && (speed.getY() <= 0))
        {
            direction.toggleY();
            speed.setY(0.0f);
        }
        else if ((direction.getY() == Movement._DIRECTION_DOWN) && (speed.getY() > maxBounceSpeed))
        {
            direction.toggleY();
            maxBounceSpeed = _MAX_BOUNCE_SPEED + MathUtils.random(_EXTRA_BOUCE_SPEED);
            speed.setY(maxBounceSpeed);
        }
        else
        {
            sprite.translate((speed.getX() * direction.getX()), (speed.getY() * direction.getY()));

            speed.y += (_BOUNCE_Y_SPEED * direction.getY());

            stopWatch.reset();
        }

        wrap();

        isFlippedX = (direction.getX() == Movement._DIRECTION_RIGHT);
    }
}

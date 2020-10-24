/*
 *  Copyright 09/07/2018 Red7Projects.
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
import com.badlogic.gdx.graphics.g2d.Animation;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.core.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.utilslib.logging.StopWatch;
import com.richikin.utilslib.logging.Trace;

import java.util.concurrent.TimeUnit;

public class ZapSprite extends GdxSprite
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

    private       StopWatch stopWatch;
    private       int       colourIndex;
    private       int       restingTime;
    private final App       app;
    private final GdxSprite parent;

    /**
     * Constructor
     *
     * @param graphicID The {@link GraphicID of this sprite}
     * @param _app     an instance of the {@link App}
     */
    public ZapSprite(GraphicID graphicID, GdxSprite _parent, App _app)
    {
        super(graphicID, _app);

        this.app = _app;
        this.parent = _parent;
    }

    @Override
    public void initialise(SpriteDescriptor descriptor)
    {
        create(descriptor);

        sprite.setPosition(parent.sprite.getX() + 2, (parent.sprite.getY() + parent.frameHeight) - 4);
        initXYZ.set(sprite.getX(), sprite.getY(), zPosition);
        animation.setPlayMode(Animation.PlayMode.LOOP);
        animation.setFrameDuration(0.4f / 6f);

        bodyCategory = parent.bodyCategory;
        collidesWith = parent.collidesWith;
        stopWatch = StopWatch.start();
        setAction(Actions._STANDING);
        isDrawable = true;
    }

    @Override
    public void update(int spriteNum)
    {
        switch (getAction())
        {
            case _STANDING:
            {
                if (animation.isAnimationFinished(elapsedAnimTime))
                {
                    setAction(Actions._HIDING);
                    stopWatch.reset();
                    restingTime = 750;
                    isDrawable = false;
                }
                else
                {
                    sprite.setRegion(app.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
                    elapsedAnimTime += Gdx.graphics.getDeltaTime();
                }

                sprite.setColor(colours[colourIndex]);

                if (++colourIndex >= colours.length)
                {
                    colourIndex = 0;
                }
            }
            break;

            case _HIDING:
            {
                if (stopWatch.time(TimeUnit.MILLISECONDS) >= restingTime)
                {
                    setAction(Actions._STANDING);
                    elapsedAnimTime = 0;
                    isDrawable = true;
                }
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
}

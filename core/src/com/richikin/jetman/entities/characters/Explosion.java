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
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Intersector;
import com.richikin.jetman.core.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.GdxSprite;
import com.richikin.jetman.entities.SpriteDescriptor;
import com.richikin.jetman.entities.hero.MainPlayer;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;

public class Explosion extends GdxSprite
{
    private       GdxSprite parent;
    private final App       app;

    /**
     * Constructor
     *
     * @param graphicID The {@link GraphicID of this sprite}
     * @param _app      an instance of the {@link App}
     */
    public Explosion(GraphicID graphicID, App _app)
    {
        super(graphicID, _app);

        this.app = _app;

        bodyCategory = Gfx.CAT_NOTHING;
        collidesWith = Gfx.CAT_NOTHING;
    }

    @Override
    public void initialise(SpriteDescriptor entityDescriptor)
    {
        create(entityDescriptor);

        this.parent = entityDescriptor._PARENT;

        initXYZ.set(sprite.getX(), sprite.getY(), zPosition);

        animation.setFrameDuration(0.4f / 6.0f);
        animation.setPlayMode(Animation.PlayMode.NORMAL);

        sprite.setCenter(parent.sprite.getX() + (parent.frameWidth / 2), parent.sprite.getY() + (parent.frameHeight / 2));
        setAction(Actions._RUNNING);

//        if (app.entityUtils.isOnScreen(this))
//        {
//            Sfx.inst().startSound(Sfx.inst().SFX_EXPLOSION_1);
//        }
    }

    @Override
    public void update(int spriteNum)
    {
        if (animation.isAnimationFinished(elapsedAnimTime))
        {
            setAction(Actions._DEAD);
            isDrawable   = false;

            if (parent.gid == GraphicID.G_PLAYER)
            {
                ((MainPlayer) parent).handleDying();
            }
            else
            {
                parent.setAction(Actions._DYING);
            }
        }
        else
        {
            if (animation.getKeyFrameIndex(elapsedAnimTime) == (animFrames.length / 4))
            {
                parent.isDrawable = false;

                if ((app.getBomb() != null)
                    && !app.getBomb().isAttachedToRover
                    && !app.getBomb().isAttachedToPlayer
                    && (app.getBomb().spriteAction == Actions._STANDING))
                {
                    if (Intersector.overlaps(getCollisionRectangle(), app.getBomb().getCollisionRectangle()))
                    {
                        app.getBomb().explode();
                    }
                }
            }

            sprite.setRegion(app.entityUtils.getKeyFrame(animation, elapsedAnimTime, false));
            elapsedAnimTime += Gdx.graphics.getDeltaTime();
        }
    }
}

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
import com.richikin.enumslib.ActionStates;
import com.richikin.jetman.audio.AudioData;
import com.richikin.jetman.audio.GameAudio;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.entities.hero.MainPlayer;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.enumslib.GraphicID;

public class Explosion extends GdxSprite
{
    private GdxSprite parent;

    /**
     * Constructor
     *
     * @param graphicID The {@link GraphicID of this sprite}
     */
    public Explosion(GraphicID graphicID)
    {
        super(graphicID);
    }

    @Override
    public void initialise(SpriteDescriptor descriptor)
    {
        create(descriptor);

        this.parent = descriptor._PARENT;

        initXYZ.set(sprite.getX(), sprite.getY(), zPosition);

        bodyCategory = Gfx.CAT_NOTHING;
        collidesWith = Gfx.CAT_NOTHING;

        animation.setFrameDuration(0.4f / 6.0f);
        animation.setPlayMode(Animation.PlayMode.NORMAL);

        sprite.setCenter(parent.sprite.getX() + ((float) parent.frameWidth / 2), parent.sprite.getY() + ((float) parent.frameHeight / 2));
        setAction(ActionStates._RUNNING);

        if (App.entityUtils.isOnScreen(this))
        {
            GameAudio.inst().startSound(AudioData.SFX_EXPLOSION_1);
        }
    }

    @Override
    public void update(int spriteNum)
    {
        if (animation.isAnimationFinished(elapsedAnimTime))
        {
            setAction(ActionStates._DEAD);
            isDrawable = false;

            if (parent.gid == GraphicID.G_PLAYER)
            {
                ((MainPlayer) parent).handleDying();
            }
            else
            {
                parent.setAction(ActionStates._DYING);
            }
        }
        else
        {
            if (animation.getKeyFrameIndex(elapsedAnimTime) == (animFrames.length / 4))
            {
                parent.isDrawable = false;

                if ((App.getBomb() != null)
                    && !App.getBomb().isAttachedToRover
                    && !App.getBomb().isAttachedToPlayer
                    && (App.getBomb().getAction() == ActionStates._STANDING))
                {
                    if (Intersector.overlaps(getCollisionRectangle(), App.getBomb().getCollisionRectangle()))
                    {
                        App.getBomb().explode();
                    }
                }
            }

            sprite.setRegion(App.entityUtils.getKeyFrame(animation, elapsedAnimTime, false));
            elapsedAnimTime += Gdx.graphics.getDeltaTime();
        }
    }

    @Override
    public void tidy(int _index)
    {
        collisionObject.kill();
        App.entityData.removeEntity(_index);
        parent = null;
    }
}

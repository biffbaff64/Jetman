
package com.richikin.jetman.characters.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.math.Intersector;
import com.richikin.enumslib.ActionStates;
import com.richikin.enumslib.GraphicID;
import com.richikin.jetman.audio.AudioData;
import com.richikin.jetman.audio.GameAudio;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.graphics.Gfx;

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
                App.getPlayer().handleDying();
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
    public void tidy(int index)
    {
        collisionObject.kill();
        App.entityData.removeEntity(index);
        parent = null;
    }
}

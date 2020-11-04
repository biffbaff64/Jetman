/*
 *  Copyright 15/08/2018 Red7Projects.
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
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.enumslib.ActionStates;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.enumslib.GraphicID;
import com.richikin.utilslib.logging.Trace;

public class TwinkleStar extends GdxSprite
{
    public TwinkleStar()
    {
        super(GraphicID.G_TWINKLE_STAR);
    }

    @Override
    public void initialise(SpriteDescriptor descriptor)
    {
        create(descriptor);

        bodyCategory = Gfx.CAT_SCENERY;
        collidesWith = Gfx.CAT_NOTHING;

        initXYZ.set(sprite.getX(), sprite.getY(), zPosition);

        sprite.setScale(0.225f + MathUtils.random(0.5f));
        sprite.setPosition(initXYZ.getX(), initXYZ.getY());

        animation.setFrameDuration(1.2f / 6);
        elapsedAnimTime = 0;

        setAction(ActionStates._STANDING);
        isDrawable = true;
    }

    @Override
    public void update(int spriteNum)
    {
        if (getAction() != ActionStates._STANDING)
        {
            Trace.__FILE_FUNC("Unsupported spriteAction: " + getAction());
        }

        animate();

        updateCommon();
    }

    @Override
    public void animate()
    {
        sprite.setRegion(App.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));

        elapsedAnimTime += Gdx.graphics.getDeltaTime();
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        if (isDrawable)
        {
            float originX = (App.baseRenderer.parallaxGameCamera.camera.position.x - (float) (Gfx._HUD_WIDTH / 2));
            float originY = (App.baseRenderer.parallaxGameCamera.camera.position.y - (float) (Gfx._HUD_HEIGHT / 2));

            sprite.setPosition(originX + initXYZ.getX(), originY + initXYZ.getY());

            super.draw(spriteBatch);
        }
    }
}

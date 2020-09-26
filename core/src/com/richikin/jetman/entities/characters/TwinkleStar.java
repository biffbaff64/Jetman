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
import com.richikin.jetman.entities.GdxSprite;
import com.richikin.jetman.core.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.SpriteDescriptor;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.utils.logging.Trace;

public class TwinkleStar extends GdxSprite
{
    private App app;

    public TwinkleStar(App _app)
    {
        super(GraphicID.G_TWINKLE_STAR, _app);

        this.app = _app;

        bodyCategory = Gfx.CAT_SCENERY;
        collidesWith = Gfx.CAT_NOTHING;
    }

    @Override
    public void initialise(SpriteDescriptor entityDescriptor)
    {
        create(entityDescriptor);

        sprite.setScale(0.225f + MathUtils.random(0.5f));

        animation.setFrameDuration(1.2f / 6);
        elapsedAnimTime = 0;

        setAction(Actions._STANDING);
        isDrawable = true;
    }

    @Override
    public void update(int spriteNum)
    {
        if (getSpriteAction() != Actions._STANDING)
        {
            Trace.__FILE_FUNC("Unsupported spriteAction: " + getSpriteAction());
        }

        animate();

        updateCommon();
    }

    @Override
    public void animate()
    {
        sprite.setRegion(app.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));

        elapsedAnimTime += Gdx.graphics.getDeltaTime();
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        final float originX = app.mapData.mapPosition.getX();
        final float originY = app.mapData.mapPosition.getY();

        sprite.setPosition(originX + initXYZ.getX(), initXYZ.getY());

        super.draw(spriteBatch);
    }
}

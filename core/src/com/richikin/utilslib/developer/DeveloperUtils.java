/*
 *  Copyright 31/01/2019 Red7Projects.
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

package com.richikin.utilslib.developer;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.richikin.utilslib.App;

public class DeveloperUtils
{
    private static final ShapeRenderer shapeRenderer = new ShapeRenderer();

    public static void drawRect(int x, int y, int width, int height, int thickness)
    {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
        shapeRenderer.box(x, y, 0, width, height, thickness);
        shapeRenderer.end();
    }

    public static void drawRect(int x, int y, int width, int height, int thickness, App app)
    {
        TextureRegion debugTextureRegion = app.assets.getObjectRegion("solid_red32x32");

        drawRect
            (
                debugTextureRegion,
                x,
                y,
                width,
                height,
                thickness,
                app
            );
    }

    public static void drawRect(TextureRegion textureRegion, int x, int y, int width, int height, int thickness, App app)
    {
        app.spriteBatch.draw(textureRegion, x, y, width, thickness);
        app.spriteBatch.draw(textureRegion, x, y, thickness, height);
        app.spriteBatch.draw(textureRegion, x, (y + height) - thickness, width, thickness);
        app.spriteBatch.draw(textureRegion, (x + width) - thickness, y, thickness, height);
    }
}

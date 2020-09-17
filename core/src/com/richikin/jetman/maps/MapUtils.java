/*
 *
 *  * *****************************************************************************
 *  *  Copyright 27/03/2017 See AUTHORS file.
 *  *  <p>
 *  *  Licensed under the Apache License, Version 2.0 (the "License");
 *  *  you may not use this file except in compliance with the License.
 *  *  You may obtain a copy of the License at
 *  *  <p>
 *  *  http://www.apache.org/licenses/LICENSE-2.0
 *  *  <p>
 *  *  Unless required by applicable law or agreed to in writing, software
 *  *  distributed under the License is distributed on an "AS IS" BASIS,
 *  *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  *  See the License for the specific language governing permissions and
 *  *  limitations under the License.
 *  * ***************************************************************************
 */

package com.richikin.jetman.maps;

import com.badlogic.gdx.utils.Array;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.SpriteDescriptor;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;

public class MapUtils
{
    private final App app;

    public MapUtils(App _app)
    {
        this.app = _app;
    }

    public void update() {}

    public void positionAt(int x, int y)
    {
        app.mapData.previousMapPosition.set(app.mapData.mapPosition);

        try
        {
            app.mapData.mapPosition.setX
                (
                    (int) Math.max(app.mapData.minScrollX,
                        (x + (app.getPlayer().frameWidth / 2)) - (float) (Gfx._VIEW_WIDTH / 2))
                );

            app.mapData.mapPosition.setX(Math.min(app.mapData.mapPosition.getX(), app.mapData.maxScrollX));

            app.mapData.mapPosition.setY
                (
                    (int) Math.max(app.mapData.minScrollY,
                        (y + (app.getPlayer().frameHeight / 2)) - (float) (Gfx._VIEW_HEIGHT / 2))
                );

            app.mapData.mapPosition.setY(Math.min(app.mapData.mapPosition.getY(), app.mapData.maxScrollY));
        }
        catch (NullPointerException npe)
        {
            app.mapData.mapPosition.set(0, 0);
        }

        app.parallaxManager.scroll();
    }

    public Array<SpriteDescriptor> findMultiTiles(final GraphicID targetGID)
    {
        Array<SpriteDescriptor> tiles = new Array<>();

        for (SpriteDescriptor marker : app.mapData.placementTiles)
        {
            if (marker._GID == targetGID)
            {
                tiles.add(marker);
            }
        }

        return tiles;
    }
}

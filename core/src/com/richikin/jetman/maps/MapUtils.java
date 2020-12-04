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
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.enumslib.GraphicID;

public class MapUtils
{
    public MapUtils()
    {
    }

    public void update()
    {
    }

    public void positionAt(int x, int y)
    {
        App.mapData.previousMapPosition.set(App.mapData.mapPosition);

        try
        {
            App.mapData.mapPosition.setX
                (
                    (int) Math.max(App.mapData.minScrollX,
                    (float) ((x + (App.getPlayer().frameWidth / 2)) - (Gfx._VIEW_WIDTH / 2)))
                );

            App.mapData.mapPosition.setX(Math.min(App.mapData.mapPosition.getX(), App.mapData.maxScrollX));

            App.mapData.mapPosition.setY
                (
                    (int) Math.max(App.mapData.minScrollY,
                    (float) ((y + (App.getPlayer().frameHeight / 2)) - (Gfx._VIEW_HEIGHT / 2)))
                );

            App.mapData.mapPosition.setY(Math.min(App.mapData.mapPosition.getY(), App.mapData.maxScrollY));

            App.parallaxManager.scroll();
        }
        catch (NullPointerException npe)
        {
            App.mapData.mapPosition.set(0, 0);
        }
    }

    public Array<SpriteDescriptor> findMultiTiles(final GraphicID targetGID)
    {
        Array<SpriteDescriptor> tiles = new Array<>();

        for (SpriteDescriptor marker : App.mapData.placementTiles)
        {
            if (marker._GID == targetGID)
            {
                tiles.add(marker);
            }
        }

        return tiles;
    }
}

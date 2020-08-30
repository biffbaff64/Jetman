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
            app.mapData.mapPosition.setY
                (
                    (int) Math.max(app.mapData.minScrollY,
                        (y + (app.getPlayer().frameHeight / 2)) - (float) (Gfx._VIEW_HEIGHT / 2))
                );

            app.mapData.mapPosition.setY(Math.min(app.mapData.mapPosition.getY(), app.mapData.maxScrollY));

            app.mapData.mapPosition.setX
                (
                    (int) Math.max(app.mapData.minScrollX,
                        (x + (app.getPlayer().frameWidth / 2)) - (float) (Gfx._VIEW_WIDTH / 2))
                );

            app.mapData.mapPosition.setX(Math.min(app.mapData.mapPosition.getX(), app.mapData.maxScrollX));
        }
        catch (NullPointerException npe)
        {
            app.mapData.mapPosition.set(0, 0);
        }

        if ((app.mapData.mapPosition.getX() > app.mapData.minScrollX)
            && (app.mapData.mapPosition.getX() < app.mapData.maxScrollX))
        {
            if (app.mapData.previousMapPosition.getX() < app.mapData.mapPosition.getX())
            {
                app.baseRenderer.parallaxBackground.scrollLayersLeft();
                app.baseRenderer.parallaxBackground.scrollLayersLeft();
            }
            else if (app.mapData.previousMapPosition.getX() > app.mapData.mapPosition.getX())
            {
                app.baseRenderer.parallaxBackground.scrollLayersRight();
                app.baseRenderer.parallaxBackground.scrollLayersRight();
            }
        }

        if ((app.mapData.mapPosition.getY() > app.mapData.minScrollY)
            && (app.mapData.mapPosition.getY() < app.mapData.maxScrollY))
        {
            if (app.mapData.previousMapPosition.getY() < app.mapData.mapPosition.getY())
            {
                app.baseRenderer.parallaxBackground.scrollLayersDown();
                app.baseRenderer.parallaxBackground.scrollLayersDown();
            }
            else if (app.mapData.previousMapPosition.getY() > app.mapData.mapPosition.getY())
            {
                app.baseRenderer.parallaxBackground.scrollLayersUp();
                app.baseRenderer.parallaxBackground.scrollLayersUp();
            }
        }
    }

    /**
     * Find all instances of Marker tiles with the specified GraphicID.
     *
     * @param targetGID The {@link GraphicID} to search for.
     *
     * @return  Array of valid {@link MarkerTile}.
     */
    public Array<MarkerTile> findMultiTiles(final GraphicID targetGID)
    {
        Array<MarkerTile> tiles = new Array<>();

        for (MarkerTile marker : app.mapData.placementTiles)
        {
            if (marker._GID == targetGID)
            {
                tiles.add(marker);
            }
        }

        return tiles;
    }
}

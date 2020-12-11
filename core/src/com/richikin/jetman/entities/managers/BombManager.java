/*
 *  Copyright 28/04/2018 Red7Projects.
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

package com.richikin.jetman.entities.managers;

import com.badlogic.gdx.math.MathUtils;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.Entities;
import com.richikin.jetman.entities.characters.Bomb;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.enumslib.GraphicID;
import com.richikin.enumslib.TileID;
import com.richikin.utilslib.logging.StopWatch;

import java.util.concurrent.TimeUnit;

public class BombManager extends GenericEntityManager
{
    private int       totalBombsUsed;
    private StopWatch spawnDelay;

    public BombManager()
    {
        super(GraphicID.G_BOMB);
    }

    @Override
    public void init()
    {
        this.totalBombsUsed = 0;
        this.spawnDelay     = StopWatch.start();

        activeCount = 0;
    }

    @Override
    public void update()
    {
        if (((spawnDelay.time(TimeUnit.MILLISECONDS) > 2000) || (totalBombsUsed == 0))
            && App.entityUtils.canUpdate(GraphicID.G_BOMB)
            && (activeCount == 0))
        {
            if (App.getRover() != null)
            {
                if (hasCreatedBomb())
                {
                    if (totalBombsUsed > 1)
                    {
                        App.getHud().messageManager.addZoomMessage("new_bomb", 3500);
                        App.getHud().messageManager.setPosition
                            (
                                "new_bomb",
                                (int) AppConfig.hudOriginX + 185,
                                (int) AppConfig.hudOriginY + (720 - 167)
                            );

                        App.getHud().alphaDisplay.setMessage("     NEW BOMB READY!           ");
                    }
                }
            }
        }
    }

    private boolean hasCreatedBomb()
    {
        boolean created = false;

        if (App.entityUtils.canUpdate(GraphicID.G_BOMB))
        {
            int markerX = (int) (App.getRover().sprite.getX() / Gfx.getTileWidth());
            int offset  = (totalBombsUsed == 0) ? 0 : MathUtils.random(10, 30);

            if (MathUtils.random(100) < 50)
            {
                markerX += (App.getRover().frameWidth + (Gfx.getTileWidth() * 2)) / Gfx.getTileWidth();
                markerX += offset;
            }
            else
            {
                markerX -= (Gfx.getTileWidth() * 3) / Gfx.getTileWidth();
                markerX -= offset;
            }

            if (isValidPosition(markerX))
            {
                descriptor             = App.entities.getDescriptor(GraphicID.G_BOMB);
                descriptor._SIZE       = GameAssets.getAssetSize(GraphicID.G_BOMB);
                descriptor._POSITION.x = markerX;
                descriptor._POSITION.y = (int) (App.getRover().getPosition().y / Gfx.getTileHeight());
                descriptor._POSITION.z = App.entityUtils.getInitialZPosition(GraphicID.G_BOMB);
                descriptor._INDEX      = App.entityData.entityMap.size;

                App.entities.bomb = new Bomb();
                App.entities.bomb.initialise(descriptor);
                App.entityData.addEntity(App.entities.bomb);

                activeCount++;
                totalBombsUsed++;

                created = true;
            }
        }

        return created;
    }

    private boolean isValidPosition(int x)
    {
        return App.collisionUtils.getMarkerTileOn(x / Gfx.getTileWidth(), 1).get() != TileID._CRATER_TILE.get();

    }

    @Override
    public void free()
    {
        reset();
    }

    @Override
    public void reset()
    {
        activeCount = 0;
        spawnDelay.reset();
    }
}

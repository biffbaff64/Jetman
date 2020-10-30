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
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.Entities;
import com.richikin.jetman.entities.characters.Bomb;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.enumslib.GraphicID;
import com.richikin.jetman.maps.TileID;
import com.richikin.utilslib.logging.StopWatch;

import java.util.concurrent.TimeUnit;

public class BombManager extends GenericEntityManager
{
    private int       totalBombsUsed;
    private StopWatch spawnDelay;

    public BombManager(App _app)
    {
        super(GraphicID.G_BOMB, _app);
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
            && app.entityUtils.canUpdate(GraphicID.G_BOMB)
            && (activeCount == 0))
        {
            if (app.getRover() != null)
            {
                if (hasCreatedBomb())
                {
                    if (totalBombsUsed > 1)
                    {
                        app.getHud().messageManager.addZoomMessage("new_bomb", 3500);
                        app.getHud().messageManager.setPosition
                            (
                                "new_bomb",
                                185,
                                (720 - 167)
                            );
                    }
                }
            }
        }
    }

    private boolean hasCreatedBomb()
    {
        boolean created = false;

        if (app.entityUtils.canUpdate(GraphicID.G_BOMB))
        {
            int markerX = (int) (app.getRover().sprite.getX() / Gfx.getTileWidth());
            int offset  = (totalBombsUsed == 0) ? 0 : MathUtils.random(10, 30);

            if (MathUtils.random(100) < 50)
            {
                markerX += (app.getRover().frameWidth + (Gfx.getTileWidth() * 2)) / Gfx.getTileWidth();
                markerX += offset;
            }
            else
            {
                markerX -= (Gfx.getTileWidth() * 3) / Gfx.getTileWidth();
                markerX -= offset;
            }

            if (isValidPosition(markerX))
            {
                descriptor             = Entities.getDescriptor(GraphicID.G_BOMB);
                descriptor._SIZE       = GameAssets.getAssetSize(GraphicID.G_BOMB);
                descriptor._POSITION.x = markerX;
                descriptor._POSITION.y = (int) (app.getRover().getPosition().y / Gfx.getTileHeight());
                descriptor._POSITION.z = app.entityUtils.getInitialZPosition(GraphicID.G_BOMB);
                descriptor._INDEX      = app.entityData.entityMap.size;

                Bomb bomb = new Bomb(app);
                bomb.initialise(descriptor);
                app.entityData.addEntity(bomb);
                app.entityManager._bombIndex = bomb.spriteNumber;

                activeCount++;
                totalBombsUsed++;

                created = true;
            }
        }

        return created;
    }

    private boolean isValidPosition(int x)
    {
        return app.collisionUtils.getMarkerTileOn(x / Gfx.getTileWidth(), 1).get() != TileID._CRATER_TILE.get();

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

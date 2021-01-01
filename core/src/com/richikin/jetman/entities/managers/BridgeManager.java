package com.richikin.jetman.entities.managers;

import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileSet;
import com.richikin.jetman.core.App;

public class BridgeManager
{
    private final int[][] bridge =
        {
            {17, 18,},
            {21, 22,},
        };

    public void layBridge(int x, int y)
    {
        TiledMapTileSet floorTileSet = App.mapData.currentMap.getTileSets().getTileSet("items");

        TiledMapTileLayer.Cell cell;

        //
        // Modify the map by adding bridge graphics
        for (int row=0; row<2; row++)
        {
            for (int column=0; column<bridge[row].length; column++)
            {
                cell = new TiledMapTileLayer.Cell();
                cell.setTile(floorTileSet.getTile(bridge[row][column] + 100));
                App.mapData.extraGameTilesLayer.setCell(x + column, y - row, cell);

                //
                // Remove the _CRATER markertile
//                App.mapData.markerTilesLayer.setCell(x + column, y - row, null);
//                App.mapData.markerTilesLayer.setCell(x + column, (y - (row + 1)), null);
            }
        }
    }
}

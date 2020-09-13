
package com.richikin.jetman.entities;

import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.entities.objects.EntityDef;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.maps.TileID;

public abstract class Entities
{
    public static final EntityDef[] entityList =
        {
            // Main Characters
            new EntityDef("Player", GraphicID.G_PLAYER, TileID._PLAYER_TILE, GameAssets._PLAYER_IDLE, GameAssets._PLAYER_STAND_FRAMES, GraphicID._MAIN),
            new EntityDef("Rover", GraphicID.G_ROVER, TileID._ROVER_TILE, GameAssets._ROVER_ASSET, GameAssets._ROVER_FRAMES, GraphicID._MAIN),

            // Pickups

            // Decorations
            new EntityDef("Crater", GraphicID.G_CRATER, TileID._TRANSPORTER_TILE, GameAssets._TRANSPORTER_ASSET, GameAssets._TRANSPORTER_FRAMES, GraphicID._INTERACTIVE),

            // Interactive
            new EntityDef("Teleporter", GraphicID.G_TRANSPORTER, TileID._CRATER_TILE, GameAssets._CRATER_ASSET, GameAssets._CRATER_FRAMES, GraphicID._DECORATION),

            // Stationary Enemies
            new EntityDef("Base", GraphicID.G_MISSILE_BASE, TileID._MISSILE_BASE_TILE, GameAssets._MISSILE_BASE_ASSET, GameAssets._MISSILE_BASE_FRAMES, GraphicID._ENEMY),

            // Mobile Enemies

            // Miscellaneous Enemy Related
        };

    public static int getEntityDefIndex(GraphicID _gid)
    {
        int index = 0;
        int defsIndex = 0;

        for (EntityDef def : entityList)
        {
            if (def.graphicID == _gid)
            {
                defsIndex = index;
            }

            index++;
        }

        return defsIndex;
    }

    public static EntityDef getEntityDef(GraphicID _gid)
    {
        return entityList[getEntityDefIndex(_gid)];
    }
}

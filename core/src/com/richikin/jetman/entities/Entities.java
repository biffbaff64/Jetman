
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
            new EntityDef("Player", GraphicID.G_PLAYER, TileID._PLAYER_TILE, GameAssets._IDLE, GameAssets._PLAYER_STAND_FRAMES, GraphicID._MAIN),

            // Pickups

            // Decorations

            // Interactive

            // Stationary Enemies

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

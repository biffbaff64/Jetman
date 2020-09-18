
package com.richikin.jetman.entities;

import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.maps.TileID;

public abstract class Entities
{
    public static final SpriteDescriptor[] entityList =
        {
            // Main Characters
            new SpriteDescriptor
                (
                    "Player",
                    GraphicID.G_PLAYER, GraphicID._MAIN,
                    GameAssets._PLAYER_IDLE, GameAssets._PLAYER_STAND_FRAMES,
                    TileID._PLAYER_TILE
                ),
            new SpriteDescriptor
                (
                    "Rover",
                    GraphicID.G_ROVER, GraphicID._MAIN,
                    GameAssets._ROVER_IDLE_ASSET, GameAssets._ROVER_FRAMES,
                    TileID._ROVER_TILE
                ),
            new SpriteDescriptor
                (
                    "Rover Gun",
                    GraphicID.G_ROVER_GUN, GraphicID._MAIN,
                    GameAssets._ROVER_GUN_ASSET, GameAssets._ROVER_GUN_FRAMES,
                    TileID._ROVER_GUN_TILE
                ),

            // Pickups
            new SpriteDescriptor
                (
                    "Bomb",
                    GraphicID.G_BOMB, GraphicID._INTERACTIVE,
                    GameAssets._BOMB_ASSET, GameAssets._BOMB_FRAMES,
                    TileID._BOMB_TILE
                ),

            // Decorations

            // Interactive
            new SpriteDescriptor
                (
                    "Teleporter",
                    GraphicID.G_TRANSPORTER, GraphicID._INTERACTIVE,
                    GameAssets._TRANSPORTER_ASSET, GameAssets._TRANSPORTER_FRAMES,
                    TileID._TRANSPORTER_TILE
                ),

            // Stationary Enemies
            new SpriteDescriptor
                (
                    "Base",
                    GraphicID.G_MISSILE_BASE, GraphicID._ENEMY,
                    GameAssets._MISSILE_BASE_ASSET, GameAssets._MISSILE_BASE_FRAMES,
                    TileID._MISSILE_BASE_TILE
                ),

            // Mobile Enemies

            // Miscellaneous Enemy Related
        };

    public static int getDescriptorIndex(GraphicID _gid)
    {
        int index = 0;
        int defsIndex = 0;

        for (SpriteDescriptor descriptor : entityList)
        {
            if (descriptor._GID == _gid)
            {
                defsIndex = index;
            }

            index++;
        }

        return defsIndex;
    }

    public static SpriteDescriptor getDescriptor(GraphicID _gid)
    {
        return entityList[getDescriptorIndex(_gid)];
    }
}

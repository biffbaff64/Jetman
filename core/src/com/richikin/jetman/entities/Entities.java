
package com.richikin.jetman.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
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
                    Animation.PlayMode.LOOP,
                    TileID._PLAYER_TILE
                ),
            new SpriteDescriptor
                (
                    "Rover",
                    GraphicID.G_ROVER, GraphicID._MAIN,
                    GameAssets._ROVER_IDLE_ASSET, GameAssets._ROVER_FRAMES,
                    Animation.PlayMode.LOOP,
                    TileID._ROVER_TILE
                ),
            new SpriteDescriptor
                (
                    "Rover Gun",
                    GraphicID.G_ROVER_GUN, GraphicID._MAIN,
                    GameAssets._ROVER_GUN_ASSET, GameAssets._ROVER_GUN_FRAMES,
                    Animation.PlayMode.NORMAL,
                    TileID._ROVER_GUN_TILE
                ),
            new SpriteDescriptor
                (
                    "Rover Wheel",
                    GraphicID.G_ROVER_WHEEL, GraphicID._MAIN,
                    GameAssets._ROVER_WHEEL_ASSET, GameAssets._ROVER_WHEEL_FRAMES,
                    Animation.PlayMode.NORMAL,
                    TileID._ROVER_WHEEL_TILE
                ),
            new SpriteDescriptor
                (
                    "Rover Boot",
                    GraphicID.G_ROVER_BOOT, GraphicID._MAIN,
                    GameAssets._ROVER_BOOT_ASSET, GameAssets._ROVER_BOOT_FRAMES,
                    Animation.PlayMode.NORMAL,
                    TileID._ROVER_BOOT_TILE
                ),

            // Pickups
            new SpriteDescriptor
                (
                    "Bomb",
                    GraphicID.G_BOMB, GraphicID._INTERACTIVE,
                    GameAssets._BOMB_ASSET, GameAssets._BOMB_FRAMES,
                    Animation.PlayMode.LOOP,
                    TileID._BOMB_TILE
                ),

            // Decorations

            // Interactive
            new SpriteDescriptor
                (
                    "Teleporter",
                    GraphicID.G_TRANSPORTER, GraphicID._INTERACTIVE,
                    GameAssets._TRANSPORTER_ASSET, GameAssets._TRANSPORTER_FRAMES,
                    Animation.PlayMode.LOOP,
                    TileID._TRANSPORTER_TILE
                ),

            // Stationary Enemies
            new SpriteDescriptor
                (
                    "Missile Base",
                    GraphicID.G_MISSILE_BASE, GraphicID._ENEMY,
                    GameAssets._MISSILE_BASE_ASSET, GameAssets._MISSILE_BASE_FRAMES,
                    Animation.PlayMode.NORMAL,
                    TileID._MISSILE_BASE_TILE
                ),
            new SpriteDescriptor
                (
                    "Missile Launcher",
                    GraphicID.G_MISSILE_LAUNCHER, GraphicID._ENEMY,
                    GameAssets._MISSILE_LAUNCHER_ASSET, GameAssets._MISSILE_LAUNCHER_FRAMES,
                    Animation.PlayMode.LOOP,
                    TileID._MISSILE_LAUNCHER_TILE
                ),
            new SpriteDescriptor
                (
                    "Base Defender",
                    GraphicID.G_DEFENDER, GraphicID._ENEMY,
                    GameAssets._DEFENDER_ASSET, GameAssets._DEFENDER_FRAMES,
                    Animation.PlayMode.LOOP,
                    TileID._DEFENDER_TILE
                ),
            new SpriteDescriptor
                (
                    "Defender Bullet",
                    GraphicID.G_DEFENDER_BULLET, GraphicID._ENEMY,
                    GameAssets._DEFENDER_BULLET_ASSET, GameAssets._DEFENDER_BULLET_FRAMES,
                    Animation.PlayMode.LOOP,
                    TileID._DEFENDER_BULLET_TILE
                ),
            new SpriteDescriptor
                (
                    "Defender Zap",
                    GraphicID.G_DEFENDER_ZAP, GraphicID._ENEMY,
                    GameAssets._DEFENDER_ZAP_ASSET, GameAssets._DEFENDER_ZAP_FRAMES,
                    Animation.PlayMode.LOOP,
                    TileID._DEFENDER_ZAP_TILE
                ),

            // Mobile Enemies

            // Miscellaneous Enemy Related

            // Background Sprites
            new SpriteDescriptor
                (
                    "Background Ufo",
                    GraphicID.G_BACKGROUND_UFO, GraphicID._BACKGROUND_ENTITY,
                    GameAssets._BACKGROUND_UFO_ASSET, GameAssets._BACKGROUND_UFO_FRAMES,
                    Animation.PlayMode.LOOP,
                    TileID._BACKGROUND_UFO_TILE
                ),

            new SpriteDescriptor
                (
                    "Twinkle Star",
                    GraphicID.G_TWINKLE_STAR, GraphicID._BACKGROUND_ENTITY,
                    GameAssets._TWINKLE_STAR1_ASSET, GameAssets._TWINKLE_STAR_FRAMES,
                    Animation.PlayMode.LOOP,
                    TileID._TWINKLE_STAR_TILE
                ),
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

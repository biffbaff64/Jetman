
package com.richikin.jetman.characters;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.richikin.enumslib.GraphicID;
import com.richikin.enumslib.TileID;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.core.App;
import com.richikin.jetman.characters.misc.Bomb;
import com.richikin.jetman.characters.misc.MissileBase;
import com.richikin.jetman.characters.misc.Rover;
import com.richikin.jetman.characters.misc.RoverGun;
import com.richikin.jetman.characters.misc.Teleporter;
import com.richikin.jetman.characters.hero.MainPlayer;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.utilslib.logging.Trace;

public class Entities
{
    public MainPlayer   mainPlayer;
    public Rover        rover;
    public RoverGun     roverGun;
    public Bomb         bomb;
    public MissileBase  missileBase;
    public Teleporter[] teleporters;

    public final SpriteDescriptor[] entityList =
        {
            // Main Characters
            new SpriteDescriptor
                (
                    "Player",
                    GraphicID.G_PLAYER, GraphicID._MAIN,
                    GameAssets._PLAYER_IDLE_ASSET, GameAssets._PLAYER_STAND_FRAMES,
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
                    // Frame 0
                    "Rover Gun",
                    GraphicID.G_ROVER_GUN, GraphicID._MAIN,
                    GameAssets._ROVER_GUN_ASSET, GameAssets._ROVER_GUN_FRAMES,
                    Animation.PlayMode.NORMAL,
                    TileID._ROVER_GUN_TILE
                ),
            new SpriteDescriptor
                (
                    // Frame 1
                    "Gun Barrel",
                    GraphicID.G_ROVER_GUN_BARREL, GraphicID._MAIN,
                    GameAssets._ROVER_GUN_BARREL_ASSET, GameAssets._ROVER_GUN_BARREL_FRAMES,
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
            new SpriteDescriptor
                (
                    "Rover Bullet",
                    GraphicID.G_ROVER_BULLET, GraphicID._MAIN,
                    GameAssets._DEFENDER_BULLET_ASSET, GameAssets._DEFENDER_BULLET_FRAMES,
                    Animation.PlayMode.LOOP,
                    TileID._ROVER_BULLET_TILE
                ),

            // Lasers, Bullets, etc
            new SpriteDescriptor
                (
                    "Laser",
                    GraphicID.G_LASER, GraphicID._INTERACTIVE,
                    GameAssets._LASER_ASSET, GameAssets._LASER_FRAMES,
                    Animation.PlayMode.LOOP,
                    TileID._LASER_TILE
                ),
            new SpriteDescriptor
                (
                    "UFOBullet",
                    GraphicID.G_UFO_BULLET, GraphicID._INTERACTIVE,
                    GameAssets._UFO_BULLET_ASSET, GameAssets._UFO_BULLET_FRAMES,
                    Animation.PlayMode.LOOP,
                    TileID._UFO_BULLET_TILE
                ),
            new SpriteDescriptor
                (
                    "Explosion 12",
                    GraphicID.G_EXPLOSION12, GraphicID._DECORATION,
                    GameAssets._EXPLOSION64_ASSET, GameAssets._EXPLOSION64_FRAMES,
                    Animation.PlayMode.NORMAL,
                    TileID._EXPLOSION_TILE
                ),
            new SpriteDescriptor
                (
                    "Explosion 64",
                    GraphicID.G_EXPLOSION64, GraphicID._DECORATION,
                    GameAssets._EXPLOSION64_ASSET, GameAssets._EXPLOSION64_FRAMES,
                    Animation.PlayMode.NORMAL,
                    TileID._EXPLOSION_TILE
                ),
            new SpriteDescriptor
                (
                    "Explosion 128",
                    GraphicID.G_EXPLOSION128, GraphicID._DECORATION,
                    GameAssets._EXPLOSION64_ASSET, GameAssets._EXPLOSION64_FRAMES,
                    Animation.PlayMode.NORMAL,
                    TileID._EXPLOSION_TILE
                ),
            new SpriteDescriptor
                (
                    "Explosion 256",
                    GraphicID.G_EXPLOSION256, GraphicID._DECORATION,
                    GameAssets._EXPLOSION64_ASSET, GameAssets._EXPLOSION64_FRAMES,
                    Animation.PlayMode.NORMAL,
                    TileID._EXPLOSION_TILE
                ),

            // Pickups
            new SpriteDescriptor
                (
                    "Bomb",
                    GraphicID.G_BOMB, GraphicID._WEAPON,
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
            new SpriteDescriptor
                (
                    "Laser Barrier",
                    GraphicID.G_POWER_BEAM, GraphicID._ENEMY,
                    GameAssets._POWER_BEAM_ASSET, GameAssets._POWER_BEAM_FRAMES,
                    Animation.PlayMode.LOOP,
                    TileID._BEAM_TILE
                ),

            // Mobile Enemies
            new SpriteDescriptor
                (
                    "3BallsUfo",
                    GraphicID.G_3BALLS_UFO, GraphicID._ENEMY,
                    GameAssets._3BALLS_UFO_ASSET, GameAssets._3BALLS_UFO_FRAMES,
                    Animation.PlayMode.LOOP,
                    TileID._3BALLS_UFO_TILE
                ),
            new SpriteDescriptor
                (
                    "3LegsAlien",
                    GraphicID.G_3LEGS_ALIEN, GraphicID._ENEMY,
                    GameAssets._3LEGS_ALIEN_ASSET, GameAssets._3LEGS_ALIEN_FRAMES,
                    Animation.PlayMode.LOOP,
                    TileID._3LEGS_ALIEN_TILE
                ),
            new SpriteDescriptor
                (
                    "Asteroid",
                    GraphicID.G_ASTEROID, GraphicID._ENEMY,
                    GameAssets._ASTEROID1_ASSET, GameAssets._ASTEROID_FRAMES,
                    Animation.PlayMode.LOOP,
                    TileID._ASTEROID_TILE
                ),
            new SpriteDescriptor
                (
                    "AlienWheel",
                    GraphicID.G_ALIEN_WHEEL, GraphicID._ENEMY,
                    GameAssets._ALIEN_WHEEL_ASSET, GameAssets._ALIEN_WHEEL_FRAMES,
                    Animation.PlayMode.LOOP,
                    TileID._ALIEN_WHEEL_TILE
                ),
            new SpriteDescriptor
                (
                    "Blob",
                    GraphicID.G_BLOB, GraphicID._ENEMY,
                    GameAssets._BLOB_ASSET, GameAssets._BLOB_FRAMES,
                    Animation.PlayMode.LOOP,
                    TileID._BLOB_TILE
                ),
            new SpriteDescriptor
                (
                    "Dog",
                    GraphicID.G_DOG, GraphicID._ENEMY,
                    GameAssets._DOG_ASSET, GameAssets._DOG_FRAMES,
                    Animation.PlayMode.LOOP,
                    TileID._DOG_TILE
                ),
            new SpriteDescriptor
                (
                    "GreenBlock",
                    GraphicID.G_GREEN_BLOCK, GraphicID._ENEMY,
                    GameAssets._GREEN_BLOCK_ASSET, GameAssets._GREEN_BLOCK_FRAMES,
                    Animation.PlayMode.LOOP,
                    TileID._GREEN_BLOCK_TILE
                ),
            new SpriteDescriptor
                (
                    "StairClimber",
                    GraphicID.G_STAIR_CLIMBER, GraphicID._ENEMY,
                    GameAssets._STAIRCLIMBER_ASSET, GameAssets._STAIRCLIMBER_FRAMES,
                    Animation.PlayMode.LOOP,
                    TileID._STAIRCLIMBER_TILE
                ),
            new SpriteDescriptor
                (
                    "StarSpinner",
                    GraphicID.G_STAR_SPINNER, GraphicID._ENEMY,
                    GameAssets._STAR_SPINNER_ASSET, GameAssets._STAR_SPINNER_FRAMES,
                    Animation.PlayMode.LOOP,
                    TileID._STAR_SPINNER_TILE
                ),
            new SpriteDescriptor
                (
                    "SpinningBall",
                    GraphicID.G_SPINNING_BALL, GraphicID._ENEMY,
                    GameAssets._SPINNING_BALL_ASSET, GameAssets._SPINNING_BALL_FRAMES,
                    Animation.PlayMode.LOOP,
                    TileID._SPINNING_BALL_TILE
                ),
            new SpriteDescriptor
                (
                    "TopSpin",
                    GraphicID.G_TOPSPIN, GraphicID._ENEMY,
                    GameAssets._TOPSPIN_ASSET, GameAssets._TOPSPIN_FRAMES,
                    Animation.PlayMode.LOOP,
                    TileID._TOPSPIN_TILE
                ),
            new SpriteDescriptor
                (
                    "Twinkles",
                    GraphicID.G_TWINKLES, GraphicID._ENEMY,
                    GameAssets._TWINKLES_ASSET, GameAssets._TWINKLES_FRAMES,
                    Animation.PlayMode.LOOP,
                    TileID._TWINKLES_TILE
                ),
            new SpriteDescriptor
                (
                    "Windmill",
                    GraphicID.G_WINDMILL, GraphicID._ENEMY,
                    GameAssets._WINDMILL_ASSET, GameAssets._WINDMILL_FRAMES,
                    Animation.PlayMode.LOOP,
                    TileID._WINDMILL_TILE
                ),

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

    public Entities()
    {
    }

    public int getDescriptorIndex(GraphicID gid)
    {
        int     index      = 0;
        int     defsIndex  = 0;
        boolean foundIndex = false;

        for (SpriteDescriptor descriptor : entityList)
        {
            if (descriptor._GID == gid)
            {
                defsIndex  = index;
                foundIndex = true;
            }

            index++;
        }

        if (!foundIndex)
        {
            Trace.megaDivider("INDEX FOR " + gid + " NOT FOUND!!!");
        }

        return defsIndex;
    }

    public SpriteDescriptor getDescriptor(GraphicID gid)
    {
        return entityList[getDescriptorIndex(gid)];
    }

    public void setAllEnemyStatuses()
    {
        boolean isEnemy;

        for (int i = 0; i < App.entityData.entityMap.size; i++)
        {
            switch (App.entityData.entityMap.get(i).gid)
            {
                case G_BACKGROUND_UFO:
                case G_TWINKLE_STAR:
                case G_PRIZE_BALLOON:
                case G_MESSAGE_BUBBLE:
                case G_ROVER_BULLET:
                case G_TRANSPORTER:
                case G_ROVER_GUN:
                case G_ROVER_GUN_BARREL:
                case G_ROVER:
                case G_ROVER_BOOT:
                case G_ROVER_WHEEL:
                case G_LASER:
                case G_BOMB:
                case G_PLAYER:
                case G_EXPLOSION12:
                case G_EXPLOSION64:
                case G_EXPLOSION128:
                case G_EXPLOSION256:
                {
                    isEnemy = false;
                }
                break;

                default:
                {
                    isEnemy = true;
                }
                break;
            }

            ((GdxSprite) App.entityData.entityMap.get(i)).isEnemy = isEnemy;
        }
    }
}

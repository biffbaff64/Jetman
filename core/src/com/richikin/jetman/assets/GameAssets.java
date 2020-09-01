
package com.richikin.jetman.assets;

import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.maths.SimpleVec2;

public class GameAssets
{
    //
    // Assets not loaded from atlases
    public static final String _TEMPLATE_BACKGROUND_ASSET = "data/template_background.png";

    //
    // MainPlayer assets
    public static final String _RUN                   = "ljm_walk2";
    public static final String _IDLE                  = "ljm_stand";
    public static final String _FLY                   = "ljm_fly2";
    public static final String _DYING                 = "ljm_death";

    public static final int    _PLAYER_STAND_FRAMES   = 5;
    public static final int    _PLAYER_DYING_FRAMES   = 1;
    public static final int    _PLAYER_FLY_FRAMES     = 3;
    public static final int    _PLAYER_RUN_FRAMES     = 12;

    //
    // Asset names for all game graphics
    public static final  String _BUTTON_PANEL_ASSET            = "button_panel";
    public static final  String _ASTEROID1_ASSET               = "asteroid01";
    public static final  String _ASTEROID2_ASSET               = "asteroid02_redglow";
    public static final  String _ASTEROID3_ASSET               = "asteroid03";
    public static final  String _ROVER_ASSET                   = "rover";
    public static final  String _ROVER_BOOT_ASSET              = "rover_boot";
    public static final  String _ROVER_GUN_ASSET               = "rover_gun_modified";
    public static final  String _ROVER_WHEEL_ASSET             = "rover_wheel";
    public static final  String _ALIEN_WHEEL_ASSET             = "wheel";
    public static final  String _BOMB_ASSET                    = "bomb";
    public static final  String _POWER_BEAM_ASSET              = "verticalblueflare";
    public static final  String _POWER_BEAM_SMALL_ASSET        = "verticalblueflare_small";
    public static final  String _MISSILE_BASE_ASSET            = "launcher_base";
    public static final  String _MISSILE_LAUNCHER_ASSET        = "launcher_top";
    public static final  String _MISSILE_ASSET                 = "rocket";
    public static final  String _TRANSPORTER_ASSET             = "transporter_v3";
    public static final  String _DEFENDER_ASSET                = "jetman_defence";
    public static final  String _DEFENDER_ZAP_ASSET            = "zap";
    public static final  String _3BALLS_ASSET                  = "3_balls";
    public static final  String _3BALLS_UFO_ASSET              = "3balls";
    public static final  String _3LEGS_ALIEN_ASSET             = "3legs";
    public static final  String _TOPSPIN_ASSET                 = "topspin";
    public static final  String _TWINKLES_ASSET                = "twinkles2";
    public static final  String _STAR_SPINNER_ASSET            = "starspinner";
    public static final  String _TWINKLE_STAR1_ASSET           = "twinkle_star1";
    public static final  String _TWINKLE_STAR2_ASSET           = "twinkle_star2";
    public static final  String _DOG_ASSET                     = "eyebitter";
    public static final  String _GREEN_BLOCK_ASSET             = "green_block";
    public static final  String _SPINNING_BALL_ASSET           = "jm_spinningball";
    public static final  String _BLOB_ASSET                    = "plus_blob";
    public static final  String _EXPLOSION64_ASSET             = "explosion64";
    public static final  String _SPARKLE_WEAPON_ASSET          = "defence_sparkle";
    public static final  String _LASER_ASSET                   = "player_laser";
    public static final  String _UFO_BULLET_ASSET              = "ufo_bullet";

    //
    // Frame counts for animations
    public static final  int    _ASTEROID_FRAMES               = 32;
    public static final  int    _ROVER_FRAMES                  = 2;
    public static final  int    _ROVER_BOOT_FRAMES             = 1;
    public static final  int    _ROVER_GUN_FRAMES              = 2;
    public static final  int    _ROVER_WHEEL_FRAMES            = 1;
    public static final  int    _ALIEN_WHEEL_FRAMES            = 32;
    public static final  int    _BOMB_FRAMES                   = 2;
    public static final  int    _POWER_BEAM_FRAMES             = 1;
    public static final  int    _MISSILE_BASE_FRAMES           = 1;
    public static final  int    _MISSILE_LAUNCHER_FRAMES       = 32;
    public static final  int    _MISSILE_FRAMES                = 2;
    public static final  int    _TRANSPORTER_FRAMES            = 21;
    public static final  int    _DEFENDER_FRAMES               = 10;
    public static final  int    _DEFENDER_ZAP_FRAMES           = 22;
    public static final  int    _3BALLS_FRAMES                 = 32;
    public static final  int    _3BALLS_UFO_FRAMES             = 32;
    public static final  int    _3LEGS_ALIEN_FRAMES            = 32;
    public static final  int    _TOPSPIN_FRAMES                = 32;
    public static final  int    _TWINKLES_FRAMES               = 32;
    public static final  int    _STAR_SPINNER_FRAMES           = 32;
    public static final  int    _TWINKLE_STAR_FRAMES           = 12;
    public static final  int    _DOG_FRAMES                    = 32;
    public static final  int    _GREEN_BLOCK_FRAMES            = 16;
    public static final  int    _SPINNING_BALL_FRAMES          = 32;
    public static final  int    _BLOB_FRAMES                   = 32;
    public static final  int    _EXPLOSION64_FRAMES            = 12;
    public static final  int    _SPARKLE_WEAPON_FRAMES         = 10;
    public static final  int    _LASER_FRAMES                  = 1;
    public static final  int    _UFO_BULLET_FRAMES             = 10;

    //
    // Fonts and HUD assets
    public static final String _BENZOIC_FONT           = "data/fonts/paraaminobenzoic.ttf";
    public static final String _ORBITRON_BOLD_FONT     = "data/fonts/Orbitron Bold.ttf";
    public static final String _PRO_WINDOWS_FONT       = "data/fonts/ProFontWindows.ttf";
    public static final String _HUD_PANEL_ASSET        = "data/hud_panel.png";
    public static final int    _HUD_PANEL_WIDTH        = 1280;
    public static final int    _HUD_PANEL_HEIGHT       = 112;

    public static final String _GETREADY_MSG_ASSET        = "get_ready";
    public static final String _GAMEOVER_MSG_ASSET        = "game_over";

    private static final AssetSize[] assetSizes =
        {
            new AssetSize(GraphicID.G_MISSILE_LAUNCHER, 90, 120),
            new AssetSize(GraphicID.G_ASTEROID,         60, 60),
            new AssetSize(GraphicID.G_ALIEN_WHEEL,      48, 48),
            new AssetSize(GraphicID.G_3BALLS_UFO,       48, 48),
            new AssetSize(GraphicID.G_3BALLS,           48, 48),
            new AssetSize(GraphicID.G_3LEGS_ALIEN,      48, 48),
            new AssetSize(GraphicID.G_BLOB,             48, 48),
            new AssetSize(GraphicID.G_TRANSPORTER,      96, 124),
            new AssetSize(GraphicID.G_GREEN_BLOCK,      72, 48),
            new AssetSize(GraphicID.G_DOG,              48, 48),
            new AssetSize(GraphicID.G_EXPLOSION12,      64, 64),
            new AssetSize(GraphicID.G_EXPLOSION64,      64, 64),
            new AssetSize(GraphicID.G_EXPLOSION128,     64, 64),
            new AssetSize(GraphicID.G_EXPLOSION256,     64, 64),
            new AssetSize(GraphicID.G_SPINNING_BALL,    48, 48),
            new AssetSize(GraphicID.G_STAR_SPINNER,     48, 48),
            new AssetSize(GraphicID.G_TOPSPIN,          48, 48),
            new AssetSize(GraphicID.G_DEFENDER_ZAP,     52, 23),
            new AssetSize(GraphicID.G_PLAYER,           80, 80),
            new AssetSize(GraphicID.G_TWINKLES,         48, 48),
            new AssetSize(GraphicID.G_POWER_BEAM,       64, 512),
            new AssetSize(GraphicID.G_POWER_BEAM_SMALL, 64, 128),
        };

    public static SimpleVec2 getAssetSize(GraphicID _gid)
    {
        SimpleVec2 size = new SimpleVec2();

        for (final AssetSize assetSize : assetSizes)
        {
            if (assetSize.graphicID == _gid)
            {
                size = assetSize.size;
            }
        }

        return size;
    }
}
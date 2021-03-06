
package com.richikin.jetman.assets;

import com.richikin.enumslib.GraphicID;
import com.richikin.utilslib.assets.AssetDescriptor;
import com.richikin.utilslib.assets.AssetSize;
import com.richikin.utilslib.maths.SimpleVec2;
import com.richikin.utilslib.logging.Trace;

public class GameAssets
{
    public static final AssetDescriptor[] assetList =
        {
        };

    //
    // MainPlayer assets
    public static final String _PLAYER_RUN_ASSET   = "ljm_walk2";
    public static final String _PLAYER_IDLE_ASSET  = "ljm_stand";
    public static final String _PLAYER_FLY_ASSET   = "ljm_fly2";
    public static final String _PLAYER_DYING_ASSET = "ljm_death";
    public static final String _PLAYER_SPAWN_ASSET = "player_appear";

    public static final int _PLAYER_STAND_FRAMES = 5;
    public static final int _PLAYER_DYING_FRAMES = 1;
    public static final int _PLAYER_FLY_FRAMES   = 3;
    public static final int _PLAYER_RUN_FRAMES   = 12;

    //
    // Asset names for all game graphics
    public static final String _3BALLS_UFO_ASSET       = "3balls";
    public static final String _3LEGS_ALIEN_ASSET      = "3legs";
    public static final String _ALIEN_WHEEL_ASSET      = "wheel";
    public static final String _ASTEROID1_ASSET        = "asteroid03";
    public static final String _BLOB_ASSET             = "plus_blob";
    public static final String _BOMB_ASSET             = "bomb";
    public static final String _DEFENDER_ASSET         = "jetman_defence";
    public static final String _DEFENDER_ZAP_ASSET     = "zap";
    public static final String _DOG_ASSET              = "eyebitter";
    public static final String _EXPLOSION64_ASSET      = "explosion64";
    public static final String _GREEN_BLOCK_ASSET      = "green_block";
    public static final String _LASER_ASSET            = "player_laser";
    public static final String _MISSILE_BASE_ASSET     = "launcher_base";
    public static final String _MISSILE_LAUNCHER_ASSET = "launcher_top";
    public static final String _MISSILE_ASSET          = "rocket";
    public static final String _POWER_BEAM_ASSET       = "powerbeam";
    public static final String _ROVER_WHEEL_ASSET      = "rover_wheel";
    public static final String _ROVER_IDLE_ASSET       = "rover_idle";
    public static final String _ROVER_BOOT_ASSET       = "rover_boot";
    public static final String _ROVER_GUN_ASSET        = "rover_gun";
    public static final String _ROVER_GUN_BARREL_ASSET = "rover_gun_barrel";
    public static final String _STAIRCLIMBER_ASSET     = "3_balls";
    public static final String _STAR_SPINNER_ASSET     = "starspinner";
    public static final String _SPINNING_BALL_ASSET    = "jm_spinningball";
    public static final String _DEFENDER_BULLET_ASSET  = "defence_sparkle";
    public static final String _TRANSPORTER_ASSET      = "transporter_v3";
    public static final String _TRANSPORTER_BEAM_ASSET = "transporter_beam";
    public static final String _TOPSPIN_ASSET          = "topspin";
    public static final String _TWINKLES_ASSET         = "twinkles2";
    public static final String _UFO_BULLET_ASSET       = "ufo_bullet";
    public static final String _WINDMILL_ASSET         = "plus_blob";

    //
    // Frame counts for animations
    public static final int _3BALLS_UFO_FRAMES       = 32;
    public static final int _3LEGS_ALIEN_FRAMES      = 32;
    public static final int _ASTEROID_FRAMES         = 32;
    public static final int _ALIEN_WHEEL_FRAMES      = 32;
    public static final int _BOMB_FRAMES             = 2;
    public static final int _BLOB_FRAMES             = 32;
    public static final int _DEFENDER_FRAMES         = 10;
    public static final int _DEFENDER_ZAP_FRAMES     = 22;
    public static final int _DOG_FRAMES              = 32;
    public static final int _EXPLOSION64_FRAMES      = 12;
    public static final int _GREEN_BLOCK_FRAMES      = 16;
    public static final int _LASER_FRAMES            = 1;
    public static final int _MISSILE_BASE_FRAMES     = 1;
    public static final int _MISSILE_LAUNCHER_FRAMES = 32;
    public static final int _MISSILE_FRAMES          = 2;
    public static final int _POWER_BEAM_FRAMES       = 2;
    public static final int _ROVER_FRAMES            = 4;
    public static final int _ROVER_BOOT_FRAMES       = 1;
    public static final int _ROVER_GUN_FRAMES        = 1;
    public static final int _ROVER_GUN_BARREL_FRAMES = 1;
    public static final int _ROVER_WHEEL_FRAMES      = 1;
    public static final int _STAIRCLIMBER_FRAMES     = 32;
    public static final int _STAR_SPINNER_FRAMES     = 32;
    public static final int _SPINNING_BALL_FRAMES    = 32;
    public static final int _DEFENDER_BULLET_FRAMES  = 10;
    public static final int _TRANSPORTER_FRAMES      = 21;
    public static final int _TOPSPIN_FRAMES          = 32;
    public static final int _TWINKLES_FRAMES         = 32;
    public static final int _UFO_BULLET_FRAMES       = 10;
    public static final int _WINDMILL_FRAMES         = 32;

    //
    // Background sprites
    public static final String _BACKGROUND_UFO_ASSET  = "background_ufo";
    public static final String _TWINKLE_STAR1_ASSET   = "twinkle_star1";
    public static final String _TWINKLE_STAR2_ASSET   = "twinkle_star2";
    public static final int    _BACKGROUND_UFO_FRAMES = 10;
    public static final int    _TWINKLE_STAR_FRAMES   = 12;

    //
    // Fonts and HUD assets
    public static final String _BENZOIC_FONT        = "data/fonts/paraaminobenzoic.ttf";
    public static final String _GALAXY_FONT         = "data/fonts/galaxy.ttf";
    public static final String _PRO_WINDOWS_FONT    = "data/fonts/ProFontWindows.ttf";
    public static final String _VIDEO_PHREAK_FONT   = "data/fonts/ProFontWindows.ttf";
    public static final String _HUD_PANEL_ASSET     = "data/hud_panel.png";
    public static final String _SPLASH_SCREEN_ASSET = "data/splash_screen.png";

    public static final String _GETREADY_MSG_ASSET  = "getready";
    public static final String _GAMEOVER_MSG_ASSET  = "gameover";
    public static final String _CREDITS_PANEL_ASSET = "data/credits_panel.png";
    public static final String _OPTIONS_PANEL_ASSET = "data/options_panel.png";
    public static final String _PAUSE_PANEL_ASSET   = "data/pause_panel.png";
    public static final String _UISKIN_ASSET        = "data/uiskin.json";

    public static int hudPanelWidth;      // Set when object is loaded
    public static int hudPanelHeight;     //

    private static final AssetSize[] assetSizes =
        {
            new AssetSize(GraphicID.G_PLAYER, 80, 80),
            new AssetSize(GraphicID.G_ROVER, 303, 131),
            new AssetSize(GraphicID.G_ROVER_WHEEL, 68, 68),
            new AssetSize(GraphicID.G_ROVER_BOOT, 108, 14),
            new AssetSize(GraphicID.G_ROVER_GUN, 122, 59),
            new AssetSize(GraphicID.G_ROVER_GUN_BARREL, 122, 59),
            new AssetSize(GraphicID.G_BOMB, 42, 53),
            new AssetSize(GraphicID.G_ROVER_BULLET, 36, 36),

            new AssetSize(GraphicID.G_MISSILE_BASE, 120, 51),
            new AssetSize(GraphicID.G_MISSILE_LAUNCHER, 90, 120),
            new AssetSize(GraphicID.G_MISSILE, 120, 29),
            new AssetSize(GraphicID.G_DEFENDER, 52, 48),
            new AssetSize(GraphicID.G_DEFENDER_ZAP, 52, 23),
            new AssetSize(GraphicID.G_DEFENDER_BULLET, 36, 36),

            new AssetSize(GraphicID.G_TRANSPORTER, 96, 124),
            new AssetSize(GraphicID.G_TRANSPORTER_BEAM, 64, 512),
            new AssetSize(GraphicID.G_LASER, 324, 8),
            new AssetSize(GraphicID.G_UFO_BULLET, 9, 9),

            new AssetSize(GraphicID.G_ASTEROID, 60, 60),
            new AssetSize(GraphicID.G_ALIEN_WHEEL, 68, 68),
            new AssetSize(GraphicID.G_3BALLS_UFO, 48, 48),
            new AssetSize(GraphicID.G_STAIR_CLIMBER, 96, 96),
            new AssetSize(GraphicID.G_3LEGS_ALIEN, 112, 112),
            new AssetSize(GraphicID.G_BLOB, 48, 48),
            new AssetSize(GraphicID.G_GREEN_BLOCK, 72, 48),
            new AssetSize(GraphicID.G_DOG, 64, 64),
            new AssetSize(GraphicID.G_SPINNING_BALL, 80, 80),
            new AssetSize(GraphicID.G_STAR_SPINNER, 68, 68),
            new AssetSize(GraphicID.G_TOPSPIN, 72, 72),
            new AssetSize(GraphicID.G_TWINKLES, 48, 48),
            new AssetSize(GraphicID.G_WINDMILL, 48, 48),

            new AssetSize(GraphicID.G_POWER_BEAM, 120, 563),

            new AssetSize(GraphicID.G_EXPLOSION12, 64, 64),
            new AssetSize(GraphicID.G_EXPLOSION64, 64, 64),
            new AssetSize(GraphicID.G_EXPLOSION128, 64, 64),
            new AssetSize(GraphicID.G_EXPLOSION256, 64, 64),

            new AssetSize(GraphicID.G_BACKGROUND_UFO, 8, 4),
            new AssetSize(GraphicID.G_TWINKLE_STAR, 46, 46),
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

        if (size.isEmpty())
        {
            Trace.__FILE_FUNC("***** Size for " + _gid + " not found! *****");
        }

        return size;
    }
}

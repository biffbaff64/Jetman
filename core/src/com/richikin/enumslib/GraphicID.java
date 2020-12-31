
package com.richikin.enumslib;

public enum GraphicID
{
    // ----------------------------
    // The Player
    G_PLAYER,

    G_ROVER,
    G_ROVER_BOOT,
    G_ROVER_GUN,
    G_ROVER_GUN_BARREL,
    G_ROVER_WHEEL,
    G_BOMB,
    G_LASER,
    G_ROVER_BULLET,

    G_SPAWNER,

    // ----------------------------
    G_ALIEN_BASE,
    G_MISSILE_BASE,
    G_MISSILE_LAUNCHER,
    G_MISSILE,

    // ----------------------------
    G_TRANSPORTER,
    G_TRANSPORTER_BEAM,
    G_DEFENDER,
    G_DEFENDER_ZAP,
    G_DEFENDER_BULLET,

    // ----------------------------
    G_POWER_BEAM,
    G_POWER_BEAM_SMALL,

    // ----------------------------
    G_PICKUP,
    G_FREEZER,
    G_ADD_TIME,

    // ----------------------------
    G_STAIR_CLIMBER,
    G_3BALLS_UFO,
    G_UFO_BULLET,
    G_3LEGS_ALIEN,
    G_TOPSPIN,
    G_TWINKLES,
    G_STAR_SPINNER,
    G_ASTEROID,
    G_DOG,
    G_GREEN_BLOCK,
    G_SPINNING_BALL,
    G_BLOB,
    G_ALIEN_WHEEL,
    G_WINDMILL,

    // ----------------------------
    G_TWINKLE_STAR,
    G_BACKGROUND_UFO,

    // ----------------------------
    G_EXPLOSION12,
    G_EXPLOSION64,
    G_EXPLOSION128,
    G_EXPLOSION256,
    G_PRIZE_BALLOON,
    G_MESSAGE_BUBBLE,
    G_ARROW,

    // ----------------------------
    // Generic
    _MONSTER,
    _BLOCKS,
    _GROUND,
    _CEILING,
    _WALL,
    _LETHAL_OBJECT,
    _SIGN,
    _SPEECH,
    _HUD_PANEL,
    _EXIT_BOX,
    _BACKGROUND_ENTITY,
    _BRIDGE,
    _CRATER,
    _ALIEN_MANAGER,

    // ----------------------------
    // Main Character type, i.e. Player
    _MAIN,

    // ----------------------------
    // Enemy Character type, but not stationary entities
    // like rocket launchers etc.
    _ENEMY,

    // ----------------------------
    // Encapsulating type, covering any collision IDs that can be stood on.
    // This will be checked against the collision object TYPE, not the NAME.
    _OBSTACLE,

    // ----------------------------
    // As above but for objects that can't be stood on and are not entities
    _DECORATION,

    // As above, but for entities
    _ENTITY,

    // ----------------------------
    // Interactive objects
    _PICKUP,
    _WEAPON,
    _INTERACTIVE,
    _PRISONER,
    _PLATFORM,

    // ----------------------------

    G_DUMMY,
    G_NO_ID
}

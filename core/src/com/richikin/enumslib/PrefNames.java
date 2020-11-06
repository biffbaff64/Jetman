package com.richikin.enumslib;

public enum PrefNames
{
    _DEFAULT_ON                 ("default on"),
    _DEFAULT_OFF                ("default off"),

    //
    // Development options
    _DEV_MODE                   ("dev mode"),           // Enables/Disables DEV Mode
    _GOD_MODE                   ("god mode"),           //
    _USING_ASHLEY_ECS           ("ashley ecs"),         // Enables use of Ashley Entity Component System
    _DISABLE_ENEMIES            ("disable enemies"),    // Disables all enemy entities
    _SCROLL_DEMO                ("scroll demo"),        // Enables Game Scroll Demo mode
    _SPRITE_BOXES               ("sprite boxes"),       // Shows sprite AABB Boxes
    _TILE_BOXES                 ("tile boxes"),         // Shows game tile AABB Boxes
    _BUTTON_BOXES               ("button boxes"),       // Shows GameButton bounding boxes
    _SHOW_FPS                   ("show fps"),           // Shows current FPS on-screen
    _SHOW_DEBUG                 ("show debug"),         // Enables on-screen debug printing
    _SPAWNPOINTS                ("spawn points"),       // Shows spawn point tiles from game map
    _MENU_HEAPS                 ("menu heaps"),         // Show Heap Sizes on Menu Page if true
    _DISABLE_MENU_SCREEN        ("disable menu"),       //
    _CULL_SPRITES               ("cull sprites"),       // Enables Sprite Culling when off screen
    _SHADER_PROGRAM             ("shader program"),     // Enables/Disables global shader program
    _BOX2D_PHYSICS              ("using box2d"),        // Enables Box2D Physics
    _B2D_RENDERER               ("b2d renderer"),       // Enables/Disables the Box2D Debug Renderer
    _GL_PROFILER                ("gl profiler"),        // Enables/Disables the LibGdx OpenGL Profiler
    _ANDROID_ON_DESKTOP         ("android desktop"),    //

    //
    // Game settings
    _INSTALLED                  ("installed"),          //
    _SHOW_HINTS                 ("show hints"),         // Enables/Disables In-Game Hints
    _VIBRATIONS                 ("vibrations"),         // Enables/Disables device vibrations
    _MUSIC_ENABLED              ("music enabled"),      // Enables/Disables Music
    _SOUNDS_ENABLED             ("sound enabled"),      // Enables/Disables Sound FX
    _FX_VOLUME                  ("fx volume"),          // Current Sound FX Volume
    _MUSIC_VOLUME               ("music volume"),       // Current Music Volume
    _PLAY_SERVICES              ("play services"),      // Enables Google Play Services
    _ACHIEVEMENTS               ("achievements"),       // Enables In-Game Achievements
    _CHALLENGES                 ("challenges"),         // Enables In-Game challenges
    _SIGN_IN_STATUS             ("sign in status"),     // Google Services sign in status (Android)

    _DUMMY                      ("dummy");

    final String id;

    PrefNames(String _id)
    {
        this.id = _id;
    }

    public String getID()
    {
        return id;
    }
}

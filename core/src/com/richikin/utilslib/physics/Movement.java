
package com.richikin.utilslib.physics;

public enum Movement
{
    ;
    public static final int _HORIZONTAL         = 1;
    public static final int _VERTICAL           = 2;
    public static final int _DIRECTION_IN       = 1;
    public static final int _DIRECTION_OUT      = -1;
    public static final int _FORWARDS           = 1;
    public static final int _BACKWARDS          = -1;
    public static final int _DIRECTION_RIGHT    = 1;
    public static final int _DIRECTION_LEFT     = -1;
    public static final int _DIRECTION_UP       = 1;
    public static final int _DIRECTION_DOWN     = -1;
    public static final int _DIRECTION_STILL    = 0;
    public static final int _DIRECTION_CUSTOM   = 2;

    private static final String[][] aliases =
        {
            {"LEFT ", "STILL", "RIGHT"},
            {"DOWN ", "STILL", "UP   "},
        };

    public static String getAliasX(int value)
    {
        return aliases[0][value+1];
    }

    public static String getAliasY(int value)
    {
        return aliases[1][value+1];
    }

    public static Dir translateDirection(Direction direction)
    {
        final DirectionValue[] translateTable =
            {
                new DirectionValue(_DIRECTION_STILL,    _DIRECTION_STILL,   Dir._STILL),
                new DirectionValue(_DIRECTION_LEFT,     _DIRECTION_STILL,   Dir._LEFT),
                new DirectionValue(_DIRECTION_RIGHT,    _DIRECTION_STILL,   Dir._RIGHT),
                new DirectionValue(_DIRECTION_STILL,    _DIRECTION_UP,      Dir._UP),
                new DirectionValue(_DIRECTION_STILL,    _DIRECTION_DOWN,    Dir._DOWN),
                new DirectionValue(_DIRECTION_LEFT,     _DIRECTION_UP,      Dir._UP_LEFT),
                new DirectionValue(_DIRECTION_RIGHT,    _DIRECTION_UP,      Dir._UP_RIGHT),
                new DirectionValue(_DIRECTION_LEFT,     _DIRECTION_DOWN,    Dir._DOWN_LEFT),
                new DirectionValue(_DIRECTION_RIGHT,    _DIRECTION_DOWN,    Dir._DOWN_RIGHT),
            };

        Dir translatedDir = Dir._STILL;

        for (DirectionValue directionValue : translateTable)
        {
            if ((directionValue.dirX == direction.getX()) && (directionValue.dirY == direction.getY()))
            {
                translatedDir = directionValue.translated;
            }
        }

        return translatedDir;
    }
}

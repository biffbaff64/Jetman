package com.richikin.jetman.core;

import com.badlogic.gdx.utils.Array;
import com.richikin.enumslib.GraphicID;
import com.richikin.utilslib.maths.NumberUtils;

public class PointsManager
{
    static class Points
    {
        final GraphicID gid;
        final int       points;

        Points(GraphicID _gid, int _points)
        {
            this.gid = _gid;
            this.points = _points;
        }
    }

    private static final Points[] pointsTable =
        {
            new Points(GraphicID.G_MISSILE_BASE,    5000),
            new Points(GraphicID.G_MISSILE,         500),
            new Points(GraphicID.G_SPINNING_BALL,   210),
            new Points(GraphicID.G_3BALLS_UFO,      170),
            new Points(GraphicID.G_TWINKLES,        170),
            new Points(GraphicID.G_ALIEN_WHEEL,     160),
            new Points(GraphicID.G_3BALLS_UFO,      160),
            new Points(GraphicID.G_GREEN_BLOCK,     120),
            new Points(GraphicID.G_STAR_SPINNER,    120),
            new Points(GraphicID.G_ASTEROID,        110),
            new Points(GraphicID.G_DOG,             100),
            new Points(GraphicID.G_BLOB,            100),
            new Points(GraphicID.G_TOPSPIN,         90),
            new Points(GraphicID.G_3LEGS_ALIEN,     90),
            };

    public static int getPoints(GraphicID gid)
    {
        int score = 0;

        for (final Points aPointsTable : pointsTable)
        {
            if (gid.equals(aPointsTable.gid))
            {
                score = aPointsTable.points;

                break;
            }
        }

        return score;
    }
}

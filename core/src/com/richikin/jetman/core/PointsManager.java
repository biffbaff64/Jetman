package com.richikin.jetman.core;

import com.badlogic.gdx.utils.Array;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.utilslib.maths.NumberUtils;

public class PointsManager
{
    static class Points
    {
        final GraphicID gid;
        final int       value;
        int total;
        int pending;

        Points(GraphicID _gid, int _points)
        {
            this.gid     = _gid;
            this.value   = _points;
            this.total   = 0;
            this.pending = 0;
        }
    }

    private static Array<Points> pointsTable = new Array<>();

    public static void updatePointStacks()
    {
        for (Points entry : pointsTable)
        {
            if (entry.pending > 0)
            {
                int amount = NumberUtils.getCount(entry.pending);

                entry.total += amount;
                entry.pending -= amount;
            }
        }
    }

    public static void addItem(GraphicID _gid, int _value)
    {
        pointsTable.add(new Points(_gid, _value));
    }

    public static void addPending(GraphicID _gid, int _value)
    {
        pointsTable.get(getIndex(_gid)).pending += _value;
    }

    public static int getPoints(GraphicID _gid)
    {
        int points = 0;

        if (pointsTable.size > getIndex(_gid))
        {
            points = pointsTable.get(getIndex(_gid)).total;
        }

        return points;
    }

    private static int getIndex(GraphicID _gid)
    {
        int index = 0;

        for (final Points entry : pointsTable)
        {
            if (_gid.equals(entry.gid))
            {
                break;
            }
        }

        return index;
    }
}

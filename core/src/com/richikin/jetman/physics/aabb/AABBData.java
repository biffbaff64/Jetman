package com.richikin.jetman.physics.aabb;

import com.badlogic.gdx.utils.Array;

public class AABBData
{
    public static final short _TOP       = 0x01;
    public static final short _BOTTOM    = 0x02;
    public static final short _LEFT      = 0x04;
    public static final short _RIGHT     = 0x08;

    private static Array<CollisionObject> collisionBoxData;

    public static void createData()
    {
        collisionBoxData = new Array<>();
    }

    public static Array<CollisionObject> boxes()
    {
        return collisionBoxData;
    }

    public static void initialise()
    {
    }

    public static void add(CollisionObject _object)
    {
        collisionBoxData.add(_object);
    }

    public static void remove(int _index)
    {
        collisionBoxData.removeIndex(_index);
        rescan();
    }

    /**
     * Rescan the collision object list, re-ordering indexes
     * after an object has been removed.
     */
    public static void rescan()
    {
        for (int i = 0; i < collisionBoxData.size; i++)
        {
            collisionBoxData.get(i).index = i;
        }
    }
}
package com.richikin.jetman.entities.managers;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.entities.components.EntityManagerComponent;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.utilslib.maths.SimpleVec2;

public class GenericEntityManager implements EntityManagerComponent, Disposable
{
    public       boolean          canPlace;
    public final App              app;
    public       SpriteDescriptor descriptor;
    public       int              activeCount;
    public       GraphicID        graphicID;

    private final GraphicID managerID;

    public GenericEntityManager(final App _app)
    {
        this.app = _app;

        this.graphicID    = GraphicID.G_NO_ID;
        this.managerID    = GraphicID.G_NO_ID;
    }

    public GenericEntityManager(final GraphicID _graphicID, final App _app)
    {
        this.app = _app;

        this.graphicID    = _graphicID;
        this.managerID    = _graphicID;
    }

    @Override
    public void init()
    {
        activeCount = 0;
    }

    @Override
    public void update()
    {
    }

    @Override
    public void create()
    {
    }

    @Override
    public SimpleVec2 findCoordinates(final GraphicID targetGID)
    {
        SimpleVec2 coords = new SimpleVec2();

        for (SpriteDescriptor marker : app.mapData.placementTiles)
        {
            if (marker._GID == targetGID)
            {
                coords.set(marker._POSITION.x, marker._POSITION.y);
            }
        }

        return coords;
    }

    @Override
    public Array<SimpleVec2> findMultiCoordinates(final GraphicID targetGID)
    {
        Array<SimpleVec2> coords = new Array<>();

        for (SpriteDescriptor marker : app.mapData.placementTiles)
        {
            if (marker._GID == targetGID)
            {
                coords.add(new SimpleVec2(marker._POSITION.x, marker._POSITION.y));
            }
        }

        return coords;
    }

    @Override
    public void free()
    {
        activeCount = Math.max(0, activeCount - 1);
    }

    @Override
    public void reset()
    {
        activeCount = 0;
    }

    @Override
    public int getActiveCount()
    {
        return activeCount;
    }

    @Override
    public void setActiveCount(final int numActive)
    {
        activeCount = numActive;
    }

    @Override
    public GraphicID getGID()
    {
        return managerID;
    }

    @Override
    public boolean isPlaceable()
    {
        return canPlace;
    }

    @Override
    public void setPlaceable(final boolean placeable)
    {
        canPlace = placeable;
    }

    @Override
    public void dispose()
    {
        descriptor = null;
    }
}

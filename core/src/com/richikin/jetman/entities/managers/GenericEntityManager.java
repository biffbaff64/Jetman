package com.richikin.jetman.entities.managers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.components.EntityManagerComponent;
import com.richikin.jetman.entities.objects.EntityDescriptor;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.maps.MarkerTile;
import com.richikin.jetman.maths.SimpleVec2;
import com.sun.prism.image.Coords;

public class GenericEntityManager implements EntityManagerComponent, Disposable
{
    public class Params
    {
        public GraphicID    gid;
        public String       asset;
        public int          frames;

        Params(final GraphicID _gid, final String _asset, final int _frames)
        {
            this.gid    =_gid;
            this.asset  = _asset;
            this.frames =_frames;
        }
    }

    public       GraphicID        graphicID;
    public final App              app;
    public       EntityDescriptor entityDescriptor;
    public       int              activeCount;

    public final Params[] entitiesList =
        {
            new Params(GraphicID.G_SPINNING_BALL,   GameAssets._SPINNING_BALL_ASSET,    GameAssets._SPINNING_BALL_FRAMES),
            new Params(GraphicID.G_BLOB,            GameAssets._BLOB_ASSET,             GameAssets._BLOB_FRAMES),
            new Params(GraphicID.G_DOG,             GameAssets._DOG_ASSET,              GameAssets._DOG_FRAMES),
            new Params(GraphicID.G_3LEGS_ALIEN,     GameAssets._3LEGS_ALIEN_ASSET,      GameAssets._3LEGS_ALIEN_FRAMES),
            new Params(GraphicID.G_TOPSPIN,         GameAssets._TOPSPIN_ASSET,          GameAssets._TOPSPIN_FRAMES),
            new Params(GraphicID.G_3BALLS,          GameAssets._3BALLS_ASSET,           GameAssets._3BALLS_FRAMES),
            new Params(GraphicID.G_TWINKLES,        GameAssets._TWINKLES_ASSET,         GameAssets._TWINKLES_FRAMES),
        };

    public GenericEntityManager(App _app)
    {
        this.app = _app;
        this.graphicID = GraphicID.G_NO_ID;
    }

    public GenericEntityManager(GraphicID _graphicID, App _app)
    {
        this.app = _app;
        this.graphicID = _graphicID;
    }

    @Override
    public void init()
    {
        activeCount = 0;
    }

    @Override
    public void update()
    {
        if (activeCount < app.roomManager.getMaxAllowed(graphicID))
        {
            create();
        }
    }

    @Override
    public void create()
    {
    }

    @Override
    public void create(final String _asset, final int _frames, final Animation.PlayMode _mode, final int x, final int y)
    {
        if (app.entityUtils.canUpdate(graphicID))
        {
            entityDescriptor = new EntityDescriptor
                (
                    x,
                    y,
                    app.entityUtils.getInitialZPosition(graphicID),
                    app.assets.getAnimationRegion(_asset),
                    _frames,
                    _mode
                );

            entityDescriptor._INDEX = app.entityData.entityMap.size;
        }
    }

    public int getParamsIndex(GraphicID _gid)
    {
        int index = 0;

        for (final Params param : entitiesList)
        {
            if (param.gid != _gid)
            {
                index++;
            }
        }

        return index;
    }

    public SimpleVec2 findCoordinates(final GraphicID targetGID)
    {
        SimpleVec2 coords = new SimpleVec2();

        for (MarkerTile marker : app.mapCreator.placementTiles)
        {
            if (marker._GID == targetGID)
            {
                coords.set(marker._X, marker._Y);
            }
        }

        return coords;
    }

    public Array<SimpleVec2> findMultiCoordinates(final GraphicID targetGID)
    {
        Array<SimpleVec2> coords = new Array<>();

        for (MarkerTile marker : app.mapCreator.placementTiles)
        {
            if (marker._GID == targetGID)
            {
                coords.add(new SimpleVec2(marker._X, marker._Y));
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
        return graphicID;
    }

    @Override
    public boolean isPlaceable()
    {
        return false;
    }

    @Override
    public void setPlaceable(boolean placeable)
    {
    }

    @Override
    public void dispose()
    {
        graphicID = null;
        entityDescriptor = null;
    }
}

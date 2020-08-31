package com.richikin.jetman.maps;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.utils.Array;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.Entities;
import com.richikin.jetman.entities.components.EntityManagerComponent;
import com.richikin.jetman.entities.objects.EntityDef;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.maths.Box;
import com.richikin.jetman.maths.SimpleVec2F;
import com.richikin.jetman.physics.Direction;
import com.richikin.jetman.physics.Movement;
import com.richikin.jetman.physics.Speed;
import com.richikin.jetman.utils.logging.Trace;

public class MapCreator
{
    public Array<MarkerTile> placementTiles;

    private final App app;

    public MapCreator(App _app)
    {
        this.app            = _app;
        this.placementTiles = new Array<>();
    }

    /**
     * Create the map data for the current level
     * Load the TileMap data, then create the
     * game map from that data.
     */
    public void createMap()
    {
        Trace.__FILE_FUNC();

        for (EntityManagerComponent component : app.entityData.managerList)
        {
            component.setPlaceable(false);
        }

        placementTiles.clear();

        parseMarkerTiles();
        createCollisionBoxes();
    }

    /**
     * NB: Does NOT create entities. This just extracts markers from
     * the Tile map (Object Layer) and creates the necessary information from them.
     */
    protected void parseMarkerTiles()
    {
        GraphicID graphicID = GraphicID.G_NO_ID;
        TileID    tileID;

        for (MapObject mapObject : app.mapData.objectTiles)
        {
            if (mapObject instanceof TiledMapTileMapObject)
            {
                //
                // Find the objects details ready for parsing
                boolean isFound = false;

                if (null != mapObject.getName())
                {
                    for (EntityDef entityDef : Entities.entityList)
                    {
                        if (mapObject.getName().equals(entityDef.objectName))
                        {
                            if (entityDef.graphicID != GraphicID.G_NO_ID)
                            {
                                ObjectTileProperties properties = setObjectTileProperties(entityDef);

                                createPlacementTile(mapObject, entityDef, properties);
                            }
                        }
                    }
                }
            }
        }
    }

    private void createPlacementTile(MapObject _mapObject, EntityDef _entityDef, ObjectTileProperties _properties)
    {
        MarkerTile markerTile = new MarkerTile();

        markerTile._X     = (int) (((TiledMapTileMapObject) _mapObject).getX() / Gfx.getTileWidth());
        markerTile._Y     = (int) (((TiledMapTileMapObject) _mapObject).getY() / Gfx.getTileHeight());
        markerTile._GID   = _entityDef.graphicID;
        markerTile._TILE  = _entityDef.tileID;
        markerTile._ASSET = _entityDef.asset;
        markerTile._INDEX = placementTiles.size;
        markerTile._DIST  = new SimpleVec2F();
        markerTile._DIR   = new Direction();
        markerTile._SPEED = new Speed();

        if (_properties.isSizeBoxNeeded)
        {
            int width  = ((int) _mapObject.getProperties().get("width"));
            int height = ((int) _mapObject.getProperties().get("height"));

            markerTile._BOX = new Box(0, 0, width, height);
        }

        if (_properties.hasDistance)
        {
            markerTile._DIST.set
                (
                    ((int) _mapObject.getProperties().get("xdistance")),
                    ((int) _mapObject.getProperties().get("ydistance"))
                );
        }

        if (_properties.hasDirection)
        {
            markerTile._DIR.set
                (
                    _mapObject.getProperties().get("xdirection")
                        .equals("right") ? Movement._DIRECTION_RIGHT :
                        _mapObject.getProperties().get("xdirection")
                            .equals("left") ? Movement._DIRECTION_LEFT : Movement._DIRECTION_STILL,

                    _mapObject.getProperties().get("ydirection")
                        .equals("up") ? Movement._DIRECTION_UP :
                        _mapObject.getProperties().get("ydirection")
                            .equals("down") ? Movement._DIRECTION_DOWN : Movement._DIRECTION_STILL
                );
        }

        if (_properties.hasSpeed)
        {
            markerTile._SPEED.set
                (
                    ((float) _mapObject.getProperties().get("xspeed")),
                    ((float) _mapObject.getProperties().get("yspeed"))
                );
        }

        if (_properties.isLinked)
        {
            //
            // Fetch the link ID of the attached entity
            if (_mapObject.getProperties().get("connection") != null)
            {
                markerTile._LINK = (int) _mapObject.getProperties().get("connection");
            }
        }

        placementTiles.add(markerTile);
    }

    protected void createCollisionBoxes()
    {
        for (MapObject mapObject : app.mapData.mapObjects)
        {
            if (mapObject instanceof RectangleMapObject)
            {
                if (null != mapObject.getName())
                {
                    // TODO: 13/08/2020 -
                    //
                    // Parse through all RectangleMapObjects in the TiledMap
                    // and create a GameEntity object for each one.
                    // Assign a Box2D body to each one and add to the World.
                    //
                    // Example:
                    /*
                    GameEntity gameEntity = new GameEntity(app);

                    switch (mapObject.getName().toLowerCase())
                    {
                        case "ground":
                        case "wall":
                        {
                            gameEntity.gid = GraphicID._GROUND;
                            gameEntity.type = GraphicID._OBSTACLE;
                            gameEntity.bodyCategory = Gfx.CAT_GROUND;
                            gameEntity.collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_MOBILE_ENEMY;
                            gameEntity.b2dBody = app.worldModel.bodyBuilder.createStaticBody(gameEntity);

                            app.entityData.addEntity(gameEntity);
                        }
                        break;
                    }
                    */
                }
            }
        }
    }

    private ObjectTileProperties setObjectTileProperties(EntityDef _entityDef)
    {
        // TODO: 13/08/2020 - set properties based on the type of entity passed.
        // eg: The entity might have an initial direction and speed...

        return new ObjectTileProperties();
    }
}

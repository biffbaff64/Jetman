package com.richikin.jetman.maps;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.Entities;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.entities.components.EntityManagerComponent;
import com.richikin.jetman.entities.objects.GameEntity;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.utilslib.maths.Box;
import com.richikin.utilslib.maths.SimpleVec2F;
import com.richikin.jetman.physics.aabb.AABBData;
import com.richikin.jetman.physics.aabb.CollisionObject;
import com.richikin.utilslib.physics.Direction;
import com.richikin.utilslib.physics.Movement;
import com.richikin.utilslib.physics.Speed;
import com.richikin.utilslib.logging.Trace;

public class MapCreator
{
    private final App app;

    public MapCreator(App _app)
    {
        this.app = _app;
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

        app.mapData.placementTiles.clear();

        parseMarkerTiles();
        createCollisionBoxes();

//        if (Developer.isDevMode())
//        {
//            Trace.megaDivider("PLACEMENT TILE ARRAY - START");
//            debugPlacementsTiles();
//            Trace.megaDivider("PLACEMENT TILE ARRAY - END");
//        }
    }

    /**
     * NB: Does NOT create entities. This just extracts markers from
     * the Tile map (Object Layer) and creates the necessary information from them.
     */
    protected void parseMarkerTiles()
    {
        for (MapObject mapObject : app.mapData.objectTiles)
        {
            if (mapObject instanceof TiledMapTileMapObject)
            {
                //
                // Find the objects details ready for parsing
                if (null != mapObject.getName())
                {
                    for (SpriteDescriptor descriptor : Entities.entityList)
                    {
                        if (mapObject.getName().equals(descriptor._NAME))
                        {
                            if (descriptor._GID != GraphicID.G_NO_ID)
                            {
                                ObjectTileProperties properties = setObjectTileProperties(descriptor);

                                createPlacementTile(mapObject, descriptor, properties);
                            }
                        }
                    }
                }
            }
        }
    }

    private void createPlacementTile(MapObject _mapObject, SpriteDescriptor _descriptor, ObjectTileProperties _properties)
    {
        SpriteDescriptor markerTile = new SpriteDescriptor();

        markerTile._POSITION.x = (int) (((TiledMapTileMapObject) _mapObject).getX() / Gfx.getTileWidth());
        markerTile._POSITION.y = (int) (((TiledMapTileMapObject) _mapObject).getY() / Gfx.getTileHeight());
        markerTile._GID        = _descriptor._GID;
        markerTile._TILE       = _descriptor._TILE;
        markerTile._ASSET      = _descriptor._ASSET;
        markerTile._INDEX      = _descriptor._INDEX;
        markerTile._DIST       = new SimpleVec2F();
        markerTile._DIR        = new Direction();
        markerTile._SPEED      = new Speed();

        //
        // Create the bounding box for this placement tile.
        markerTile._BOX = new Box
            (
                (int) (((TiledMapTileMapObject)  _mapObject).getX()),
                (int) (((TiledMapTileMapObject)  _mapObject).getY()),
                Gfx.getTileWidth(),
                Gfx.getTileHeight()
            );

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

        app.mapData.placementTiles.add(markerTile);
    }

    protected void createCollisionBoxes()
    {
        GameEntity gameEntity;

        gameEntity = new GameEntity(app);
        gameEntity.gid = GraphicID.G_NO_ID;
        gameEntity.type = GraphicID.G_NO_ID;
        gameEntity.position = new SimpleVec2F();
        gameEntity.collisionObject = app.collisionUtils.newObject();
        AABBData.add(gameEntity.collisionObject);

        for (MapObject mapObject : app.mapData.mapObjects)
        {
            if (mapObject instanceof RectangleMapObject)
            {
                if (null != mapObject.getName())
                {
                    gameEntity = new GameEntity(app);

                    switch (mapObject.getName().toLowerCase())
                    {
                        case "ceiling":
                        {
                            gameEntity.gid = GraphicID._CEILING;
                            gameEntity.type = GraphicID._OBSTACLE;
                            gameEntity.bodyCategory = Gfx.CAT_CEILING;
                            gameEntity.collidesWith = Gfx.CAT_PLAYER;
                        }
                        break;

                        case "crater":
                        {
                            gameEntity.gid = GraphicID._CRATER;
                            gameEntity.type = GraphicID._OBSTACLE;
                            gameEntity.bodyCategory = Gfx.CAT_SCENERY;
                            gameEntity.collidesWith = Gfx.CAT_PLAYER
                                | Gfx.CAT_PLAYER_WEAPON
                                | Gfx.CAT_MOBILE_ENEMY
                                | Gfx.CAT_MISSILE_BASE
                                | Gfx.CAT_TELEPORTER
                                | Gfx.CAT_FIXED_ENEMY;
                        }
                        break;

                        case "ground":
                        case "wall":
                        {
                            gameEntity.gid = GraphicID._GROUND;
                            gameEntity.type = GraphicID._OBSTACLE;
                            gameEntity.bodyCategory = Gfx.CAT_GROUND;
                            gameEntity.collidesWith = Gfx.CAT_PLAYER
                                | Gfx.CAT_PLAYER_WEAPON
                                | Gfx.CAT_MOBILE_ENEMY
                                | Gfx.CAT_MISSILE_BASE
                                | Gfx.CAT_TELEPORTER
                                | Gfx.CAT_FIXED_ENEMY;
                        }
                        break;

                        default:
                            break;
                    }

                    if (gameEntity.gid != GraphicID.G_NO_ID)
                    {
                        gameEntity.position = new SimpleVec2F
                            (
                                (float) mapObject.getProperties().get("x"),
                                (float) mapObject.getProperties().get("y")
                            );
                        gameEntity.frameWidth   = (float) mapObject.getProperties().get("width");
                        gameEntity.frameHeight  = (float) mapObject.getProperties().get("height");
                        gameEntity.setCollisionObject(gameEntity.position.x, gameEntity.position.y);
                    }
                }
            }
        }
    }

    private ObjectTileProperties setObjectTileProperties(SpriteDescriptor _descriptor)
    {
        // TODO: 13/08/2020 - set properties based on the type of entity passed.
        // eg: The entity might have an initial direction and speed...

        return new ObjectTileProperties();
    }

    private void debugPlacementsTiles()
    {
        for (SpriteDescriptor tile : app.mapData.placementTiles)
        {
            tile.debug();
        }
    }

    private void debugCollisionBoxes()
    {
        Trace.__FILE_FUNC();

        for (CollisionObject object : AABBData.boxes())
        {
            Trace.dbg("_GID  : " + object.gid + "  _INDEX: " + object.index);
        }
    }
}

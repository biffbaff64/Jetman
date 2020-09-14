package com.richikin.jetman.maps;

import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.objects.TiledMapTileMapObject;
import com.badlogic.gdx.utils.Array;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.managers.PlayerManager;
import com.richikin.jetman.entities.objects.EntityDescriptor;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.input.buttons.AnimatedButton;
import com.richikin.jetman.maths.SimpleVec2;
import com.richikin.jetman.maths.SimpleVec2F;
import com.richikin.jetman.utils.logging.Trace;
import org.jetbrains.annotations.NotNull;

public class RoomManager
{
    public static final int _MAX_TELEPORTERS = 2;

    private static final String _MAPS_PATH = "data/maps/";

    private final Room[] roomMap =
        {
            null,
            new Room
                (
                    "level1.tmx",
                    new MapEntry
                        (
                            -2.0f, 1200,
                            6, 6, 0, 0, 0, 0,
                            0, 0, 0, 0, 0, 0
                        )
                ),
            new Room
                (
                    "level1.tmx",
                    new MapEntry
                        (
                            3.0f, 1200,
                            6, 6, 2, 0, 0, 0,
                            0, 0, 0, 0, 0, 0
                        )
                ),
            new Room
                (
                    "level1.tmx",
                    new MapEntry
                        (
                            -3.5f, 1100,
                            6, 6, 3, 0, 1, 0,
                            0, 0, 0, 0, 0, 0
                        )
                ),
            new Room
                (
                    "level1.tmx",
                    new MapEntry
                        (
                            3.0f, 1100,
                            6, 6, 3, 2, 0, 0,
                            0, 0, 0, 0, 0, 0
                        )
                ),
            new Room
                (
                    "level1.tmx",
                    new MapEntry
                        (
                            -5.0f, 1100,
                            24, 4, 2, 0, 1, 0,
                            0, 0, 0, 0, 0, 1
                        )
                ),
            new Room
                (
                    "level1.tmx",
                    new MapEntry
                        (
                            2.0f, 1100,
                            6, 6, 3, 2, 2, 0,
                            0, 0, 0, 0, 0, 0
                        )
                ),
            new Room
                (
                    "level1.tmx",
                    new MapEntry
                        (
                            -3.0f, 1100,
                            6, 0, 3, 3, 0, 2,
                            0, 0, 0, 0, 0, 0
                        )
                ),
            new Room
                (
                    "level1.tmx",
                    new MapEntry
                        (
                            2.5f, 1100,
                            6, 0, 0, 3, 2, 2,
                            1, 0, 0, 0, 0, 0
                        )
                ),
            new Room
                (
                    "level1.tmx",
                    new MapEntry
                        (
                            -2.5f, 1100,
                            8, 0, 0, 3, 1, 1,
                            2, 0, 0, 0, 0, 0
                        )
                ),
            new Room
                (
                    "level1.tmx",
                    new MapEntry
                        (
                            3.0f, 1100,
                            28, 0, 0, 0, 0, 0,
                            0, 0, 0, 0, 0, 1
                        )
                ),
        };

    public Room activeRoom;

    private final App app;

    public RoomManager(App _app)
    {
        Trace.__FILE_FUNC();

        this.app = _app;

//        worldWidth  = 1;
//        worldHeight = roomMap.length;

//        Trace.dbg("_WORLD_WIDTH : " + worldWidth);
//        Trace.dbg("_WORLD_HEIGHT: " + worldHeight);
    }

    public void initialise()
    {
        Trace.__FILE_FUNC();

        setRoom(app.getLevel());
    }

    private void setRoom(int _index)
    {
        if (roomMap[_index] != null)
        {
            activeRoom.set(roomMap[_index]);
        }
    }

    public int getMaxAllowed(GraphicID gid)
    {
        int thisMax;

        switch (gid)
        {
            case G_3BALLS:
            {
                thisMax = calculateEntityCount(roomMap[app.getLevel()].mapEntry.max3Balls);
            }
            break;

            case G_3BALLS_UFO:
            {
                thisMax = calculateEntityCount(roomMap[app.getLevel()].mapEntry.max3BallAliens);
            }
            break;

            case G_3LEGS_ALIEN:
            {
                thisMax = calculateEntityCount(roomMap[app.getLevel()].mapEntry.max3LegAliens);
            }
            break;

            case G_ALIEN_WHEEL:
            {
                thisMax = calculateEntityCount(roomMap[app.getLevel()].mapEntry.maxWheels);
            }
            break;

            case G_ASTEROID:
            {
                thisMax = calculateEntityCount(roomMap[app.getLevel()].mapEntry.maxAsteroids);
            }
            break;

            case G_BLOB:
            {
                thisMax = calculateEntityCount(roomMap[app.getLevel()].mapEntry.maxBlobs);
            }
            break;

            case G_DOG:
            {
                thisMax = calculateEntityCount(roomMap[app.getLevel()].mapEntry.maxDogs);
            }
            break;

            case G_GREEN_BLOCK:
            {
                thisMax = calculateEntityCount(roomMap[app.getLevel()].mapEntry.maxGreenBlocks);
            }
            break;

            case G_SPINNING_BALL:
            {
                thisMax = calculateEntityCount(roomMap[app.getLevel()].mapEntry.maxSpinningBalls);
            }
            break;

            case G_TWINKLES:
            {
                thisMax = calculateEntityCount(roomMap[app.getLevel()].mapEntry.maxTwinkles);
            }
            break;

            case G_TOPSPIN:
            {
                thisMax = calculateEntityCount(roomMap[app.getLevel()].mapEntry.maxTopSpinners);
            }
            break;

            case G_STAR_SPINNER:
            {
                thisMax = calculateEntityCount(roomMap[app.getLevel()].mapEntry.maxStarSpinners);
            }
            break;

            default:
            {
                thisMax = 0;
            }
            break;
        }

        return thisMax;
    }

    public float getBaseOffset()
    {
        return roomMap[app.getLevel()].mapEntry.baseOffset;
    }

    public float getFireRate()
    {
        return roomMap[app.getLevel()].mapEntry.fireRate;
    }

    private int calculateEntityCount(int initialValue)
    {
        if (initialValue > 0)
        {
            return (int) ((float) initialValue * app.gameProgress.getGameDifficulty());
        }

        return initialValue;
    }

    public String getCurrentMap()
    {
        int index;

        if ((index = app.getLevel()) > roomMap.length)
        {
            index = roomMap.length - 1;
        }

        return _MAPS_PATH + roomMap[index].roomName;
    }

    public String getMapNameWithPath()
    {
        return _MAPS_PATH + roomMap[app.getLevel()].roomName;
    }

    @NotNull
    public String getMapNameWithPath(String roomName)
    {
        return _MAPS_PATH + roomName;
    }

    public SimpleVec2 getStartPosition()
    {
        SimpleVec2 position = new SimpleVec2();

        for (MarkerTile tile : app.mapCreator.placementTiles)
        {
            if (tile._GID.equals(GraphicID.G_PLAYER))
            {
                position.set(tile._X, tile._Y);
            }
        }

        return position;
    }
}

package com.richikin.jetman.maps;

import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.enumslib.GraphicID;
import com.richikin.utilslib.maths.SimpleVec2;
import com.richikin.utilslib.logging.Trace;
import org.jetbrains.annotations.NotNull;

public class RoomManager
{
    public static final int _MAX_TELEPORTERS = 2;
    public static final int _MAX_DEFENCE_STATIONS = 2;

    private static final String _MAPS_PATH = "data/maps/";

    private final Room[] roomMap =
        {
            null,
            // ####################################################################
            // Test Level
            new Room
                (
                    "level1.tmx",
                    new MapEntry
                        (
                            -2.0f, 1200,
                            // Zero Entities
                            0, 4, 0, 0, 0, 0,
                            0, 0, 0, 0, 0, 0
                            // Lots of Entities
//                            4, 4, 4, 4, 4, 4,
//                            4, 4, 4, 4, 4, 4
                        )
                ),
            // ####################################################################
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

    public RoomManager()
    {
        Trace.__FILE_FUNC();

//        worldWidth  = 1;
//        worldHeight = roomMap.length;

//        Trace.dbg("_WORLD_WIDTH : " + worldWidth);
//        Trace.dbg("_WORLD_HEIGHT: " + worldHeight);
    }

    public void initialise()
    {
        Trace.__FILE_FUNC();

        setRoom(App.getLevel());
    }

    private void setRoom(int _index)
    {
        if (roomMap[_index] != null)
        {
            if (activeRoom == null)
            {
                activeRoom = new Room();
            }

            activeRoom.set(roomMap[_index]);
        }
    }

    public int getMaxAllowed(GraphicID gid)
    {
        int thisMax;

        switch (gid)
        {
            case G_STAIR_CLIMBER:
            {
                thisMax = calculateEntityCount(roomMap[App.getLevel()].mapEntry.maxStairClimbers);
            }
            break;

            case G_3BALLS_UFO:
            {
                thisMax = calculateEntityCount(roomMap[App.getLevel()].mapEntry.max3BallAliens);
            }
            break;

            case G_3LEGS_ALIEN:
            {
                thisMax = calculateEntityCount(roomMap[App.getLevel()].mapEntry.max3LegAliens);
            }
            break;

            case G_ALIEN_WHEEL:
            {
                thisMax = calculateEntityCount(roomMap[App.getLevel()].mapEntry.maxWheels);
            }
            break;

            case G_ASTEROID:
            {
                thisMax = calculateEntityCount(roomMap[App.getLevel()].mapEntry.maxAsteroids);
            }
            break;

            case G_BLOB:
            {
                thisMax = calculateEntityCount(roomMap[App.getLevel()].mapEntry.maxBlobs);
            }
            break;

            case G_DOG:
            {
                thisMax = calculateEntityCount(roomMap[App.getLevel()].mapEntry.maxDogs);
            }
            break;

            case G_GREEN_BLOCK:
            {
                thisMax = calculateEntityCount(roomMap[App.getLevel()].mapEntry.maxGreenBlocks);
            }
            break;

            case G_SPINNING_BALL:
            {
                thisMax = calculateEntityCount(roomMap[App.getLevel()].mapEntry.maxSpinningBalls);
            }
            break;

            case G_TWINKLES:
            {
                thisMax = calculateEntityCount(roomMap[App.getLevel()].mapEntry.maxTwinkles);
            }
            break;

            case G_TOPSPIN:
            {
                thisMax = calculateEntityCount(roomMap[App.getLevel()].mapEntry.maxTopSpinners);
            }
            break;

            case G_STAR_SPINNER:
            {
                thisMax = calculateEntityCount(roomMap[App.getLevel()].mapEntry.maxStarSpinners);
            }
            break;

            case _CRATER:
            case G_CRATER:
            {
                thisMax = 4;
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
        return roomMap[App.getLevel()].mapEntry.baseOffset;
    }

    public float getFireRate()
    {
        return roomMap[App.getLevel()].mapEntry.fireRate;
    }

    private int calculateEntityCount(int initialValue)
    {
        if (initialValue > 0)
        {
            return (int) ((float) initialValue * App.gameProgress.getGameDifficulty());
        }

        return initialValue;
    }

    public String getCurrentMap()
    {
        int index;

        if ((index = App.getLevel()) > roomMap.length)
        {
            index = roomMap.length - 1;
        }

        return _MAPS_PATH + roomMap[index].roomName;
    }

    public String getMapNameWithPath()
    {
        return _MAPS_PATH + roomMap[App.getLevel()].roomName;
    }

    @NotNull
    public String getMapNameWithPath(String roomName)
    {
        return _MAPS_PATH + roomName;
    }

    public SimpleVec2 getStartPosition()
    {
        SimpleVec2 position = new SimpleVec2();

        for (SpriteDescriptor tile : App.mapData.placementTiles)
        {
            if (tile._GID.equals(GraphicID.G_PLAYER))
            {
                position.set(tile._POSITION.x, tile._POSITION.y);
            }
        }

        return position;
    }
}

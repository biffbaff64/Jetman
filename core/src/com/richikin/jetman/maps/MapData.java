package com.richikin.jetman.maps;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapObjects;
import com.badlogic.gdx.maps.objects.RectangleMapObject;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.parallax.LayerImage;
import com.richikin.utilslib.maths.SimpleVec2;
import com.richikin.utilslib.maths.SimpleVec2F;
import com.richikin.utilslib.physics.Movement;
import com.richikin.utilslib.logging.Trace;

public class MapData
{
    public static final int _GAME_TILES       = 0;
    public static final int _EXTRA_GAME_TILES = 1;
    public static final int _OBJECT_TILES     = 2;
    public static final int _COLLISION_LAYER  = 3;

    public final String[] mapLayerNames =
        {
            "game tiles",
            "extra game tiles",
            "object tiles",
            "collision",
        };

    public final LayerImage[] backgroundLayers =
        {
            new LayerImage("data/background_layer.png", 0.0f, 0.0f),
            new LayerImage("data/nebula_background.png", 0.0f, 0.0f),
//            new LayerImage("data/stars_background.png", 0.0f, 0.0f),
            new LayerImage("data/dark_mountains.png", 1.2f, 0.01f),
            new LayerImage("data/light_mountains.png", 2.4f, 0.015f),
        };

    //
    // NOTE: Not made final because _horizointalSpeed fields
    // will be modified based on LJMs current speed.
    public LayerImage[] foregroundLayers =
        {
            new LayerImage("data/foreground.png", 0.0f, 0.0f),
            new LayerImage("data/foreground_near.png", 0.0f, 0.0f),
        };

    public OrthogonalTiledMapRenderer mapRenderer;
    public TmxMapLoader               tmxMapLoader;

    public int maxScrollX;
    public int maxScrollY;
    public int minScrollX;
    public int minScrollY;
    public int scrollXDirection;
    public int scrollYDirection;

    // Current bottom left position of the screen
    // window (0, 0) into the game currentMap
    public SimpleVec2  mapPosition;
    public SimpleVec2  previousMapPosition;
    public SimpleVec2F checkPoint;
    public Rectangle   viewportBox;
    public Rectangle   innerViewportBox;
    public Rectangle   extendedViewportBox;

    public TiledMapTileLayer gameTilesLayer;
    public TiledMapTileLayer extraGameTilesLayer;

    public TiledMap                currentMap;
    public MapObjects              objectTiles;
    public MapObjects              mapObjects;
    public Array<Rectangle>        enemyFreeZones;
    public Array<SpriteDescriptor> placementTiles;

    private       String currentMapName;
    private final App    app;

    public MapData(App _app)
    {
        Trace.__FILE_FUNC();

        this.app = _app;

        mapPosition         = new SimpleVec2();
        previousMapPosition = new SimpleVec2();
        checkPoint          = new SimpleVec2F();
        tmxMapLoader        = new TmxMapLoader();
        viewportBox         = new Rectangle();
        innerViewportBox    = new Rectangle();
        extendedViewportBox = new Rectangle();
        enemyFreeZones      = new Array<>();
        placementTiles      = new Array<>();
    }

    /**
     * Load and set up the current room.
     */
    public void initialiseRoom()
    {
        initialiseMap(app.roomManager.getMapNameWithPath(), mapLayerNames);
    }

    public void initialiseMap(String gameMap, String[] mapLayers)
    {
        currentMapName = gameMap;
        currentMap     = tmxMapLoader.load(gameMap);

        setGameLevelMap(mapLayers);
        setEnemyFreeZones();

        if (mapRenderer != null)
        {
            mapRenderer.setMap(currentMap);
        }
        else
        {
            mapRenderer = new OrthogonalTiledMapRenderer(currentMap, app.spriteBatch);
        }

        scrollXDirection = Movement._DIRECTION_STILL;
        scrollYDirection = Movement._DIRECTION_STILL;

        debugMap();
    }

    /**
     * Update the screen virtual window.
     * This box is used for checking that entities are
     * visible on screen.
     */
    public void update()
    {
        OrthographicCamera camera = app.baseRenderer.spriteGameCamera.camera;

        float xPos       = camera.position.x;
        float yPos       = camera.position.y;
        float viewWidth  = camera.viewportWidth;
        float viewHeight = camera.viewportHeight;
        float zoom       = camera.zoom;

        float x      = (xPos - ((viewWidth * zoom) / 2));
        float y      = (yPos - ((viewHeight * zoom) / 2));
        float width  = (viewWidth * zoom);
        float height = (viewHeight * zoom);

        viewportBox.set(x, y, width, height);

        innerViewportBox.set
            (
                mapPosition.getX(),
                mapPosition.getY(),
                Gfx._VIEW_WIDTH,
                Gfx._VIEW_HEIGHT
            );

        extendedViewportBox.set
            (
                x - (width / 4),
                y - (height / 4),
                width + (width / 2),
                height + (height / 2)
            );
    }

    /**
     * Draws the TiledMap game tile layers.
     *
     * @param camera The {@link OrthographicCamera} to use.
     */
    public void render(OrthographicCamera camera)
    {
        mapRenderer.setView(camera);
        mapRenderer.renderTileLayer(gameTilesLayer);
        mapRenderer.renderTileLayer(extraGameTilesLayer);
    }

    private void setTileIDBoxes()
    {
        for (MapObject objectTile : objectTiles)
        {

        }
    }

    private void setGameLevelMap(String[] mapLayers)
    {
        gameTilesLayer      = (TiledMapTileLayer) currentMap.getLayers().get(mapLayers[_GAME_TILES]);
        extraGameTilesLayer = (TiledMapTileLayer) currentMap.getLayers().get(mapLayers[_EXTRA_GAME_TILES]);
        objectTiles         = currentMap.getLayers().get(mapLayers[_OBJECT_TILES]).getObjects();
        mapObjects          = currentMap.getLayers().get(mapLayers[_COLLISION_LAYER]).getObjects();

        Gfx.tileWidth  = currentMap.getProperties().get("tilewidth", Integer.class);
        Gfx.tileHeight = currentMap.getProperties().get("tileheight", Integer.class);
        Gfx.mapWidth   = (currentMap.getProperties().get("width", Integer.class) * Gfx.tileWidth);
        Gfx.mapHeight  = (currentMap.getProperties().get("height", Integer.class) * Gfx.tileHeight);

        maxScrollX = Gfx.mapWidth - Gfx._VIEW_WIDTH;
        maxScrollY = Gfx.mapHeight - Gfx._VIEW_HEIGHT;
        minScrollX = 0;
        minScrollY = 0;

        previousMapPosition.set(mapPosition.getX(), mapPosition.getY());
    }

    /**
     * Creates a map of areas that enemies cannot spawn into.
     */
    private void setEnemyFreeZones()
    {
        enemyFreeZones.clear();

        for (MapObject object : mapObjects)
        {
            if (object instanceof RectangleMapObject)
            {
                enemyFreeZones.add(new Rectangle(((RectangleMapObject) object).getRectangle()));
            }
        }
    }

    private void debugMap()
    {
        Trace.__FILE_FUNC();
        Trace.dbg("Map Name: " + currentMapName);
        Trace.dbg("Width: " + currentMap.getProperties().get("width"));
        Trace.dbg("Height: " + currentMap.getProperties().get("height"));
        Trace.dbg("tileWidth: " + Gfx.getTileWidth());
        Trace.dbg("tileHeight: " + Gfx.getTileHeight());
    }

    public void dispose()
    {
        Trace.__FILE_FUNC();

        mapRenderer.dispose();
        mapRenderer = null;

        gameTilesLayer      = null;
        extraGameTilesLayer = null;
        currentMap          = null;
        objectTiles         = null;
        mapObjects          = null;
        mapPosition         = null;
        previousMapPosition = null;
        checkPoint          = null;
        tmxMapLoader        = null;
    }
}

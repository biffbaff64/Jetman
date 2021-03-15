package com.richikin.jetman.characters.managers;

import com.badlogic.gdx.math.MathUtils;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.characters.enemies.AlienWheel;
import com.richikin.jetman.characters.enemies.Asteroid;
import com.richikin.jetman.characters.enemies.Blob;
import com.richikin.jetman.characters.enemies.Dog;
import com.richikin.jetman.characters.enemies.GreenBlock;
import com.richikin.jetman.characters.enemies.SpinningBall;
import com.richikin.jetman.characters.enemies.StarSpinner;
import com.richikin.jetman.characters.enemies.ThreeBallsUFO;
import com.richikin.jetman.characters.enemies.ThreeLegsAlien;
import com.richikin.jetman.characters.enemies.TopSpin;
import com.richikin.jetman.characters.enemies.Twinkle;
import com.richikin.jetman.config.Settings;
import com.richikin.jetman.core.App;
import com.richikin.jetman.developer.Developer;
import com.richikin.jetman.characters.basetypes.StairClimber;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.enumslib.GraphicID;
import com.richikin.utilslib.graphics.GraphicIndex;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.maths.SimpleVec2;

public class AlienManager extends GenericEntityManager
{
    private final GraphicIndex[] aliens =
        {
            new GraphicIndex(GraphicID.G_3BALLS_UFO, 0),
            new GraphicIndex(GraphicID.G_3LEGS_ALIEN, 0),
            new GraphicIndex(GraphicID.G_ASTEROID, 0),
            new GraphicIndex(GraphicID.G_ALIEN_WHEEL, 0),
            new GraphicIndex(GraphicID.G_BLOB, 0),
            new GraphicIndex(GraphicID.G_DOG, 0),
            new GraphicIndex(GraphicID.G_GREEN_BLOCK, 0),
            new GraphicIndex(GraphicID.G_SPINNING_BALL, 0),
            new GraphicIndex(GraphicID.G_STAIR_CLIMBER, 0),
            new GraphicIndex(GraphicID.G_STAR_SPINNER, 0),
            new GraphicIndex(GraphicID.G_TOPSPIN, 0),
            new GraphicIndex(GraphicID.G_TWINKLES, 0),
        };

    public AlienManager()
    {
        super();
    }

    @Override
    public void init()
    {
        if (!Developer.isDevMode() || App.settings.isEnabled(Settings._ENABLE_ENEMIES))
        {
            Trace.__FILE_FUNC();

            for (GraphicIndex item : aliens)
            {
                item.value = 0;
            }

            update();
        }
    }

    @Override
    public void free(final GraphicID gid)
    {
        for (GraphicIndex item : aliens)
        {
            if (item.graphicID == gid)
            {
                item.value = Math.max(0, item.value - 1);
            }
        }
    }

    @Override
    public void update()
    {
        if (!Developer.isDevMode() || App.settings.isEnabled(Settings._ENABLE_ENEMIES))
        {
            for (GraphicIndex alien : aliens)
            {
                if (alien.value < App.roomManager.getMaxAllowed(alien.graphicID))
                {
                    create(alien.graphicID);
                    alien.value++;
                }
            }
        }
    }

    public void create(GraphicID graphicID)
    {
        if (App.entityUtils.canUpdate(graphicID))
        {
            SimpleVec2 markerPos = setInitialPosition(graphicID);

            descriptor             = App.entities.getDescriptor(graphicID);
            descriptor._SIZE       = GameAssets.getAssetSize(graphicID);
            descriptor._POSITION.x = markerPos.getX();
            descriptor._POSITION.y = markerPos.getY();
            descriptor._POSITION.z = App.entityUtils.getInitialZPosition(graphicID);
            descriptor._INDEX      = App.entityData.entityMap.size;

            switch (graphicID)
            {
                case G_3BALLS_UFO:
                {
                    ThreeBallsUFO threeBallsUFO = new ThreeBallsUFO();
                    threeBallsUFO.initialise(descriptor);
                    App.entityData.addEntity(threeBallsUFO);
                }
                break;

                case G_3LEGS_ALIEN:
                {
                    ThreeLegsAlien threeLegsAlien = new ThreeLegsAlien();
                    threeLegsAlien.initialise(descriptor);
                    App.entityData.addEntity(threeLegsAlien);
                }
                break;

                case G_ASTEROID:
                {
                    Asteroid asteroid = new Asteroid();
                    asteroid.initialise(descriptor);
                    App.entityData.addEntity(asteroid);
                }
                break;

                case G_ALIEN_WHEEL:
                {
                    AlienWheel alienWheel = new AlienWheel();
                    alienWheel.initialise(descriptor);
                    App.entityData.addEntity(alienWheel);
                }
                break;

                case G_BLOB:
                {
                    Blob blob = new Blob();
                    blob.initialise(descriptor);
                    App.entityData.addEntity(blob);
                }
                break;

                case G_DOG:
                {
                    Dog dog = new Dog();
                    dog.initialise(descriptor);
                    App.entityData.addEntity(dog);
                }
                break;

                case G_GREEN_BLOCK:
                {
                    GreenBlock greenBlock = new GreenBlock();
                    greenBlock.initialise(descriptor);
                    App.entityData.addEntity(greenBlock);
                }
                break;

                case G_SPINNING_BALL:
                {
                    SpinningBall spinningBall = new SpinningBall();
                    spinningBall.initialise(descriptor);
                    App.entityData.addEntity(spinningBall);
                }
                break;

                case G_STAIR_CLIMBER:
                {
                    StairClimber stairClimber = new StairClimber(GraphicID.G_STAIR_CLIMBER);
                    stairClimber.initialise(descriptor);
                    App.entityData.addEntity(stairClimber);
                }
                break;

                case G_STAR_SPINNER:
                {
                    StarSpinner starSpinner = new StarSpinner();
                    starSpinner.initialise(descriptor);
                    App.entityData.addEntity(starSpinner);
                }
                break;

                case G_TOPSPIN:
                {
                    TopSpin topSpin = new TopSpin();
                    topSpin.initialise(descriptor);
                    App.entityData.addEntity(topSpin);
                }
                break;

                case G_TWINKLES:
                {
                    Twinkle twinkle = new Twinkle();
                    twinkle.initialise(descriptor);
                    App.entityData.addEntity(twinkle);
                }
                break;

                default:
                    break;
            }
        }
    }

    /**
     * Set up the initial position for the alien specified
     * by graphicID.
     * Some aliens have limited areas of the map height they can
     * exist in, or must start at specified heights etc.
     *
     * @param graphicID The GraphicID of the alien.
     * @return SimpleVec2 holding X and Y.
     */
    private SimpleVec2 setInitialPosition(GraphicID graphicID)
    {
        SimpleVec2 initPos = new SimpleVec2();

        initPos.x = (int) (App.getPlayer().sprite.getX() / Gfx.getTileWidth());

        switch (graphicID)
        {
            case G_3BALLS_UFO:
            {
                initPos.y = MathUtils.random(6, 11);

                initPos.x += MathUtils.random(Gfx._GAME_SCENE_WIDTH);
                initPos.x += (int) (MathUtils.random(100) < 50 ?
                    (Gfx._GAME_SCENE_WIDTH * 2) : -(Gfx._GAME_SCENE_WIDTH * 2));
            }
            break;

            case G_TOPSPIN:
            case G_3LEGS_ALIEN:
            {
                initPos.y = MathUtils.random(10, 16);

                initPos.x += MathUtils.random(Gfx._GAME_SCENE_WIDTH);
                initPos.x += (int) (MathUtils.random(100) < 50 ?
                    (Gfx._GAME_SCENE_WIDTH * 2) : -(Gfx._GAME_SCENE_WIDTH * 2));
            }
            break;

            case G_BLOB:
            case G_SPINNING_BALL:
            {
                initPos.y = MathUtils.random(5, 12);

                initPos.x += MathUtils.random(Gfx._GAME_SCENE_WIDTH);
                initPos.x += (int) (MathUtils.random(100) < 50 ?
                    (Gfx._GAME_SCENE_WIDTH * 2) : -(Gfx._GAME_SCENE_WIDTH * 2));
            }
            break;

            case G_DOG:
            {
                initPos.y = MathUtils.random(6, 16);

                initPos.x += MathUtils.random(Gfx._GAME_SCENE_WIDTH);
                initPos.x += (int) (MathUtils.random(100) < 50 ?
                    (Gfx._GAME_SCENE_WIDTH * 2) : -(Gfx._GAME_SCENE_WIDTH * 2));
            }
            break;

            case G_TWINKLES:
            {
                initPos.y = MathUtils.random(3, 16);

                initPos.x += MathUtils.random(Gfx._GAME_SCENE_WIDTH);
                initPos.x += (int) (MathUtils.random(100) < 50 ?
                    (Gfx._GAME_SCENE_WIDTH * 2) : -(Gfx._GAME_SCENE_WIDTH * 2));
            }
            break;

            case G_ASTEROID:
            {
                initPos.y = ((Gfx._VIEW_HEIGHT / Gfx.getTileHeight()) / 2) + MathUtils.random(4);

                initPos.x += ((MathUtils.random(100) < 50) ?
                    (Gfx._VIEW_WIDTH / Gfx.getTileWidth()) + 10 :
                    -((Gfx._VIEW_WIDTH / Gfx.getTileWidth()) + 10));
                initPos.y += (Gfx._VIEW_HEIGHT / Gfx.getTileHeight());
            }
            break;

            case G_GREEN_BLOCK:
            {
                initPos.y = MathUtils.random(4, 16);

                initPos.x += MathUtils.random(Gfx._GAME_SCENE_WIDTH);
                initPos.x += (int) (MathUtils.random(100) < 50 ?
                    (Gfx._GAME_SCENE_WIDTH * 2) : -(Gfx._GAME_SCENE_WIDTH * 2));
            }
            break;

            case G_ALIEN_WHEEL:
            case G_STAR_SPINNER:
            {
                initPos.y = 3;

                initPos.x -= (Gfx._VIEW_WIDTH / Gfx.getTileWidth());
                initPos.x -= MathUtils.random(10);
            }
            break;

            case G_STAIR_CLIMBER:
            {
                initPos.y = MathUtils.random(3, 5);

                initPos.x += (int) ((MathUtils.random(100) < 50) ?
                    (Gfx._GAME_SCENE_WIDTH * 2) : -(Gfx._GAME_SCENE_WIDTH * 2));
            }
            break;

            default:
            {
                Trace.__FILE_FUNC("WARNING: Unknown Alien Entity type: " + graphicID);
            }
            break;
        }

        return initPos;
    }

    @Override
    public GraphicID getGID()
    {
        return GraphicID._ALIEN_MANAGER;
    }
}

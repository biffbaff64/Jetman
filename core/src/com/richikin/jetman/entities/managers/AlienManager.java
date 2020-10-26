package com.richikin.jetman.entities.managers;

import com.badlogic.gdx.math.MathUtils;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.Entities;
import com.richikin.jetman.entities.characters.*;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.graphics.GraphicIndex;
import com.richikin.utilslib.maths.SimpleVec2;

import java.sql.Blob;

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

    public AlienManager(final App _app)
    {
        super(_app);
    }

    @Override
    public void init()
    {
        for (GraphicIndex item : aliens)
        {
            item.value = 0;
        }
    }

    @Override
    public void free(final GraphicID _gid)
    {
        for (GraphicIndex item : aliens)
        {
            if (item.graphicID == _gid)
            {
                item.value = Math.max(0, item.value - 1);
            }
        }
    }

    @Override
    public void update()
    {
        for (GraphicIndex alien : aliens)
        {
            if (alien.value < app.roomManager.getMaxAllowed(alien.graphicID))
            {
                create(alien.graphicID);
                alien.value++;
            }
        }
    }

    public void create(GraphicID graphicID)
    {
        if (app.entityUtils.canUpdate(graphicID))
        {
            SimpleVec2 markerPos = setInitialPosition(graphicID);

            descriptor             = Entities.getDescriptor(graphicID);
            descriptor._SIZE       = GameAssets.getAssetSize(graphicID);
            descriptor._POSITION.x = markerPos.getX();
            descriptor._POSITION.y = markerPos.getY();
            descriptor._POSITION.z = app.entityUtils.getInitialZPosition(graphicID);
            descriptor._INDEX      = app.entityData.entityMap.size;

            switch (graphicID)
            {
                case G_3BALLS_UFO:
                {
//                    ThreeBallsUFO threeBallsUFO = new ThreeBallsUFO(app);
//                    threeBallsUFO.initialise(descriptor);
//                    app.entityData.addEntity(threeBallsUFO);
                }
                break;

                case G_3LEGS_ALIEN:
                {
                    ThreeLegsAlien threeLegsAlien = new ThreeLegsAlien(app);
                    threeLegsAlien.initialise(descriptor);
                    app.entityData.addEntity(threeLegsAlien);
                }
                break;

                case G_ASTEROID:
                {
                    Asteroid asteroid = new Asteroid(app);
                    asteroid.initialise(descriptor);
                    app.entityData.addEntity(asteroid);
                }
                break;

                case G_ALIEN_WHEEL:
                {
                    AlienWheel alienWheel = new AlienWheel(app);
                    alienWheel.initialise(descriptor);
                    app.entityData.addEntity(alienWheel);
                }
                break;

                case G_BLOB:
                {
//                    Blob blob = new Blob(app);
//                    blob.initialise(descriptor);
//                    app.entityData.addEntity(blob);
                }
                break;

                case G_DOG:
                {
//                    Dog dog = new Dog(app);
//                    dog.initialise(descriptor);
//                    app.entityData.addEntity(dog);
                }
                break;

                case G_GREEN_BLOCK:
                {
                    GreenBlock greenBlock = new GreenBlock(app);
                    greenBlock.initialise(descriptor);
                    app.entityData.addEntity(greenBlock);
                }
                break;

                case G_SPINNING_BALL:
                {
//                    SpinningBall spinningBall = new SpinningBall(app);
//                    spinningBall.initialise(descriptor);
//                    app.entityData.addEntity(spinningBall);;
                }
                break;

                case G_STAIR_CLIMBER:
                {
                    StairClimber stairClimber = new StairClimber(GraphicID.G_STAIR_CLIMBER, app);
                    stairClimber.initialise(descriptor);
                    app.entityData.addEntity(stairClimber);
                }
                break;

                case G_STAR_SPINNER:
                {
                    StarSpinner starSpinner = new StarSpinner(app);
                    starSpinner.initialise(descriptor);
                    app.entityData.addEntity(starSpinner);
                }
                break;

                case G_TOPSPIN:
                {
//                    TopSpin topSpin = new TopSpin(app);
//                    topSpin.initialise(descriptor);
//                    app.entityData.addEntity(topSpin);
                }
                break;

                case G_TWINKLES:
                {
                    Twinkle twinkle = new Twinkle(app);
                    twinkle.initialise(descriptor);
                    app.entityData.addEntity(twinkle);
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

        switch (graphicID)
        {
            case G_3BALLS_UFO:
            {
                initPos.x = (int) (app.getPlayer().sprite.getX() / Gfx.getTileWidth());
                initPos.y = MathUtils.random(6, 11);

                initPos.x += MathUtils.random(Gfx._GAME_SCENE_WIDTH);
                initPos.x += (int) (MathUtils.random(100) < 50 ?
                    (Gfx._GAME_SCENE_WIDTH * 2) : -(Gfx._GAME_SCENE_WIDTH * 2));
            }
            break;

            case G_3LEGS_ALIEN:
            {
                initPos.x = (int) (app.getPlayer().sprite.getX() / Gfx.getTileWidth());
                initPos.y = MathUtils.random(10, 16);

                initPos.x += MathUtils.random(Gfx._GAME_SCENE_WIDTH);
                initPos.x += (int) (MathUtils.random(100) < 50 ?
                    (Gfx._GAME_SCENE_WIDTH * 2) : -(Gfx._GAME_SCENE_WIDTH * 2));
            }
            break;

            case G_BLOB:
            {
                initPos.x = (int) (app.getPlayer().sprite.getX() / Gfx.getTileWidth());
                initPos.y = MathUtils.random(5, 12);

                initPos.x += MathUtils.random(Gfx._GAME_SCENE_WIDTH);
                initPos.x += (int) (MathUtils.random(100) < 50 ?
                    (Gfx._GAME_SCENE_WIDTH * 2) : -(Gfx._GAME_SCENE_WIDTH * 2));
            }
            break;

            case G_DOG:
            {
                initPos.x = (int) (app.getPlayer().sprite.getX() / Gfx.getTileWidth());
                initPos.y = MathUtils.random(6, 16);

                initPos.x += MathUtils.random(Gfx._GAME_SCENE_WIDTH);
                initPos.x += (int) (MathUtils.random(100) < 50 ?
                    (Gfx._GAME_SCENE_WIDTH * 2) : -(Gfx._GAME_SCENE_WIDTH * 2));
            }
            break;

            case G_TOPSPIN:
            {
                initPos.x = (int) (app.getPlayer().sprite.getX() / Gfx.getTileWidth());
                initPos.y = MathUtils.random(10, 16);

                initPos.x += MathUtils.random(Gfx._GAME_SCENE_WIDTH);
                initPos.x += (int) (MathUtils.random(100) < 50 ?
                    (Gfx._GAME_SCENE_WIDTH * 2) : -(Gfx._GAME_SCENE_WIDTH * 2));
            }
            break;

            case G_TWINKLES:
            {
                initPos.x = (int) (app.getPlayer().sprite.getX() / Gfx.getTileWidth());
                initPos.y = MathUtils.random(3, 16);

                initPos.x += MathUtils.random(Gfx._GAME_SCENE_WIDTH);
                initPos.x += (int) (MathUtils.random(100) < 50 ?
                    (Gfx._GAME_SCENE_WIDTH * 2) : -(Gfx._GAME_SCENE_WIDTH * 2));
            }
            break;

            case G_SPINNING_BALL:
            {
                initPos.x = (int) (app.getPlayer().sprite.getX() / Gfx.getTileWidth());
                initPos.y = MathUtils.random(6, 12);

                initPos.x += MathUtils.random(Gfx._GAME_SCENE_WIDTH);
                initPos.x += (int) (MathUtils.random(100) < 50 ?
                    (Gfx._GAME_SCENE_WIDTH * 2) : -(Gfx._GAME_SCENE_WIDTH * 2));
            }
            break;

            case G_ASTEROID:
            {
                initPos.x = (int) (app.getPlayer().sprite.getX() / Gfx.getTileWidth());
                initPos.y = ((Gfx._VIEW_HEIGHT / Gfx.getTileHeight()) / 2) + MathUtils.random(4);

                initPos.x += ((MathUtils.random(100) < 50) ?
                    (Gfx._VIEW_WIDTH / Gfx.getTileWidth()) + 10 :
                    -((Gfx._VIEW_WIDTH / Gfx.getTileWidth()) + 10));
                initPos.y += (Gfx._VIEW_HEIGHT / Gfx.getTileHeight());
            }
            break;

            case G_GREEN_BLOCK:
            {
                initPos.x = (int) (app.getPlayer().sprite.getX() / Gfx.getTileWidth());
                initPos.y = MathUtils.random(4, 16);

                initPos.x += MathUtils.random(Gfx._GAME_SCENE_WIDTH);
                initPos.x += (int) (MathUtils.random(100) < 50 ?
                    (Gfx._GAME_SCENE_WIDTH * 2) : -(Gfx._GAME_SCENE_WIDTH * 2));
            }
            break;

            case G_ALIEN_WHEEL:
            {
                initPos.x = (int) (app.getPlayer().sprite.getX() / Gfx.getTileWidth());
                initPos.y = 3;

                initPos.x -= (Gfx._VIEW_WIDTH / Gfx.getTileWidth());
                initPos.x -= MathUtils.random(10);
            }
            break;

            case G_STAIR_CLIMBER:
            {
                initPos.x = (int) (app.getPlayer().sprite.getX() / Gfx.getTileWidth());
                initPos.y = MathUtils.random(3, 5);

                initPos.x += (int) ((MathUtils.random(100) < 50) ?
                    (Gfx._GAME_SCENE_WIDTH * 2) : -(Gfx._GAME_SCENE_WIDTH * 2));
            }
            break;

            case G_STAR_SPINNER:
            {
                initPos.x = (int) (app.getPlayer().sprite.getX() / Gfx.getTileWidth());
                initPos.y = MathUtils.random(3, 5);

                initPos.x += MathUtils.random(Gfx._GAME_SCENE_WIDTH);
                initPos.x += (int) ((MathUtils.random(100) < 50) ?
                    (Gfx._GAME_SCENE_WIDTH * 2) : -(Gfx._GAME_SCENE_WIDTH * 2));
            }
            break;

            default:
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

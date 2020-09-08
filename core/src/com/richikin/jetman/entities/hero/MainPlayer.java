package com.richikin.jetman.entities.hero;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.core.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.core.GameConstants;
import com.richikin.jetman.core.StateID;
import com.richikin.jetman.entities.GdxSprite;
import com.richikin.jetman.entities.managers.BridgeManager;
import com.richikin.jetman.entities.objects.EntityDescriptor;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.graphics.parallax.ParallaxLayer;
import com.richikin.jetman.physics.Movement;
import com.richikin.jetman.utils.logging.Meters;
import com.richikin.jetman.utils.logging.Stats;
import com.richikin.jetman.utils.logging.Trace;

import java.util.concurrent.TimeUnit;

public class MainPlayer extends GdxSprite
{
    public static final float _PLAYER_X_SPEED      = 3.0f;
    public static final float _PLAYER_JUMP_X_SPEED = 16.0f;
    public static final float _PLAYER_Y_SPEED      = 4.0f;

    private static final int   _SPAWN_FRAMES        = 20;
    private static final int   _PLAYER_APPEAR_FRAME = 13;

    public boolean isRidingRover;
    public boolean isOnRoverBack;
    public boolean isHurting;
    public boolean isMovingX;
    public boolean isMovingY;
    public boolean isShooting;
    public boolean localIsDrawable;
    public boolean isOnGround;
    public boolean isInMidAir;
    public boolean isFalling;
    public boolean isCarrying;
    public boolean isJumpingCrater;
    public boolean isTeleporting;
    public boolean isBlockedLeft;
    public boolean isBlockedRight;

    public float    shootRate;
    public int      shootCount;
    public float    maxMoveSpeed;

    private TextureRegion bridgeSection;
    private int           laserColour;

    protected ButtonInputHandler  buttons;
    protected CollisionHandler    collision;
    protected ActionButtonHandler actionButton;
    protected TeleportHandler     teleport;
    protected LaserManager        laserManager;
    protected BridgeManager       bridgeManager;

    public MainPlayer(App _app)
    {
        super(GraphicID.G_PLAYER, _app);
    }

    @Override
    public void initialise(EntityDescriptor descriptor)
    {
        create(descriptor);

        bodyCategory = Gfx.CAT_PLAYER;
        collidesWith = Gfx.CAT_MOBILE_ENEMY
            | Gfx.CAT_FIXED_ENEMY
            | Gfx.CAT_GROUND
            | Gfx.CAT_SCENERY;

        isMainCharacter = true;

        createPartners();

        initXYZ.set(sprite.getX(), sprite.getY(), zPosition);
        app.mapData.checkPoint.set(sprite.getX(), sprite.getY());

        setup(true);

        bridgeSection = app.assets.getObjectRegion("bridge");
    }

    /**
     * Set up the player, ready for play. This method is separate from initialise()
     * because it does not create objects, and is called on initialisation and also
     * when restarting after losing a life.
     */
    public void setup(boolean _spawning)
    {
        direction.set(Movement._DIRECTION_RIGHT, Movement._DIRECTION_STILL);
        lookingAt.set(direction);

        maxMoveSpeed = _PLAYER_X_SPEED;

        isMovingX       = false;
        isMovingY       = false;
        isRotating      = false;
        isFlippedX      = false;
        isHurting       = false;
        isDrawable      = true;
        isShooting      = false;
        localIsDrawable = false;
        isCarrying      = false;
        isJumpingCrater = false;
        isRidingRover   = false;
        isOnRoverBack   = false;
        isOnGround      = false;
        isBlockedLeft   = false;
        isBlockedRight  = false;

        sprite.setRotation(0);
        sprite.setScale(1.0f);

        strength    = GameConstants._MAX_STRENGTH;
        shootCount  = 0;
        laserColour = 0;

        setAction(Actions._STANDING);
    }

    @Override
    public void update(int spriteNum)
    {
        if (app.appState.peek() == StateID._STATE_PAUSED)
        {
            setAction(Actions._PAUSED);
        }

        if (isRidingRover)
        {
            app.getRover().playerControl();
        }
        else
        {
            if (getSpriteAction() == Actions._TELEPORTING)
            {
                if (teleport.update())
                {
                    teleport.end();
                }
            }
            else
            {
                updateMainPlayer();
            }
        }

        animate();

        updateCommon();
    }

    private void updateMainPlayer()
    {
        switch (getSpriteAction())
        {
            case _TELEPORTING:
            case _RIDING:
            case _DYING:
            case _EXPLODING:
            case _RESETTING:
            case _RESTARTING:
            case _WAITING:
            case _DEAD:
            case _PAUSED:
            case _KILLED:
            {
            }
            break;

            case _SPAWNING:
            {
//                elapsedSpawnTime += Gdx.graphics.getDeltaTime();

//                if (spawnAnim.isAnimationFinished(elapsedSpawnTime))
//                {
//                    setAction(Actions._STANDING);

//                    spawnAnim = null;
//                    spawnFrames = null;
//                }
//                else
//                {
//                    localIsDrawable = (spawnAnim.getKeyFrameIndex(elapsedSpawnTime) >= _PLAYER_APPEAR_FRAME);
//                }

                setAction(Actions._STANDING);
                localIsDrawable = true;
            }
            break;

            case _STANDING:
            {
                buttons.checkButtons();

                if (getSpriteAction() == Actions._STANDING)
                {
                    movePlayer();
                }
            }
            break;

            case _HOVERING:
            case _RUNNING:
            case _FLYING:
            case _FALLING:
            {
                buttons.checkButtons();

                movePlayer();
            }
            break;

            // Fall to the ground after being killed while flying
            case _FALLING_TO_GROUND:
            {
//                if (app.collisionUtils.getBoxHittingBottom(this).gid == GraphicID._GROUND)
//                {
//                    explode();
//
//                    isRotating = false;
//                    rotateSpeed = 0;
//                }
//                else
//                {
//                    sprite.translate(0, (speed.getY() * Movement._DIRECTION_DOWN));
//                    speed.addY(0.2f);
//                }
            }
            break;

            default:
            {
                Trace.__FILE_FUNC_WithDivider();
                Trace.dbg("Unsupported player action: " + getSpriteAction());

                Stats.incMeter(Meters._BAD_PLAYER_ACTION.get());
            }
            break;
        }

        checkForFuelRefill();

        if (isShooting)
        {
            shootRate += Gdx.graphics.getDeltaTime();

            if (isShooting && (shootRate > 0.0875f) && (shootCount < 5))
            {
                laserManager.createLaser(this);

                if (++laserColour >= 8)
                {
                    laserColour = 0;
                }

                if (++shootCount >= 5)
                {
                    isShooting = false;
                }

                shootRate = 0;
            }
        }
    }

    @Override
    public void animate()
    {
        switch (getSpriteAction())
        {
            case _TELEPORTING:
            case _RIDING:
            case _EXPLODING:
            {
            }
            break;

            default:
            {
                sprite.setRegion(app.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
                elapsedAnimTime += Gdx.graphics.getDeltaTime();
            }
            break;
        }
    }

    @Override
    public void setAction(Actions action)
    {
        if (getSpriteAction() != action)
        {
            switch (action)
            {
                //
                // These cases do NOT need an
                // animation setting.
                case _TELEPORTING:
                case _RIDING:
                case _EXPLODING:
                case _DEAD:
                case _SPAWNING:
                {
                }
                break;

                case _LAST_RITES:
                case _FALLING_TO_GROUND:
                case _DYING:
                {
                    setAnimation
                        (
                            new EntityDescriptor
                                (
                                    app.assets.getAnimationRegion(GameAssets._PLAYER_DYING),
                                    GameAssets._PLAYER_DYING_FRAMES,
                                    Animation.PlayMode.LOOP
                                ),
                            1.0f
                        );

                    sprite.setScale(1.2f);

                    elapsedAnimTime = 0;
                }
                break;

                case _FLYING:
                case _FALLING:
                case _HOVERING:
                {
                    setAnimation
                        (
                            new EntityDescriptor
                                (
                                    app.assets.getAnimationRegion(GameAssets._PLAYER_FLY),
                                    GameAssets._PLAYER_FLY_FRAMES,
                                    Animation.PlayMode.LOOP
                                ),
                            1.25f
                        );

                    elapsedAnimTime = 0;
                }
                break;

                case _RUNNING:
                {
                    setAnimation
                        (
                            new EntityDescriptor
                                (
                                    app.assets.getAnimationRegion(GameAssets._PLAYER_RUN),
                                    GameAssets._PLAYER_RUN_FRAMES,
                                    Animation.PlayMode.LOOP
                                ),
                            0.5f
                        );

                    elapsedAnimTime = 0;
                }
                break;

                case _PAUSED:
                case _WAITING:
                case _STANDING:
                default:
                {
                    setAnimation
                        (
                            new EntityDescriptor
                                (
                                    app.assets.getAnimationRegion(GameAssets._PLAYER_IDLE),
                                    GameAssets._PLAYER_STAND_FRAMES,
                                    Animation.PlayMode.LOOP
                                ),
                            1.0f
                        );

                    elapsedAnimTime = 0;
                    isTeleporting   = false;
                    isRidingRover   = false;
                }
                break;
            }

            super.setAction(action);
        }
    }

    public void kill()
    {
    }

    /**
     * Handles the player movement.
     */
    private void movePlayer()
    {
        if (isMovingY)
        {
            if ((direction.getY() == Movement._DIRECTION_UP)
                && ((getPosition().y + frameHeight) > (Gfx._VIEW_HEIGHT - GameAssets.hudPanelHeight)))
            {
                speed.setY(0);
            }
        }

        if (getSpriteAction() == Actions._FALLING)
        {
            sprite.translate
                (
                    (speed.getX() * app.inputManager.getControllerXPercentage()),
                    (speed.getY() * direction.getY())
                );
            speed.y += 0.025f;
        }
        else
        {
            if (isMovingX || isMovingY)
            {
                float layerSpeed = speed.getX() + 1;

                for (ParallaxLayer layer : app.baseRenderer.parallaxForeground.layers)
                {
                    layer.xSpeed = layerSpeed++;
                }

                sprite.translate
                    (
                        (speed.getX() * app.inputManager.getControllerXPercentage()),
                        (speed.getY() * app.inputManager.getControllerYPercentage())
                    );
            }

            if (sprite.getX() > Gfx.visibleMapRight())
            {
                sprite.translateX(-(Gfx.visibleMapRight() - Gfx._VIEW_WIDTH));
            }
            else if ((sprite.getX() + frameWidth) < Gfx._VIEW_WIDTH)
            {
                sprite.translateX(Gfx.visibleMapRight() - Gfx._VIEW_WIDTH);
            }
        }
    }

    /**
     * Standing in front of the Rover, and in-between the
     * wheels, refills LJMs jetpack fuel tank.
     */
    private void checkForFuelRefill()
    {
        if (!app.getHud().getFuelBar().isFull() && collision.isInRoverMiddle())
        {
            app.getHud().getFuelBar().refill();
        }
    }

    private void createPartners()
    {
        buttons       = new ButtonInputHandler(app);
        collision     = new CollisionHandler(app);
        actionButton  = new ActionButtonHandler(app);
        teleport      = new TeleportHandler(app);
        laserManager  = new LaserManager(app);
        bridgeManager = new BridgeManager(app);
    }
}

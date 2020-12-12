package com.richikin.jetman.entities.hero;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.enumslib.ActionStates;
import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.core.App;
import com.richikin.jetman.core.GameConstants;
import com.richikin.enumslib.StateID;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.entities.managers.BridgeManager;
import com.richikin.jetman.entities.managers.ExplosionManager;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.enumslib.GraphicID;
import com.richikin.utilslib.input.controllers.ControllerType;
import com.richikin.utilslib.maths.Box;
import com.richikin.jetman.physics.aabb.CollisionRect;
import com.richikin.utilslib.physics.Movement;
import com.richikin.jetman.developer.Developer;
import com.richikin.utilslib.logging.Meters;
import com.richikin.utilslib.logging.Stats;
import com.richikin.utilslib.logging.StopWatch;
import com.richikin.utilslib.logging.Trace;

public class MainPlayer extends GdxSprite
{
    public static final float _PLAYER_X_SPEED      = 6.0f;
    public static final float _PLAYER_JUMP_X_SPEED = 16.0f;
    public static final float _PLAYER_Y_SPEED      = 4.0f;

    private static final int _SPAWN_FRAMES        = 20;
    private static final int _PLAYER_APPEAR_FRAME = 13;

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

    public float rightEdge;
    public float topEdge;
    public float shootRate;
    public int   shootCount;
    public float maxMoveSpeed;
    public Box   viewBox;

    public ButtonInputHandler  buttons;
    public CollisionHandler    collision;
    public ActionButtonHandler actionButton;
    public TeleportHandler     teleport;
    public LaserManager        laserManager;
    public BridgeManager       bridgeManager;
    public CollisionRect       tileRectangle;

    private TextureRegion            bridgeSection;
    public  int                      laserColour;
    private TextureRegion[]          spawnFrames;
    private Animation<TextureRegion> spawnAnim;
    private float                    elapsedSpawnTime;
    private StopWatch                stopWatch;

    public MainPlayer()
    {
        super(GraphicID.G_PLAYER);
    }

    @Override
    public void initialise(SpriteDescriptor _descriptor)
    {
        create(_descriptor);

        bodyCategory = Gfx.CAT_PLAYER;
        collidesWith = Gfx.CAT_MOBILE_ENEMY
                    | Gfx.CAT_FIXED_ENEMY
                    | Gfx.CAT_GROUND
                    | Gfx.CAT_CEILING;

        isMainCharacter = true;
        stopWatch       = StopWatch.start();

        createPartners();

        initXYZ.set(sprite.getX(), sprite.getY(), zPosition);
        App.mapData.checkPoint.set(sprite.getX(), sprite.getY());

        setup(true);

        bridgeSection = App.assets.getObjectRegion("bridge");
        tileRectangle = new CollisionRect(this.gid);
        viewBox       = new Box();
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

        actionButton.removeAction();
        setAction(ActionStates._SPAWNING);

        createSpawnAnimation();
    }

    @Override
    public void preUpdate()
    {
        if (getAction() == ActionStates._RESTARTING)
        {
            sprite.setPosition(App.mapData.checkPoint.getX(), App.mapData.checkPoint.getY());

            setAction(ActionStates._SPAWNING);
        }

        super.preUpdate();
    }

    @Override
    public void update(int spriteNum)
    {
        if (App.appState.peek() == StateID._STATE_PAUSED)
        {
            setAction(ActionStates._PAUSED);
        }

        if (isRidingRover)
        {
            App.getRover().playerControl();
        }
        else
        {
            if (getAction() == ActionStates._TELEPORTING)
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

        actionButton.update();
    }

    private void updateMainPlayer()
    {
        switch (getAction())
        {
            case _TELEPORTING:
            case _RIDING:
            case _DYING:
            case _HURT:
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
                elapsedSpawnTime += Gdx.graphics.getDeltaTime();

                if (spawnAnim.isAnimationFinished(elapsedSpawnTime))
                {
                    setAction(ActionStates._STANDING);

                    spawnAnim   = null;
                    spawnFrames = null;
                }
                else
                {
                    localIsDrawable = (spawnAnim.getKeyFrameIndex(elapsedSpawnTime) >= _PLAYER_APPEAR_FRAME);
                }
            }
            break;

            case _STANDING:
            {
                buttons.checkButtons();

                if (getAction() == ActionStates._STANDING)
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
                if (App.collisionUtils.getBoxHittingBottom(this).gid == GraphicID._GROUND)
                {
                    Trace.__FILE_FUNC_LINE();

                    explode();

                    isRotating  = false;
                    rotateSpeed = 0;
                }
                else
                {
                    sprite.translate(0, (speed.getY() * Movement._DIRECTION_DOWN));
                    speed.y += 0.2f;
                }
            }
            break;

            default:
            {
                Trace.__FILE_FUNC_WithDivider();
                Trace.dbg("Unsupported player action: " + getAction());

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
    public void postUpdate(int spriteNum)
    {
        switch (getAction())
        {
            case _PAUSED:
            case _TELEPORTING:
            case _RIDING:
            case _EXPLODING:
            case _FALLING_TO_GROUND:
            case _DYING:
            case _RESETTING:
            case _DEAD:
            {
            }
            break;

            case _FLYING:
            {
                if (App.getHud().getFuelBar().isEmpty())
                {
                    setAction(ActionStates._FALLING_TO_GROUND);

                    speed.setY(2.0f);
                    isRotating      = true;
                    rotateSpeed     = 6.0f;
                    elapsedAnimTime = 0;
                    stopWatch.reset();
                }
            }
            break;

            default:
            {
//                if (strength <= 0)
//                {
//                    Trace.__FILE_FUNC_LINE();
//
//                    explode();
//
//                    stopWatch.reset();
//                }
            }
            break;
        }
    }

    public void handleDying()
    {
        if (!Developer.isGodMode())
        {
            if (App.gameProgress.playerLifeOver)
            {
                App.gameProgress.getLives().setToMinimum();
            }
            else
            {
                App.gameProgress.getLives().subtract(1);
            }
        }

        // Restart if this player has more lives left...
        if (App.gameProgress.getLives().getTotal() > 0)
        {
            actionButton.removeAction();
            setAction(ActionStates._RESETTING);
            isDrawable = false;

            App.gameProgress.isRestarting   = true;
            App.gameProgress.playerGameOver = false;

            App.mapData.checkPoint.set(sprite.getX(), sprite.getY());
        }
        else
        {
            setAction(ActionStates._DEAD);

            App.gameProgress.isRestarting   = false;
            App.gameProgress.playerGameOver = true;

            if (App.gameProgress.playerLifeOver)
            {
                App.gameProgress.getLives().setToMinimum();
            }
        }
    }

    @Override
    public void animate()
    {
        switch (getAction())
        {
            case _TELEPORTING:
            case _RIDING:
            case _EXPLODING:
            {
            }
            break;

            default:
            {
                sprite.setRegion(App.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
                elapsedAnimTime += Gdx.graphics.getDeltaTime();
            }
            break;
        }
    }

    @Override
    public void setAction(ActionStates action)
    {
        if (getAction() != action)
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
                    SpriteDescriptor descriptor = App.entities.getDescriptor(this.gid);

                    descriptor._ASSET    = GameAssets._PLAYER_DYING;
                    descriptor._FRAMES   = GameAssets._PLAYER_DYING_FRAMES;
                    descriptor._PLAYMODE = Animation.PlayMode.LOOP;

                    setAnimation(descriptor, 1.0f);
                    sprite.setScale(1.2f);

                    elapsedAnimTime = 0;
                }
                break;

                case _FLYING:
                case _FALLING:
                case _HOVERING:
                {
                    SpriteDescriptor descriptor = App.entities.getDescriptor(this.gid);

                    descriptor._ASSET    = GameAssets._PLAYER_FLY;
                    descriptor._FRAMES   = GameAssets._PLAYER_FLY_FRAMES;
                    descriptor._PLAYMODE = Animation.PlayMode.LOOP;

                    setAnimation(descriptor, 1.25f);

                    elapsedAnimTime = 0;
                }
                break;

                case _RUNNING:
                {
                    SpriteDescriptor descriptor = App.entities.getDescriptor(this.gid);

                    descriptor._ASSET    = GameAssets._PLAYER_RUN;
                    descriptor._FRAMES   = GameAssets._PLAYER_RUN_FRAMES;
                    descriptor._PLAYMODE = Animation.PlayMode.LOOP;

                    setAnimation(descriptor, 0.5f);

                    elapsedAnimTime = 0;
                }
                break;

                case _PAUSED:
                case _WAITING:
                case _STANDING:
                default:
                {
                    SpriteDescriptor descriptor = App.entities.getDescriptor(this.gid);

                    descriptor._ASSET    = GameAssets._PLAYER_IDLE;
                    descriptor._FRAMES   = GameAssets._PLAYER_STAND_FRAMES;
                    descriptor._PLAYMODE = Animation.PlayMode.LOOP;

                    setAnimation(descriptor, 1.0f);

                    elapsedAnimTime = 0;
                    isTeleporting   = false;
                    isRidingRover   = false;
                }
                break;
            }

            super.setAction(action);
        }
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        if (!isTeleporting && !isRidingRover && !App.teleportManager.teleportActive)
        {
            sprite.setFlip(isFlippedX, false);

            if (actionButton.getActionMode() == ActionButtonHandler.ActionMode._BRIDGE_CARRY)
            {
                spriteBatch.draw(bridgeSection, sprite.getX(), sprite.getY() + (Gfx.getTileHeight() / 2.0f));
            }

            if (localIsDrawable)
            {
                super.draw(spriteBatch);
            }

            if (getAction() == ActionStates._SPAWNING)
            {
                spriteBatch.draw
                    (
                        App.entityUtils.getKeyFrame(spawnAnim, elapsedSpawnTime, false),
                        this.sprite.getX(),
                        this.sprite.getY()
                    );
            }
        }
    }

    @Override
    public void updateCollisionBox()
    {
        if (isRidingRover)
        {
            collisionObject.rectangle.x      = -1;
            collisionObject.rectangle.y      = -1;
            collisionObject.rectangle.width  = 1;
            collisionObject.rectangle.height = 1;
        }
        else
        {
            collisionObject.rectangle.x      = (sprite.getX() + ((float) frameWidth / 4));
            collisionObject.rectangle.y      = sprite.getY();
            collisionObject.rectangle.width  = ((float) frameWidth / 2);
            collisionObject.rectangle.height = frameHeight;
        }

        viewBox.x      = (int) ((sprite.getX() - Gfx._VIEW_HALF_WIDTH) + (frameWidth / 2));
        viewBox.y      = (int) sprite.getY() - Gfx._VIEW_HALF_HEIGHT;
        viewBox.width  = Gfx._VIEW_WIDTH;
        viewBox.height = Gfx._VIEW_HEIGHT;

        if (viewBox.y < 0)
        {
            viewBox.y += (Math.abs(viewBox.y));
        }

        tileRectangle.x      = (((collisionObject.rectangle.x + ((float) frameWidth / 2)) / Gfx.getTileWidth()));
        tileRectangle.y      = ((collisionObject.rectangle.y - Gfx.getTileHeight()) / Gfx.getTileHeight());
        tileRectangle.width  = Gfx.getTileWidth();
        tileRectangle.height = Gfx.getTileHeight();

        rightEdge = sprite.getX() + frameWidth;
        topEdge   = sprite.getY() + frameHeight;
    }

    /**
     * Trigger the explosion effect for LJM
     */
    public void explode()
    {
        ExplosionManager explosionManager = new ExplosionManager();
        explosionManager.createExplosion(GraphicID.G_EXPLOSION128, this);
        setAction(ActionStates._EXPLODING);
        elapsedAnimTime = 0;
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

        if (getAction() == ActionStates._FALLING)
        {
            sprite.translate
                (
                    (speed.getX() * App.inputManager.getControllerXPercentage()),
                    (speed.getY() * direction.getY())
                );
            speed.y += 0.025f;
        }
        else
        {
            if (isMovingX || isMovingY)
            {
                float movementXSpeed = (speed.getX() * getXDirection());
                float movementYSpeed = (speed.getY() * getYDirection());

                App.baseRenderer.parallaxForeground.layers.get(0).xSpeed = speed.getX() + 0.5f;
                App.baseRenderer.parallaxForeground.layers.get(1).xSpeed = speed.getX() + 1.0f;

                sprite.translate(movementXSpeed, movementYSpeed);
            }

            if (sprite.getX() > Gfx.visibleMapRight())
            {
                sprite.translateX(-(Gfx.visibleMapRight() - Gfx._VIEW_HALF_WIDTH));

                App.entityManager.renderSystem.relocateSprites();
            }
            else if ((sprite.getX() + frameWidth) < Gfx._VIEW_HALF_WIDTH)
            {
                sprite.translateX(Gfx.visibleMapRight() - Gfx._VIEW_HALF_WIDTH);

                App.entityManager.renderSystem.relocateSprites();
            }
        }
    }

    private float getXDirection()
    {
        float dir = 0;

        if (AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
        {
            dir = App.inputManager.getControllerXPercentage();
        }
        else
        {
            if (AppConfig.availableInputs.contains(ControllerType._KEYBOARD, true))
            {
                dir = direction.getX();
            }
        }

        return dir;
    }

    private float getYDirection()
    {
        float dir = 0;

        if (AppConfig.availableInputs.contains(ControllerType._VIRTUAL, true))
        {
            dir = App.inputManager.getControllerYPercentage();
        }
        else
        {
            if (AppConfig.availableInputs.contains(ControllerType._KEYBOARD, true))
            {
                dir = direction.getY();
            }
        }

        return dir;
    }

    /**
     * Standing in front of the Rover, and in-between the
     * wheels, refills LJMs jetpack fuel tank.
     */
    private void checkForFuelRefill()
    {
        if (!App.getHud().getFuelBar().isFull() && collision.isInRoverMiddle())
        {
            App.getHud().getFuelBar().refill();
        }
    }

    private void createPartners()
    {
        buttons       = new ButtonInputHandler();
        collision     = new CollisionHandler();
        actionButton  = new ActionButtonHandler();
        teleport      = new TeleportHandler();
        laserManager  = new LaserManager();
        bridgeManager = new BridgeManager();
    }

    private void createSpawnAnimation()
    {
        //
        // Spawn frames are the same size as standard
        // LJM frames, so no need to modify frame sizes
        spawnFrames = new TextureRegion[_SPAWN_FRAMES];
        spawnAnim   = App.entityUtils.createAnimation
            (
                GameAssets._PLAYER_SPAWN,
                spawnFrames,
                _SPAWN_FRAMES,
                Animation.PlayMode.NORMAL
            );
        spawnAnim.setFrameDuration(0.3f / 6.0f);
        elapsedSpawnTime = 0.0f;
    }
}

package com.richikin.jetman.entities.hero;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.core.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.core.GameConstants;
import com.richikin.jetman.core.StateID;
import com.richikin.jetman.entities.Entities;
import com.richikin.jetman.entities.GdxSprite;
import com.richikin.jetman.entities.SpriteDescriptor;
import com.richikin.jetman.entities.managers.BridgeManager;
import com.richikin.jetman.entities.managers.ExplosionManager;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.maths.Box;
import com.richikin.jetman.physics.AABB.CollisionRect;
import com.richikin.jetman.physics.Movement;
import com.richikin.jetman.utils.developer.Developer;
import com.richikin.jetman.utils.logging.Meters;
import com.richikin.jetman.utils.logging.Stats;
import com.richikin.jetman.utils.logging.StopWatch;
import com.richikin.jetman.utils.logging.Trace;

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

    private SpriteDescriptor         descriptor;
    private TextureRegion            bridgeSection;
    private int                      laserColour;
    private TextureRegion[]          spawnFrames;
    private Animation<TextureRegion> spawnAnim;
    private float                    elapsedSpawnTime;
    private StopWatch                stopWatch;

    public ButtonInputHandler  buttons;
    public CollisionHandler    collision;
    public ActionButtonHandler actionButton;
    public TeleportHandler     teleport;
    public LaserManager        laserManager;
    public BridgeManager       bridgeManager;
    public CollisionRect       tileRectangle;

    public MainPlayer(App _app)
    {
        super(GraphicID.G_PLAYER, _app);
    }

    @Override
    public void initialise(SpriteDescriptor _descriptor)
    {
        this.descriptor = _descriptor;

        create(_descriptor);

        bodyCategory = Gfx.CAT_PLAYER;
        collidesWith = Gfx.CAT_MOBILE_ENEMY
            | Gfx.CAT_FIXED_ENEMY
            | Gfx.CAT_GROUND
            | Gfx.CAT_CEILING
            | Gfx.CAT_SCENERY;

        isMainCharacter = true;
        stopWatch       = StopWatch.start();

        createPartners();

        initXYZ.set(sprite.getX(), sprite.getY(), zPosition);
        app.mapData.checkPoint.set(sprite.getX(), sprite.getY());

        setup(true);

        bridgeSection = app.assets.getObjectRegion("bridge");
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

        setAction(Actions._SPAWNING);

        createSpawnAnimation();
    }

    @Override
    public void preUpdate()
    {
        if (getSpriteAction() == Actions._RESTARTING)
        {
            sprite.setPosition(app.mapData.checkPoint.getX(), app.mapData.checkPoint.getY());

            setAction(Actions._SPAWNING);
        }

        super.preUpdate();
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
                elapsedSpawnTime += Gdx.graphics.getDeltaTime();

                if (spawnAnim.isAnimationFinished(elapsedSpawnTime))
                {
                    setAction(Actions._STANDING);

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
                if (app.collisionUtils.getBoxHittingBottom(this).gid == GraphicID._GROUND)
                {
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
    public void postUpdate(int spriteNum)
    {
        switch (getSpriteAction())
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

            default:
            {
                if (((app.getRover() != null) && (app.getRover().getSpriteAction() == Actions._EXPLODING))
                    || ((strength <= 0) && isOnGround))
                {
                    explode();

                    stopWatch.reset();
                }
                else
                {
                    if (strength <= 0)
                    {
                        setAction(Actions._FALLING_TO_GROUND);

                        speed.setY(2.0f);
                        isRotating = true;
                        rotateSpeed = 6.0f;
                        elapsedAnimTime = 0;
                        stopWatch.reset();
                    }
                }
            }
            break;
        }
    }

    public void handleDying()
    {
        if (app.gameProgress.playerLifeOver)
        {
            app.gameProgress.lives.setToMinimum();
        }
        else
        {
            app.gameProgress.lives.subtract(1);
        }

        // Restart if this player has more lives left...
        if (app.gameProgress.lives.getTotal() > 0)
        {
            setAction(Actions._RESETTING);
            isDrawable = false;

            app.gameProgress.isRestarting   = true;
            app.gameProgress.playerGameOver = false;

            app.mapData.checkPoint.set(sprite.getX(), sprite.getY());
        }
        else
        {
            setAction(Actions._DEAD);

            app.gameProgress.isRestarting   = false;
            app.gameProgress.playerGameOver = true;

            if (app.gameProgress.playerLifeOver)
            {
                app.gameProgress.lives.setToMinimum();
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
                    SpriteDescriptor descriptor = Entities.getDescriptor(this.gid);

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
                    SpriteDescriptor descriptor = Entities.getDescriptor(this.gid);

                    descriptor._ASSET    = GameAssets._PLAYER_FLY;
                    descriptor._FRAMES   = GameAssets._PLAYER_FLY_FRAMES;
                    descriptor._PLAYMODE = Animation.PlayMode.LOOP;

                    setAnimation(descriptor, 1.25f);

                    elapsedAnimTime = 0;
                }
                break;

                case _RUNNING:
                {
                    SpriteDescriptor descriptor = Entities.getDescriptor(this.gid);

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
                    SpriteDescriptor descriptor = Entities.getDescriptor(this.gid);

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
        if (!isTeleporting && !isRidingRover && !app.teleportManager.teleportActive)
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

            if (getSpriteAction() == Actions._SPAWNING)
            {
                spriteBatch.draw
                    (
                        app.entityUtils.getKeyFrame(spawnAnim, elapsedSpawnTime, false),
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
            collisionObject.rectangle.x      = (sprite.getX() + (frameWidth / 4));
            collisionObject.rectangle.y      = sprite.getY();
            collisionObject.rectangle.width  = (frameWidth / 2);
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

        tileRectangle.x = (((collisionObject.rectangle.x + (frameWidth / 2)) / Gfx.getTileWidth()));
        tileRectangle.y = ((collisionObject.rectangle.y - Gfx.getTileHeight()) / Gfx.getTileHeight());
        tileRectangle.width = Gfx.getTileWidth();
        tileRectangle.height = Gfx.getTileHeight();

        rightEdge = sprite.getX() + frameWidth;
        topEdge   = sprite.getY() + frameHeight;
    }

    public void kill()
    {
        if (!Developer.isGodMode())
        {
            strength = 0;
        }
    }

    /**
     * Trigger the explosion effect for LJM
     */
    public void explode()
    {
        ExplosionManager explosionManager = new ExplosionManager();
        explosionManager.createExplosion(GraphicID.G_EXPLOSION128, this, app);
        setAction(Actions._EXPLODING);
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
            float movementXSpeed = 0.0f;
            float movementYSpeed = 0.0f;

            if (isMovingX || isMovingY)
            {
                movementXSpeed = (speed.getX() * app.inputManager.getControllerXPercentage());
                movementYSpeed = (speed.getY() * app.inputManager.getControllerYPercentage());

//                app.baseRenderer.parallaxForeground.layers.get(0).xSpeed = movementXSpeed + 0.0025f;
//                app.baseRenderer.parallaxForeground.layers.get(1).xSpeed = movementXSpeed + 0.1f;

                sprite.translate(movementXSpeed, movementYSpeed);
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

    private void createSpawnAnimation()
    {
        //
        // Spawn frames are the same size as standard
        // LJM frames, so no need to modify frame sizes
        spawnFrames = new TextureRegion[_SPAWN_FRAMES];
        spawnAnim   = app.entityUtils.createAnimation
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

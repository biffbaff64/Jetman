package com.richikin.jetman.entities.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.core.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.Entities;
import com.richikin.jetman.entities.GdxSprite;
import com.richikin.jetman.entities.SpriteDescriptor;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.physics.Movement;
import com.richikin.jetman.utils.logging.Trace;

public class Rover extends GdxSprite
{
    public boolean isMovingX;
    public boolean isMovingY;

    public GdxSprite roverBack;
    public GdxSprite frontWheel;
    public GdxSprite backWheel;

    private final App app;

    public Rover(App _app)
    {
        super(GraphicID.G_ROVER, _app);

        this.app = _app;

        bodyCategory = Gfx.CAT_VEHICLE;
        collidesWith = Gfx.CAT_SCENERY | Gfx.CAT_FIXED_ENEMY;
    }

    /**
     * Create and initialise the Moon Rover
     *
     * @param entityDescriptor  The {@link SpriteDescriptor} which
     *                          holds all the necessary information
     *                          for creating this entity.
     */
    @Override
    public void initialise(SpriteDescriptor entityDescriptor)
    {
        create(entityDescriptor);

        initXYZ.set(sprite.getX(), sprite.getY(), zPosition);

        setup();
    }

    /**
     * Part 2 of the initialisation process.
     * This is seperate from Initialise() as it
     * does not create objects and can be called
     * on level reset/retry.
     */
    public void setup()
    {
        setAction(Actions._STANDING);

        animation.setPlayMode(Animation.PlayMode.NORMAL);
        speed.set(1, 0);

        isMovingX = false;
        isMovingY = false;

        sprite.setPosition(initXYZ.getX(), initXYZ.getY());

        direction.setX(Movement._DIRECTION_RIGHT);
        lookingAt.setX(direction.getX());

        app.gameProgress.roverDestroyed = false;
    }

    /**
     * Add all required extra sprite objects, such as
     * the wheels and boot.
     */
    public void addPartners()
    {
        Trace.__FILE_FUNC();

        SpriteDescriptor descriptor = Entities.getDescriptor(GraphicID.G_ROVER_WHEEL);
        descriptor._SIZE          = GameAssets.getAssetSize(GraphicID.G_ROVER_WHEEL);
        descriptor._PLAYMODE      = Animation.PlayMode.NORMAL;
        descriptor._POSITION.x    = 0;
        descriptor._POSITION.y    = 0;
        descriptor._POSITION.z    = app.entityUtils.getInitialZPosition(GraphicID.G_ROVER_WHEEL);
        descriptor._INDEX         = app.entityData.entityMap.size;

        // Add the front wheel
        if (frontWheel == null)
        {
            Trace.__FILE_FUNC("Adding Rover Front Wheel.");

            frontWheel = new GdxSprite(GraphicID.G_ROVER_WHEEL, app);
            frontWheel.create(descriptor);
            app.entityData.addEntity(frontWheel);
        }

        // Add the back wheel
        if (backWheel == null)
        {
            Trace.__FILE_FUNC("Adding Rover Back Wheel.");

            descriptor._INDEX = app.entityData.entityMap.size;

            backWheel = new GdxSprite(GraphicID.G_ROVER_WHEEL, app);
            backWheel.create(descriptor);
            app.entityData.addEntity(backWheel);
        }

        // Add the back tray
        if (roverBack == null)
        {
            Trace.__FILE_FUNC("Adding Rover Boot.");

            descriptor = Entities.getDescriptor(GraphicID.G_ROVER_BOOT);
            descriptor._SIZE = GameAssets.getAssetSize(GraphicID.G_ROVER_BOOT);
            descriptor._POSITION.z = app.entityUtils.getInitialZPosition(GraphicID.G_ROVER_BOOT);
            descriptor._INDEX = app.entityData.entityMap.size;

            roverBack = new GdxSprite(GraphicID.G_ROVER_BOOT, app);
            roverBack.create(descriptor);
            roverBack.bodyCategory = Gfx.CAT_GROUND;
            roverBack.collidesWith = Gfx.CAT_PLAYER;
            app.entityData.addEntity(roverBack);
        }
    }

    @Override
    public void update(int spriteNum)
    {
        frontWheel.preUpdate();
        backWheel.preUpdate();
        roverBack.preUpdate();

        switch(getSpriteAction())
        {
            case _STANDING:
            {
                frontWheel.isRotating = false;
                backWheel.isRotating = false;

                speed.set(0, 0);
            }
            break;

            case _RUNNING:
            {
                frontWheel.isRotating = true;
                backWheel.isRotating = true;

                frontWheel.rotateSpeed = speed.getX() * (direction.getX() * -1);
                backWheel.rotateSpeed = speed.getX() * (direction.getX() * -1);

                sprite.translate(speed.getX() * direction.getX(), 0);
            }
            break;

            case _HURT:
            case _KILLED:
            {
//                ExplosionManager explosionManager = new ExplosionManager();
//                explosionManager.createExplosion(GraphicID.G_EXPLOSION256, this, app);
//                explosionManager.createExplosion(GraphicID.G_EXPLOSION128, roverBack, app);
//                explosionManager.createExplosion(GraphicID.G_EXPLOSION128, frontWheel, app);
//                explosionManager.createExplosion(GraphicID.G_EXPLOSION128, backWheel, app);
//
//                collisionObject.setInvisibility(1000);
//
//                spriteAction = Actions._EXPLODING;
            }
            break;

            case _EXPLODING:
            {
            }
            break;

            case _DYING:
            {
                setAction(Actions._DEAD);
                frontWheel.setAction(Actions._DEAD);
                backWheel.setAction(Actions._DEAD);
                roverBack.setAction(Actions._DEAD);

                app.gameProgress.playerLifeOver = true;
                app.gameProgress.roverDestroyed = true;
            }
            break;

            default:
            {
                Trace.__FILE_FUNC("Unsupported spriteAction: " + getSpriteAction());
            }
            break;
        }

        isFlippedX = (direction.getX() == Movement._DIRECTION_LEFT);

        animate();

        updateCommon();

        frontWheel.updateCommon();
        backWheel.updateCommon();
        roverBack.updateCommon();
    }

    @Override
    public void postUpdate(int spriteNum)
    {
        if (getSpriteAction() == Actions._DEAD)
        {
            app.gameProgress.lives.setToMinimum();
        }
    }

    @Override
    public void animate()
    {
        elapsedAnimTime += Gdx.graphics.getDeltaTime();
        sprite.setRegion(app.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
    }

//    @Override
//    public void updateCollisionBox()
//    {
//        collisionObject.rectangle.x = sprite.getX();
//        collisionObject.rectangle.y = sprite.getY() - 2;
//        collisionObject.rectangle.width = frameWidth;
//        collisionObject.rectangle.height = frameHeight;
//
//        frontWheel.updateCollisionBox();
//        backWheel.updateCollisionBox();
//        roverBack.updateCollisionBox();
//    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        super.draw(spriteBatch);

        boolean flipSetting = false;

        if (lookingAt.getX() == Movement._DIRECTION_RIGHT)
        {
            frontWheel.sprite.setPosition(sprite.getX() + 197, sprite.getY());
            backWheel.sprite.setPosition(sprite.getX() + 48, sprite.getY());
            roverBack.sprite.setPosition(sprite.getX() + 47, sprite.getY() + (frameHeight - 57));
        }
        else if (lookingAt.getX() == Movement._DIRECTION_LEFT)
        {
            frontWheel.sprite.setPosition(sprite.getX() + 40, sprite.getY());
            backWheel.sprite.setPosition(sprite.getX() + 189, sprite.getY());
            roverBack.sprite.setPosition(sprite.getX() + 148, sprite.getY() + (frameHeight - 57));

            flipSetting = true;
        }

        isFlippedX = flipSetting;
        frontWheel.isFlippedX = flipSetting;
        backWheel.isFlippedX = flipSetting;
        roverBack.isFlippedX = flipSetting;

        roverBack.draw(spriteBatch);
        frontWheel.draw(spriteBatch);
        backWheel.draw(spriteBatch);
    }

    public void playerControl()
    {
//        app.getPlayer().buttons.checkButtons();

        moveGunTurret();

        obstacleCheck();

        if (isMovingX && !checkForCrater())
        {
            speed.set(3, 0);
            setAction(Actions._RUNNING);

//            app.googleServices.unlockAchievement(PlayServicesID.achievement_moon_rider.getID());
        }
        else
        {
            speed.set(0, 0);
            setAction(Actions._STANDING);
        }
    }

    private boolean checkForCrater()
    {
//        int x;
//        int y;
//
//        if (lookingAt.getX() == Movement._DIRECTION_RIGHT)
//        {
//            x = (int) (collisionObject.rectangle.x + frameWidth);
//            y = ((int) collisionObject.rectangle.y / Gfx.getTileHeight()) - 1;
//        }
//        else
//        {
//            x = (int) collisionObject.rectangle.x / Gfx.getTileWidth();
//            y = ((int) collisionObject.rectangle.y / Gfx.getTileHeight()) - 1;
//        }
//
//        return app.collisionUtils.getMarkerTileOn(x, y) == TileID._CRATER_TILE;

        return false;
    }

    private void obstacleCheck()
    {
//        app.getPlayer().isBlockedLeft = (collisionCheck(app.collisionUtils.getBoxHittingLeft(this).gid)
//            && (lookingAt.getX() == Movement._DIRECTION_LEFT));
//
//        app.getPlayer().isBlockedRight = (collisionCheck(app.collisionUtils.getBoxHittingRight(this).gid)
//            && (lookingAt.getX() == Movement._DIRECTION_RIGHT));
    }

    private boolean collisionCheck(GraphicID graphicID)
    {
        boolean isHitting;

        switch (graphicID)
        {
            case G_DEFENDER:
            case G_DEFENDER_ZAP:
            case G_MISSILE_LAUNCHER:
            case G_MISSILE_BASE:
            {
                isHitting = true;
            }
            break;

            default:
            {
                isHitting = false;
            }
            break;
        }

        return isHitting;
    }

    // TODO: 29/09/2018 - Move this to RoverGun() class
    private void moveGunTurret()
    {
//        if (app.getGun().isAttachedToRover)
//        {
//            if (UIButtons.buttonUp.isPressed && (app.getGun().gunTurretAngle >= 0.0f) && (app.getGun().gunTurretAngle < 90.0f))
//            {
//                app.getGun().gunTurretAngle++;
//            }
//            else
//            {
//                if (UIButtons.buttonDown.isPressed && (app.getGun().gunTurretAngle <= 90.0f) && (app.getGun().gunTurretAngle > 0.0f))
//                {
//                    app.getGun().gunTurretAngle--;
//                }
//            }
//
//            app.getGun().gunTurret.sprite.setRotation(app.getGun().gunTurretAngle * this.direction.getX());
//        }
    }
}

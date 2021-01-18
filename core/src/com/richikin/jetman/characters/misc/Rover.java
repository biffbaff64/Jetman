package com.richikin.jetman.characters.misc;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.enumslib.ActionStates;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.characters.managers.ExplosionManager;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.enumslib.GraphicID;
import com.richikin.enumslib.PlayServicesID;
import com.richikin.utilslib.physics.Movement;
import com.richikin.utilslib.logging.Trace;

public class Rover extends GdxSprite
{
    public boolean isMovingX;
    public boolean isMovingY;

    public GdxSprite roverBack;
    public GdxSprite frontWheel;
    public GdxSprite backWheel;

    public Rover()
    {
        super(GraphicID.G_ROVER);
    }

    /**
     * Create and initialise the Moon Rover
     *
     * @param descriptor  The {@link SpriteDescriptor} which
     *                          holds all the necessary information
     *                          for creating this entity.
     */
    @Override
    public void initialise(SpriteDescriptor descriptor)
    {
        create(descriptor);

        bodyCategory = Gfx.CAT_VEHICLE;
        collidesWith = Gfx.CAT_MOBILE_ENEMY
            | Gfx.CAT_FIXED_ENEMY
            | Gfx.CAT_PLAYER_WEAPON
            | Gfx.CAT_GROUND;

        initXYZ.set(sprite.getX(), sprite.getY(), zPosition);

        setup();

        addDynamicPhysicsBody();
    }

    /**
     * Part 2 of the initialisation process.
     * This is seperate from Initialise() as it
     * does not create objects and can be called
     * on level reset/retry.
     */
    public void setup()
    {
        setAction(ActionStates._STANDING);

        animation.setPlayMode(Animation.PlayMode.NORMAL);
        speed.set(1, 0);

        isMovingX = false;
        isMovingY = false;

        sprite.setPosition(initXYZ.getX(), initXYZ.getY());

        direction.setX(Movement._DIRECTION_RIGHT);
        lookingAt.setX(direction.getX());

        App.gameProgress.roverDestroyed = false;
    }

    /**
     * Add all required extra sprite objects, such as
     * the wheels and boot.
     */
    public void addPartners()
    {
        Trace.__FILE_FUNC();

        SpriteDescriptor descriptor = App.entities.getDescriptor(GraphicID.G_ROVER_WHEEL);
        descriptor._SIZE          = GameAssets.getAssetSize(GraphicID.G_ROVER_WHEEL);
        descriptor._POSITION.x    = 0;
        descriptor._POSITION.y    = 0;
        descriptor._POSITION.z    = App.entityUtils.getInitialZPosition(GraphicID.G_ROVER_WHEEL);
        descriptor._INDEX         = App.entityData.entityMap.size;

        // Add the front wheel
        if (frontWheel == null)
        {
            Trace.__FILE_FUNC("Adding Rover Front Wheel.");

            frontWheel = new GdxSprite(GraphicID.G_ROVER_WHEEL);
            frontWheel.create(descriptor);
            App.entityData.addEntity(frontWheel);
        }

        // Add the back wheel
        if (backWheel == null)
        {
            Trace.__FILE_FUNC("Adding Rover Back Wheel.");

            descriptor._INDEX = App.entityData.entityMap.size;

            backWheel = new GdxSprite(GraphicID.G_ROVER_WHEEL);
            backWheel.create(descriptor);
            App.entityData.addEntity(backWheel);
        }

        // Add the back tray
        if (roverBack == null)
        {
            Trace.__FILE_FUNC("Adding Rover Boot.");

            descriptor = App.entities.getDescriptor(GraphicID.G_ROVER_BOOT);
            descriptor._SIZE = GameAssets.getAssetSize(GraphicID.G_ROVER_BOOT);
            descriptor._POSITION.z = App.entityUtils.getInitialZPosition(GraphicID.G_ROVER_BOOT);
            descriptor._INDEX = App.entityData.entityMap.size;

            roverBack = new GdxSprite(GraphicID.G_ROVER_BOOT);
            roverBack.create(descriptor);
            roverBack.bodyCategory = Gfx.CAT_GROUND;
            roverBack.collidesWith = Gfx.CAT_PLAYER;
            App.entityData.addEntity(roverBack);
        }
    }

    @Override
    public void update(int spriteNum)
    {
        frontWheel.preUpdate();
        backWheel.preUpdate();
        roverBack.preUpdate();

        switch(getAction())
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
                moveRover();
            }
            break;

            case _HURT:
            case _KILLED:
            {
                explode();

                collisionObject.setInvisibility(1000);

                setAction(ActionStates._EXPLODING);
            }
            break;

            case _EXPLODING:
            {
            }
            break;

            case _DYING:
            {
                kill();
            }
            break;

            default:
            {
                Trace.__FILE_FUNC("Unsupported spriteAction: " + getAction());
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

    private void moveRover()
    {
        frontWheel.isRotating = true;
        backWheel.isRotating = true;

        frontWheel.rotateSpeed = speed.getX() * (direction.getX() * -1);
        backWheel.rotateSpeed = speed.getX() * (direction.getX() * -1);

        sprite.translate(speed.getX() * direction.getX(), 0);
    }

    private void kill()
    {
        setAction(ActionStates._DEAD);
        frontWheel.setAction(ActionStates._DEAD);
        backWheel.setAction(ActionStates._DEAD);
        roverBack.setAction(ActionStates._DEAD);

        App.gameProgress.playerLifeOver = true;
        App.gameProgress.roverDestroyed = true;
    }

    private void explode()
    {
        ExplosionManager explosionManager = new ExplosionManager();
        explosionManager.createExplosion(GraphicID.G_EXPLOSION256, this);
        explosionManager.createExplosion(GraphicID.G_EXPLOSION128, roverBack);
        explosionManager.createExplosion(GraphicID.G_EXPLOSION128, frontWheel);
        explosionManager.createExplosion(GraphicID.G_EXPLOSION128, backWheel);
    }

    @Override
    public void postUpdate(int spriteNum)
    {
        if (getAction() == ActionStates._DEAD)
        {
            App.gameProgress.getLives().setToMinimum();
        }
    }

    @Override
    public void animate()
    {
        elapsedAnimTime += Gdx.graphics.getDeltaTime();
        sprite.setRegion(App.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
    }

    @Override
    public void updateCollisionBox()
    {
        collisionObject.rectangle.x = sprite.getX();
        collisionObject.rectangle.y = sprite.getY() - 2;
        collisionObject.rectangle.width = frameWidth;
        collisionObject.rectangle.height = frameHeight;

        frontWheel.updateCollisionBox();
        backWheel.updateCollisionBox();
        roverBack.updateCollisionBox();
    }

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
        App.getPlayer().buttons.checkButtons();
        App.getGun().moveGunTurret();

        obstacleCheck();

        if (isMovingX && !checkForCrater())
        {
            speed.set(3, 0);
            setAction(ActionStates._RUNNING);

            App.googleServices.unlockAchievement(PlayServicesID.achievement_moon_rider.getID());
        }
        else
        {
            speed.set(0, 0);
            setAction(ActionStates._STANDING);
        }
    }

    @SuppressWarnings("SameReturnValue")
    private boolean checkForCrater()
    {
//        int x;
//        int y;
//
//        if (lookingAt.getX() == Movement._DIRECTION_RIGHT)
//        {
//            x = (int) ((collisionObject.rectangle.x + frameWidth) / Gfx.getTileWidth());
//            y = ((int) collisionObject.rectangle.y / Gfx.getTileHeight()) - 1;
//        }
//        else
//        {
//            x = (int) collisionObject.rectangle.x / Gfx.getTileWidth();
//            y = ((int) collisionObject.rectangle.y / Gfx.getTileHeight()) - 1;
//        }
//
//        return App.collisionUtils.getMarkerTileOn(x, y) == TileID._CRATER_TILE;

        return false;
    }

    private void obstacleCheck()
    {
        App.getPlayer().isBlockedLeft = (collisionCheck(App.collisionUtils.getBoxHittingLeft(this).gid)
            && (lookingAt.getX() == Movement._DIRECTION_LEFT));

        App.getPlayer().isBlockedRight = (collisionCheck(App.collisionUtils.getBoxHittingRight(this).gid)
            && (lookingAt.getX() == Movement._DIRECTION_RIGHT));
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
            case G_POWER_BEAM:
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

    @Override
    public void tidy(int index)
    {
        collisionObject.kill();
        App.entityData.removeEntity(index);
    }
}

package com.richikin.jetman.entities.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.richikin.jetman.core.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.core.GameConstants;
import com.richikin.jetman.entities.GdxSprite;
import com.richikin.jetman.entities.objects.EntityDescriptor;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.physics.Movement;

public class MainPlayer extends GdxSprite
{
    private static final float _PLAYER_X_SPEED      = 3.0f;
    private static final float _PLAYER_JUMP_X_SPEED = 16.0f;
    private static final float _PLAYER_Y_SPEED      = 4.0f;
    private static final int   _SPAWN_FRAMES        = 20;
    private static final int   _PLAYER_APPEAR_FRAME = 13;

    public boolean isRidingRover;
    public boolean isOnRoverBack;
    public boolean isHurting;
    public boolean isMovingX;
    public boolean isMovingY;
    public boolean isShooting;
    public boolean localIsDrawable;
    public boolean isTeleporting;
    public boolean isOnGround;
    public boolean isInMidAir;
    public boolean isFalling;

    private TextureRegion bridgeSection;
    private int           laserColour;
    private float         shootRate;
    private int           shootCount;
    private float         maxMoveSpeed;

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
        animate();

        updateCommon();
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

    public void kill()
    {
    }

    private void createPartners()
    {
    }
}

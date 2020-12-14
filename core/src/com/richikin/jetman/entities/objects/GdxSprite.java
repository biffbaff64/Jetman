package com.richikin.jetman.entities.objects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.richikin.enumslib.ActionStates;
import com.richikin.jetman.config.Settings;
import com.richikin.jetman.core.App;
import com.richikin.jetman.core.GameConstants;
import com.richikin.jetman.entities.SpriteComponent;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.enumslib.GraphicID;
import com.richikin.jetman.physics.aabb.AABB;
import com.richikin.jetman.physics.box2d.B2DConstants;
import com.richikin.utilslib.maths.*;
import com.richikin.jetman.physics.aabb.ICollisionListener;
import com.richikin.utilslib.logging.Trace;
import com.richikin.utilslib.physics.Direction;
import com.richikin.utilslib.physics.Movement;

public class GdxSprite extends GameEntity implements SpriteComponent
{
    // -----------------------------------------------
    // properties etc
    //
    public SimpleVec3 initXYZ;                      // The entity's start map coordinates
    public int        spriteNumber;
    public float      rotateSpeed;
    public float      rotation;
    public int        link;

    // -----------------------------------------------
    // Collision Related
    //
    public  AABB               aabb;               // Reference to the box collision handler;
    private ICollisionListener collisionCallback;

    // -----------------------------------------------
    // public flags
    //
    public boolean isDrawable;
    public boolean isRotating;
    public boolean isFlippedX;
    public boolean isFlippedY;
    public boolean isAnimating;
    public boolean isLinked;
    public boolean isMainCharacter;
    public boolean isEnemy;

    // -----------------------------------------------
    // Animation related
    public Animation<TextureRegion> animation;
    public float                    elapsedAnimTime;
    public TextureRegion[]          animFrames;

    public Sprite    sprite;
    public Direction direction;
    public Direction lookingAt;
    public XYSetF    distance;
    public SimpleVec2F     speed;
    public int       strength;

    // --------------------------------------------------------------
    // Code
    // --------------------------------------------------------------

    public GdxSprite()
    {
        super();
    }

    public GdxSprite(GraphicID gid)
    {
        super(gid);
    }

    /**
     * Initialise this entity
     *
     * @param descriptor {@link SpriteDescriptor} Entity initialisation data.
     */
    @Override
    public void initialise(final SpriteDescriptor descriptor)
    {
        create(descriptor);

        //
        // Override in any entity classes and add any
        // other relevant initialisation code here.
    }

    @Override
    public void create(SpriteDescriptor descriptor)
    {
        sprite    = new Sprite();
        direction = new Direction();
        lookingAt = new Direction();
        speed     = new SimpleVec2F();
        distance  = new XYSetF();
        initXYZ   = new SimpleVec3();
        aabb      = new AABB();

        strength            = GameConstants._MAX_STRENGTH;
        spriteNumber        = 0;
        rotation            = 0;
        isDrawable          = true;
        isRotating          = false;
        isFlippedX          = false;
        isFlippedY          = false;
        isMainCharacter     = false;

        spriteNumber = descriptor._INDEX;
        isAnimating  = (descriptor._FRAMES > 1);

        setAction(ActionStates._NO_ACTION);

        if (descriptor._ASSET != null)
        {
            setAnimation(descriptor, descriptor._ANIM_RATE);
        }

        initPosition(new SimpleVec3
            (
                descriptor._POSITION.x,
                descriptor._POSITION.y,
                descriptor._POSITION.z
            ));

        setCollisionObject(sprite.getX(), sprite.getY());

        isLinked = (descriptor._LINK > 0);
        link     = descriptor._LINK;
    }

    @Override
    public void initPosition(SimpleVec3 vec3F)
    {
        sprite.setSize(frameWidth, frameHeight);

        sprite.setPosition((vec3F.x * Gfx.getTileWidth()), (vec3F.y * Gfx.getTileHeight()));

        sprite.setBounds(sprite.getX(), sprite.getY(), frameWidth, frameHeight);
        sprite.setOriginCenter();

        position  = new SimpleVec2((int) sprite.getX(), (int) sprite.getY());
        zPosition = vec3F.z;

        initXYZ.set(sprite.getX(), sprite.getY(), vec3F.z);
    }

    @Override
    public void addPhysicsBody()
    {
        if (App.settings.isEnabled(Settings._BOX2D_PHYSICS))
        {
            b2dBody = App.worldModel.bodyBuilder.createDynamicBox
                (
                    this,
                    1.0f,
                    B2DConstants.DEFAULT_FRICTION,
                    B2DConstants.DEFAULT_RESTITUTION
                );
        }
    }

    /**
     * Sets the sprite position from the physics body coordinates
     */
    @Override
    public void setPositionfromBody()
    {
        if (b2dBody != null)
        {
            sprite.setPosition
                (
                    (b2dBody.getPosition().x * Gfx._PPM) - (frameWidth >> 1),
                    (b2dBody.getPosition().y * Gfx._PPM) - (frameHeight >> 1)
                );
        }
    }

    @Override
    public void preUpdate()
    {
        if (App.gameProgress.levelCompleted
            && !isMainCharacter
            && (entityAction != ActionStates._DEAD)
            && (entityAction != ActionStates._DYING))
        {
            entityAction = ActionStates._DYING;
        }
        else
        {
            if (entityAction == ActionStates._RESTARTING)
            {
                sprite.setPosition(initXYZ.getX(), initXYZ.getY());
            }

            updateCollisionCheck();
        }
    }

    /**
     * Performs update tasks for
     * this entity.
     *
     * @param spriteNum index into the entity map
     */
    @Override
    public void update(final int spriteNum)
    {
    }

    /**
     * Common updates needed for all entities
     */
    // TODO: 04/12/2020 - Remove Feature Envy warning (Suppressed for now).
    @SuppressWarnings("FeatureEnvy")
    @Override
    public void updateCommon()
    {
        if (isRotating)
        {
            sprite.rotate(rotateSpeed);
        }

        sprite.setFlip(isFlippedX, isFlippedY);

        //
        // A sprite can be looking left but moving right etc...
        // (This does, of course, rely on all animations
        // facing RIGHT by default)
        lookingAt.setX(isFlippedX ? Movement._DIRECTION_LEFT : Movement._DIRECTION_RIGHT);

        // By default, sprites are always looking DOWN
        if (lookingAt.getY() == Movement._DIRECTION_STILL)
        {
            lookingAt.setY(Movement._DIRECTION_DOWN);
        }
    }

    @Override
    public void postUpdate(final int spriteNum)
    {
    }

    @Override
    public void postMove()
    {
    }

    @Override
    public void animate()
    {
    }

    @Override
    public void setAnimation(SpriteDescriptor entityDescriptor)
    {
        setAnimation(entityDescriptor, entityDescriptor._ANIM_RATE);
    }

    @Override
    public void setAnimation(SpriteDescriptor descriptor, float frameRate)
    {
        animFrames = new TextureRegion[descriptor._FRAMES];

        TextureRegion asset = App.assets.getAnimationRegion(descriptor._ASSET);

        if (descriptor._SIZE != null)
        {
            frameWidth  = descriptor._SIZE.x;
            frameHeight = descriptor._SIZE.y;
        }
        else
        {
            frameWidth  = asset.getRegionWidth() / descriptor._FRAMES;
            frameHeight = asset.getRegionHeight();
        }

        TextureRegion[][] tmpFrames = asset.split(frameWidth, frameHeight);

        int i = 0;

        for (final TextureRegion[] tmpFrame : tmpFrames)
        {
            for (final TextureRegion textureRegion : tmpFrame)
            {
                if (i < descriptor._FRAMES)
                {
                    animFrames[i++] = textureRegion;
                }
            }
        }

        animation = new Animation<>(frameRate / 6f, animFrames);
        animation.setPlayMode(descriptor._PLAYMODE);

        sprite.setRegion(animFrames[0]);
    }

    @Override
    public void draw(final SpriteBatch spriteBatch)
    {
        if (isDrawable)
        {
            try
            {
                sprite.draw(spriteBatch);
            }
            catch (NullPointerException npe)
            {
                Trace.__FILE_FUNC(gid.name() + " : " + npe.getMessage());
            }
        }
    }

    /**
     * Wrap an entities position in the map if it
     * has gone beyond either if the maps borders.
     */
    // TODO: 04/12/2020 - Remove Feature Envy warning (Suppressed for now).
    @SuppressWarnings("FeatureEnvy")
    @Override
    public void wrap()
    {
        if ((direction.getX() == Movement._DIRECTION_LEFT) && ((sprite.getX() + frameWidth) < 0))
        {
            sprite.translateX(Gfx.getMapWidth() * Movement._DIRECTION_RIGHT);
        }
        else
        {
            if ((direction.getX() == Movement._DIRECTION_RIGHT) && (sprite.getX() > Gfx.getMapWidth()))
            {
                sprite.translateX(Gfx.getMapWidth() * Movement._DIRECTION_LEFT);
            }
        }
    }

    @Override
    public void updateCollisionBox()
    {
        collisionObject.rectangle.x      = sprite.getX();
        collisionObject.rectangle.y      = sprite.getY();
        collisionObject.rectangle.width  = frameWidth;
        collisionObject.rectangle.height = frameHeight;
    }

    /**
     * Add a {@link ICollisionListener} to
     * this entity.
     *
     * @param listener The listener.
     */
    @Override
    public void addCollisionListener(ICollisionListener listener)
    {
        this.collisionCallback = listener;
    }

    /**
     * Check for any collisions.
     */
    // TODO: 04/12/2020 - Remove Feature Envy warning (Suppressed for now).
    @SuppressWarnings("FeatureEnvy")
    @Override
    public void updateCollisionCheck()
    {
        if (collisionObject != null)
        {
            // make sure the collision rectangle
            // is where the player is
            updateCollisionBox();

            // Invisibility is set for a period of
            // time whn the entity is not affected
            // by any collisions.
            collisionObject.checkInvisibility();
            collisionObject.clearCollision();

            //
            // All CollisionObjects are collidable by default.
            // This flag is available to turn off detection
            // as and when needed.
            if (collisionObject.action == ActionStates._COLLIDABLE)
            {
                if (aabb.checkAABBBoxes(collisionObject))
                {
                    collisionObject.action = ActionStates._COLLIDING;

                    if (collisionCallback != null)
                    {
                        collisionCallback.onPositiveCollision(collisionObject);
                    }

                    collisionObject.setInvisibility(1000);
                }

                //
                // collisionObject.action might have changed at this point.
                if (collisionObject.action != ActionStates._COLLIDING)
                {
                    if (collisionCallback != null)
                    {
                        collisionCallback.onNegativeCollision();
                    }
                }
            }
        }
    }

    /**
     * The LibGDX {@link Sprite} class doesn't have a
     * getPosition() method, just getX() and getY(),
     * so this is here to make up for that.
     *
     * @return Vector3 holding the map position.
     */
    @Override
    public Vector3 getPosition()
    {
        return new Vector3(sprite.getX(), sprite.getY(), zPosition);
    }

    /**
     * Releases all resources of this object.
     */
    @Override
    public void dispose()
    {
        super.dispose();
    }
}

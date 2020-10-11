package com.richikin.jetman.entities;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.richikin.jetman.core.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.core.GameConstants;
import com.richikin.jetman.entities.objects.GameEntity;
import com.richikin.jetman.entities.components.SpriteComponent;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.maths.SimpleVec2F;
import com.richikin.jetman.maths.SimpleVec3F;
import com.richikin.jetman.maths.XYSetF;
import com.richikin.jetman.physics.aabb.AABB;
import com.richikin.jetman.physics.Direction;
import com.richikin.jetman.physics.Movement;
import com.richikin.jetman.physics.Speed;
import com.richikin.jetman.physics.aabb.ICollisionListener;
import com.richikin.jetman.utils.logging.Trace;

public class GdxSprite extends GameEntity implements SpriteComponent
{
    // -----------------------------------------------
    // properties etc
    //
    public SimpleVec3F initXYZ;                 // The entity's start map coordinates
    public int         spriteNumber;
    public float       rotateSpeed;
    public float       rotation;
    public int         link;

    // -----------------------------------------------
    // Collision Related
    //
    public  AABB               aabb;               // Reference to the box collision handler;
    private ICollisionListener collisionCallback;

    // -----------------------------------------------
    // public flags
    //
    // TODO: 22/09/2020 - These flags are getting untidy. Look at making use of these more efficient.
    public boolean isDrawable;
    public boolean isRotating;
    public boolean isFlippedX;
    public boolean isFlippedY;
    public boolean isAnimating;
    public boolean isLinked;
    public boolean isDebuggable;
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
    public Speed     speed;
    public int       strength;

    protected boolean preUpdateCommonDone;

    public GdxSprite(App _app)
    {
        super(_app);
    }

    public GdxSprite(GraphicID _gid, App _app)
    {
        super(_gid, _app);
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
    public void create(SpriteDescriptor entityDescriptor)
    {
        sprite    = new Sprite();
        direction = new Direction();
        lookingAt = new Direction();
        speed     = new Speed();
        distance  = new XYSetF();
        initXYZ   = new SimpleVec3F();
        aabb      = new AABB(app);

        strength            = GameConstants._MAX_STRENGTH;
        spriteNumber        = 0;
        rotation            = 0;
        isDrawable          = true;
        isRotating          = false;
        isFlippedX          = false;
        isFlippedY          = false;
        preUpdateCommonDone = false;
        isDebuggable        = false;
        isMainCharacter     = false;

        spriteNumber = entityDescriptor._INDEX;
        isAnimating  = (entityDescriptor._FRAMES > 1);

        if (entityDescriptor._ASSET != null)
        {
            setAnimation(entityDescriptor, entityDescriptor._ANIM_RATE);
        }

        initPosition(new SimpleVec3F
            (
                entityDescriptor._POSITION.x,
                entityDescriptor._POSITION.y,
                entityDescriptor._POSITION.z
            ));

        setCollisionObject(sprite.getX(), sprite.getY());

        isLinked = (entityDescriptor._LINK > 0);
        link     = entityDescriptor._LINK;
    }

    @Override
    public void initPosition(SimpleVec3F vec3F)
    {
        sprite.setSize(frameWidth, frameHeight);

        sprite.setPosition((vec3F.x * Gfx.getTileWidth()), (vec3F.y * Gfx.getTileHeight()));

        sprite.setBounds(sprite.getX(), sprite.getY(), frameWidth, frameHeight);
        sprite.setOriginCenter();

        initXYZ.set(sprite.getX(), sprite.getY(), (float) vec3F.z);

        position = new SimpleVec2F(sprite.getX(), sprite.getY());
        zPosition = (int) vec3F.z;
    }

    /**
     * Translates the physics body coordinates onto the sprite position
     */
//    public void setPositionfromBody()
//    {
//        if (b2dBody != null)
//        {
//            sprite.setPosition
//                (
//                    (b2dBody.getPosition().x * Gfx._PPM) - (frameWidth / 2),
//                    (b2dBody.getPosition().y * Gfx._PPM) - (frameHeight / 2)
//                );
//        }
//    }

    @Override
    public void preUpdate()
    {
        if (app.gameProgress.levelCompleted
            && !isMainCharacter
            && (entityAction != Actions._DEAD)
            && (entityAction != Actions._DYING))
        {
            entityAction = Actions._DYING;
        }
        else
        {
            if (entityAction == Actions._RESTARTING)
            {
                sprite.setPosition(initXYZ.getX(), initXYZ.getY());
            }

            updateCollisionCheck();
        }

        preUpdateCommonDone = true;
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
        if (isFlippedX)
        {
            lookingAt.setX(Movement._DIRECTION_LEFT);
        }
        else
        {
            lookingAt.setX(Movement._DIRECTION_RIGHT);
        }

        // By default, sprites are always looking DOWN
        if ((lookingAt.getY() != Movement._DIRECTION_DOWN)
            && (lookingAt.getY() != Movement._DIRECTION_UP))
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
    public void setAnimation(SpriteDescriptor _entityDescriptor)
    {
        setAnimation(_entityDescriptor, _entityDescriptor._ANIM_RATE);
    }

    @Override
    public void setAnimation(SpriteDescriptor _descriptor, float _frameRate)
    {
        animFrames = new TextureRegion[_descriptor._FRAMES];

        TextureRegion asset = app.assets.getAnimationRegion(_descriptor._ASSET);

        if (_descriptor._SIZE != null)
        {
            frameWidth  = _descriptor._SIZE.x;
            frameHeight = _descriptor._SIZE.y;
        }
        else
        {
            frameWidth  = (float) (asset.getRegionWidth() / _descriptor._FRAMES);
            frameHeight = asset.getRegionHeight();
        }

        TextureRegion[][] tmpFrames = asset.split
            (
                (int) frameWidth,
                (int) frameHeight
            );

        int i = 0;

        for (final TextureRegion[] tmpFrame : tmpFrames)
        {
            for (final TextureRegion textureRegion : tmpFrame)
            {
                if (i < _descriptor._FRAMES)
                {
                    animFrames[i++] = textureRegion;
                }
            }
        }

        animation = new Animation<>(_frameRate / 6f, animFrames);
        animation.setPlayMode(_descriptor._PLAYMODE);

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
            if (collisionObject.action == Actions._COLLIDABLE)
            {
                if (aabb.checkHittingBox(this))
                {
                    collisionObject.action = Actions._COLLIDING;
                }

                if (collisionObject.action == Actions._COLLIDING)
                {
                    if (app.collisionUtils.filter(collisionObject.contactEntity.collidesWith, bodyCategory))
                    {
                        if (collisionCallback != null)
                        {
                            collisionCallback.onPositiveCollision(collisionObject.contactEntity.gid);
                        }
                    }

                    if (isEnemy && collisionObject.isInvisibilityAllowed)
                    {
                        collisionObject.setInvisibility(1000);
                    }
                }

                //
                // collisionObject.action might have changed at this point.
                if (collisionObject.action != Actions._COLLIDING)
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
     * so this is here to make up fpr that.
     *
     * @return Vector3 holding the map position.
     */
    @Override
    public Vector3 getPosition()
    {
        return new Vector3(sprite.getX(), sprite.getY(), zPosition);
    }

    public float getRightEdge()
    {
        return sprite.getX() + frameWidth;
    }

    @Override
    public void setAction(final Actions action)
    {
        if (entityAction != action)
        {
            entityAction = action;
        }
    }

    @Override
    public Actions getAction()
    {
        return entityAction;
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

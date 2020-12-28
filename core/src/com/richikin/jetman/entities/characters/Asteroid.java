package com.richikin.jetman.entities.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.richikin.enumslib.ActionStates;
import com.richikin.jetman.config.Settings;
import com.richikin.jetman.core.App;
import com.richikin.jetman.core.GameProgress;
import com.richikin.jetman.core.PointsManager;
import com.richikin.jetman.entities.managers.CraterManager;
import com.richikin.jetman.entities.managers.ExplosionManager;
import com.richikin.jetman.entities.objects.GameEntity;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.enumslib.GraphicID;
import com.richikin.jetman.physics.box2d.BodyIdentity;
import com.richikin.jetman.physics.box2d.IContactListener;
import com.richikin.utilslib.physics.Movement;
import com.richikin.utilslib.logging.Trace;

public class Asteroid extends GdxSprite
{
    //
    // Table of scaling values.
    // This is used to create asteroids
    // of varying sizes
    private static final float[] sizes =
        {
            1.5f, 1.25f, 1.0f,
        };

    public Asteroid()
    {
        super(GraphicID.G_ASTEROID);
    }

    @Override
    public void initialise(SpriteDescriptor descriptor)
    {
        create(descriptor);

        sprite.setScale(sizes[MathUtils.random(sizes.length - 1)]);

        initXYZ.set(sprite.getX(), sprite.getY(), zPosition);

        bodyCategory = Gfx.CAT_MOBILE_ENEMY;
        collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_PLAYER_WEAPON | Gfx.CAT_GROUND | Gfx.CAT_VEHICLE;

        speed.setX(Gfx._VIEW_WIDTH * 0.004f);
        speed.setY(speed.getX() * (0.35f + MathUtils.random(0.9f)));

        direction.setX((sprite.getX() < App.getPlayer().sprite.getX()) ? Movement._DIRECTION_RIGHT : Movement._DIRECTION_LEFT);
        direction.setY(Movement._DIRECTION_DOWN);

        setAction(ActionStates._RUNNING);
        isRotating  = true;
        rotateSpeed = (MathUtils.random(5, 8) * (direction.getX() * -1));

        addDynamicPhysicsBody();
        setContactListener();
    }

    @Override
    public void tidy(int index)
    {
    }

    @Override
    public void update(int spriteNum)
    {
        switch (getAction())
        {
            case _RUNNING:
            {
//                b2dBody.setLinearVelocity(speed.getX() * direction.getX(), speed.getY() * direction.getY());
                sprite.translate(speed.getX() * direction.getX(), speed.getY() * direction.getY());
                wrap();

                if ((collisionObject.idBottom == GraphicID._GROUND) || collisionObject.isHittingPlayer)
                {
                    setAction(ActionStates._HURT);
                    elapsedAnimTime = 0;
                }
            }
            break;

            case _KILLED:
            case _HURT:
            {
                ExplosionManager explosionManager = new ExplosionManager();
                explosionManager.createExplosion(GraphicID.G_EXPLOSION128, this);

                if (getAction() == ActionStates._KILLED)
                {
                    App.gameProgress.stackPush(GameProgress.Stack._SCORE, PointsManager.getPoints(this.gid));
                }

                setAction(ActionStates._EXPLODING);
                bodyCategory = Gfx.CAT_NOTHING;
                collidesWith = Gfx.CAT_NOTHING;
            }
            break;

            case _EXPLODING:
            {
                //
                // Do nothing while waiting for the explosion to finish
            }
            break;

            case _DYING:
            {
                CraterManager craterManager = new CraterManager();

                if ((collisionObject.idBottom == GraphicID._GROUND)
                    && (craterManager.canMakeCrater(this)))
                {
                    int x = (int) (sprite.getX() / Gfx.getTileWidth());
                    int y = (int) (App.getPlayer().sprite.getY() / Gfx.getTileHeight()) - 1;

                    craterManager.makeCrater(x, y);
                }

                setAction(ActionStates._DEAD);
            }
            break;

            default:
            {
                Trace.__FILE_FUNC("Unsupported spriteAction: " + getAction());
            }
            break;
        }

        animate();

        updateCommon();
    }

    @Override
    public void animate()
    {
        switch (getAction())
        {
            case _STANDING:
            case _RUNNING:
            {
                if (animation != null)
                {
                    sprite.setRegion(App.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
                    elapsedAnimTime += Gdx.graphics.getDeltaTime();
                }
            }
            break;

            case _KILLED:
            case _DYING:
            case _EXPLODING:
            default:
            {
            }
            break;
        }
    }

    @Override
    public void updateCollisionBox()
    {
        float width  = (frameWidth * sprite.getScaleX());
        float height = (frameWidth * sprite.getScaleY());

        collisionObject.rectangle.x      = sprite.getX() + ((frameWidth - width) / 2);
        collisionObject.rectangle.y      = sprite.getY() + ((frameHeight - height) / 2);
        collisionObject.rectangle.width  = width;
        collisionObject.rectangle.height = height;
    }

    private void setContactListener()
    {
        if (App.settings.isEnabled(Settings._BOX2D_PHYSICS))
        {
            App.worldModel.box2DContactListener.addListener
                (
                    new ContactListener()
                    {
                        @Override
                        public void beginContact(Contact contact)
                        {
//                            GdxSprite spriteA = ((BodyIdentity) contact.getFixtureA().getBody().getUserData()).entity;
//                            GdxSprite spriteB = ((BodyIdentity) contact.getFixtureB().getBody().getUserData()).entity;

//                            if (spriteA != null)
//                            {
//                            if (spriteA.spriteNumber == index)
//                            {
//                                GraphicID gidA = spriteA.gid;
//
//                                collisionObject.isHittingPlayer = (gidA == GraphicID.G_PLAYER);
//
//                                if (gidA == GraphicID._GROUND)
//                                {
//                                    collisionObject.idBottom = GraphicID._GROUND;
//                                }
//                            }
//                            }

//                            if (spriteB != null)
//                            {
//                            if (spriteB.spriteNumber == index)
//                            {
//                                GraphicID gidB = spriteB.gid;
//
//                                collisionObject.isHittingPlayer = (gidB == GraphicID.G_PLAYER);
//
//                                if (gidB == GraphicID._GROUND)
//                                {
//                                    collisionObject.idBottom = GraphicID._GROUND;
//                                }
//                            }
//                            }
                        }

                        @Override
                        public void endContact(Contact contact)
                        {
                        }

                        @Override
                        public void preSolve(Contact contact, Manifold oldManifold)
                        {
                        }

                        @Override
                        public void postSolve(Contact contact, ContactImpulse impulse)
                        {
                        }
                    }
                );
        }
    }
}

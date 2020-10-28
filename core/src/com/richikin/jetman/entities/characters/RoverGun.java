package com.richikin.jetman.entities.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.core.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.Entities;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.entities.managers.ExplosionManager;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.utilslib.maths.SimpleVec2F;
import com.richikin.utilslib.physics.Movement;
import com.richikin.jetman.physics.aabb.ICollisionListener;
import com.richikin.utilslib.logging.Trace;

public class RoverGun extends GdxSprite
{
    public boolean isAttachedToRover;
    public boolean isAttachedToPlayer;
    public boolean isShooting;
    public float gunTurretAngle;
    public float shootRate;
    public int shootCount;
    public SimpleVec2F releaseXY;
    public GunTurret gunTurret;

    private App app;

    public RoverGun(App _app)
    {
        super(GraphicID.G_ROVER_GUN, _app);

        this.app = _app;
    }

    @Override
    public void initialise(SpriteDescriptor descriptor)
    {
        create(descriptor);

        initXYZ.set(sprite.getX(), sprite.getY(), zPosition);

        bodyCategory = Gfx.CAT_PLAYER_WEAPON;
        collidesWith = Gfx.CAT_GROUND | Gfx.CAT_VEHICLE | Gfx.CAT_PLAYER;

        setAction(Actions._STANDING);

        animation.setPlayMode(Animation.PlayMode.NORMAL);

        distance.set(0, 0);

        releaseXY = new SimpleVec2F();
        isAttachedToRover = false;
        isAttachedToPlayer = false;

        setCollisionListener();
    }

    public void addTurret()
    {
        SpriteDescriptor descriptor = Entities.getDescriptor(GraphicID.G_ROVER_GUN_BARREL);
        descriptor._SIZE          = GameAssets.getAssetSize(GraphicID.G_ROVER_GUN_BARREL);
        descriptor._POSITION.x    = 0;
        descriptor._POSITION.y    = 0;
        descriptor._POSITION.z    = app.entityUtils.getInitialZPosition(GraphicID.G_ROVER_GUN_BARREL);
        descriptor._INDEX         = app.entityData.entityMap.size;

        gunTurret = new GunTurret(app);
        gunTurret.initialise(descriptor);
        app.entityData.addEntity(gunTurret);
    }

    @Override
    public void update(int spriteNum)
    {
        switch(getAction())
        {
            case _STANDING:
            {
                if (isAttachedToPlayer)
                {
                    if (app.getPlayer().isOnGround
                        || (app.getPlayer().sprite.getY() < (initXYZ.getY() + frameHeight)))
                    {
                        sprite.setPosition(app.getPlayer().sprite.getX(), app.getPlayer().sprite.getY());
                    }
                    else
                    {
                        sprite.setPosition(app.getPlayer().sprite.getX(), app.getPlayer().sprite.getY() - (frameHeight / 2));
                    }

                    isAttachedToRover = false;
                }
                else if (isAttachedToRover)
                {
                    isFlippedX = app.getRover().isFlippedX;

                    if (gunTurret != null)
                    {
                        gunTurret.isFlippedX = app.getRover().isFlippedX;
                    }

                    if (isFlippedX)
                    {
                        sprite.setPosition(app.getRover().sprite.getX() + 82,
                            (app.getRover().sprite.getY() + app.getRover().frameHeight) - 48);
                    }
                    else
                    {
                        sprite.setPosition(app.getRover().sprite.getX() + 99,
                            (app.getRover().sprite.getY() + app.getRover().frameHeight) - 48);
                    }
                }
            }
            break;

            case _FALLING:
            {
                isAttachedToRover = false;
                isAttachedToPlayer = false;

                direction.setY(Movement._DIRECTION_DOWN);
                speed.y += 0.2f;
            }
            break;

            case _HURT:
            case _KILLED:
            {
                ExplosionManager explosionManager = new ExplosionManager();
                explosionManager.createExplosion(GraphicID.G_EXPLOSION64, this, app);
                setAction(Actions._EXPLODING);

                if (gunTurret != null)
                {
                    explosionManager.createExplosion(GraphicID.G_EXPLOSION64, gunTurret, app);
                    gunTurret.setAction(Actions._EXPLODING);
                }
            }
            break;

            case _FIGHTING:
            case _EXPLODING:
            {
            }
            break;

            case _DYING:
            {
                setAction(Actions._DEAD);
                gunTurret.setAction(Actions._DEAD);
                releaseXY = null;
            }
            break;

            default:
            {
                Trace.__FILE_FUNC("Unsupported spriteAction: " + getAction());
            }
            break;
        }

        if (isShooting)
        {
            shootRate += Gdx.graphics.getDeltaTime();

            if (isShooting && (shootRate > 0.0875f) && (shootCount < 2))
            {
//                EntityDescriptor descriptor = new EntityDescriptor();
//                descriptor._ASSET         = app.assets.getAnimationsAtlas().findRegion(GameAssets._SPARKLE_WEAPON_ASSET);
//                descriptor._FRAMES        = GameAssets._SPARKLE_WEAPON_FRAMES;
//                descriptor._PLAYMODE      = Animation.PlayMode.LOOP;
//                descriptor._X             = (int) (gunTurret.sprite.getX() / Gfx.getTileWidth());
//                descriptor._Y             = (int) (gunTurret.sprite.getY() / Gfx.getTileHeight());
//                descriptor._Z             = app.entityUtils.getInitialZPosition(GraphicID.G_ROVER_BULLET);
//                descriptor._INDEX         = app.entityData.entityMap.size;
//                descriptor._ENEMY         = app.entityUtils.setEnemyStatus(GraphicID.G_ROVER_BULLET);
//                descriptor._UPDATEABLE    = app.entityUtils.canUpdate(GraphicID.G_ROVER_BULLET);
//
//                SparkleWeapon weapon = new SparkleWeapon(GraphicID.G_ROVER_BULLET, app);
//                weapon.initialise(descriptor);
//
//                app.entityData.addEntity(weapon);

                if (++shootCount >= 2)
                {
                    isShooting = false;
                }

                shootRate = 0;
            }
        }

        sprite.translate((speed.getX() * direction.getX()), (speed.getY() * direction.getY()));

        animate();

        updateCommon();
    }

    @Override
    public void animate()
    {
        sprite.setRegion(animFrames[0]);

        if (gunTurret != null)
        {
            gunTurret.animate();
        }
    }

    @Override
    public void updateCollisionBox()
    {
        collisionObject.rectangle.x = sprite.getX() + Gfx.getTileWidth();
        collisionObject.rectangle.y = sprite.getY() - 2;
        collisionObject.rectangle.width = (float) (frameWidth - (Gfx.getTileWidth() * 1.5));
        collisionObject.rectangle.height = frameHeight;
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        if (isAttachedToPlayer && app.teleportManager.teleportActive)
        {
            isDrawable = false;

            if (gunTurret != null)
            {
                gunTurret.isDrawable = false;
            }
        }
        else
        {
            isDrawable = true;

            if (gunTurret != null)
            {
                gunTurret.isDrawable = true;
            }
        }

        super.draw(spriteBatch);

        if (gunTurret != null)
        {
            gunTurret.sprite.setPosition(sprite.getX(), sprite.getY());
            gunTurret.draw(spriteBatch);
        }
    }

    @Override
    public void tidy(int _index)
    {
        app.entityData.removeEntity(_index);
    }

    public void startShooting()
    {
        if (!isShooting)
        {
            isShooting = true;
            shootCount = 0;
            shootRate = 0;
        }
    }

    public void explode()
    {
        ExplosionManager explosionManager = new ExplosionManager();
        explosionManager.createExplosion(GraphicID.G_EXPLOSION256, this, app);
        explosionManager.createExplosion(GraphicID.G_EXPLOSION256, gunTurret, app);

        Entities.explode(this);
        Entities.explode(gunTurret);

        elapsedAnimTime = 0;
        gunTurret.elapsedAnimTime = 0;
    }

    private void setCollisionListener()
    {
        addCollisionListener(new ICollisionListener()
        {
            @Override
            public void onPositiveCollision(GraphicID graphicID)
            {
                if (getAction() == Actions._FALLING)
                {
                    GraphicID contactID = app.collisionUtils.getBoxHittingBottom(app.getGun()).gid;

                    if (contactID == GraphicID._GROUND)
                    {
                        direction.setY(Movement._DIRECTION_STILL);
                        speed.setY(0);
                        Entities.stand(app.getGun());
                    }
                    else if ((contactID == GraphicID.G_MISSILE_BASE)
                        || (contactID == GraphicID.G_MISSILE_LAUNCHER)
                        || (contactID == GraphicID.G_DEFENDER))
                    {
                        direction.setY(Movement._DIRECTION_STILL);
                        speed.setY(0);

                        explode();
                    }
                    else if (contactID == GraphicID.G_ROVER_BOOT)
                    {
                        isAttachedToRover = true;
                        direction.setY(Movement._DIRECTION_STILL);
                        speed.setY(0);
                        Entities.stand(app.getGun());

//                        app.googleServices.unlockAchievement(PlayServicesID.achievement_gunman_jetman.getID());
                    }
                }
            }

            @Override
            public void onNegativeCollision()
            {
                if ((sprite.getY() + frameHeight) < 0)
                {
                     setAction(Actions._DEAD);
                }
                else
                {
                    if (collisionObject.idBottom == GraphicID.G_NO_ID)
                    {
                        setAction(Actions._FALLING);
                    }
                }
            }

            @Override
            public void dispose()
            {
            }
        });
    }
}

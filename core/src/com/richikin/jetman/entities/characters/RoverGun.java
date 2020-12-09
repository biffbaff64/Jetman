package com.richikin.jetman.entities.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.enumslib.ActionStates;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.entities.managers.ExplosionManager;
import com.richikin.jetman.entities.types.Carryable;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.enumslib.GraphicID;
import com.richikin.jetman.core.PlayServicesID;
import com.richikin.jetman.physics.Movement;
import com.richikin.jetman.physics.aabb.ICollisionListener;
import com.richikin.utilslib.logging.Trace;

public class RoverGun extends Carryable
{
    public boolean   isShooting;
    public float     gunTurretAngle;
    public float     shootRate;
    public int       shootCount;
    public GunTurret gunTurret;

    public RoverGun()
    {
        super(GraphicID.G_ROVER_GUN);
    }

    @Override
    public void initialise(SpriteDescriptor descriptor)
    {
        Trace.__FILE_FUNC();

        super.initialise(descriptor);

        bodyCategory = Gfx.CAT_PLAYER_WEAPON;
        collidesWith = Gfx.CAT_GROUND | Gfx.CAT_VEHICLE;

        isShooting = false;
        gunTurretAngle = 0f;
        shootCount = 0;
        shootRate = 0f;
    }

    public void addTurret()
    {
        SpriteDescriptor descriptor = App.entities.getDescriptor(GraphicID.G_ROVER_GUN_BARREL);
        descriptor._SIZE       = GameAssets.getAssetSize(GraphicID.G_ROVER_GUN_BARREL);
        descriptor._POSITION.x = 0;
        descriptor._POSITION.y = 0;
        descriptor._POSITION.z = App.entityUtils.getInitialZPosition(GraphicID.G_ROVER_GUN_BARREL);
        descriptor._INDEX      = App.entityData.entityMap.size;

        // TODO: 29/11/2020 - Does the turret need to be added to the entity list?
        gunTurret = new GunTurret();
        gunTurret.initialise(descriptor);
        App.entityData.addEntity(gunTurret);
    }

    @Override
    public void updateAttachedToRover()
    {
        isFlippedX = App.getRover().isFlippedX;

        if (gunTurret != null)
        {
            gunTurret.isFlippedX = App.getRover().isFlippedX;
        }

        if (isFlippedX)
        {
            sprite.setPosition(App.getRover().sprite.getX() + 82,
                (App.getRover().sprite.getY() + App.getRover().frameHeight) - 48);
        }
        else
        {
            sprite.setPosition(App.getRover().sprite.getX() + 99,
                (App.getRover().sprite.getY() + App.getRover().frameHeight) - 48);
        }

        isAttachedToPlayer = false;
    }

    @Override
    public void update(int spriteNum)
    {
        switch (getAction())
        {
            case _HURT:
            case _KILLED:
            {
                ExplosionManager explosionManager = new ExplosionManager();
                explosionManager.createExplosion(GraphicID.G_EXPLOSION64, this);
                setAction(ActionStates._EXPLODING);

                if (gunTurret != null)
                {
                    explosionManager.createExplosion(GraphicID.G_EXPLOSION64, gunTurret);
                    gunTurret.setAction(ActionStates._EXPLODING);
                }
            }
            break;

            case _DYING:
            {
                setAction(ActionStates._DEAD);
                gunTurret.setAction(ActionStates._DEAD);
                releaseXY = null;
            }
            break;

            default:
            {
                super.update(spriteNum);
            }
            break;
        }

        if (isShooting)
        {
            shootRate += Gdx.graphics.getDeltaTime();

            if (isShooting && (shootRate > 0.0875f) && (shootCount < 2))
            {
                SpriteDescriptor descriptor = App.entities.getDescriptor(GraphicID.G_ROVER_BULLET);
                descriptor._POSITION.x = (int) (gunTurret.sprite.getX() / Gfx.getTileWidth());
                descriptor._POSITION.x = (int) (gunTurret.sprite.getY() / Gfx.getTileHeight());
                descriptor._POSITION.z = App.entityUtils.getInitialZPosition(GraphicID.G_ROVER_BULLET);
                descriptor._INDEX      = App.entityData.entityMap.size;

                DefenderBullet weapon = new DefenderBullet(GraphicID.G_ROVER_BULLET);
                weapon.initialise(descriptor);

                App.entityData.addEntity(weapon);

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
        collisionObject.rectangle.x      = sprite.getX() + Gfx.getTileWidth();
        collisionObject.rectangle.y      = sprite.getY() - 2;
        collisionObject.rectangle.width  = (float) (frameWidth - (Gfx.getTileWidth() * 1.5));
        collisionObject.rectangle.height = frameHeight;
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        isDrawable = (isAttachedToPlayer && App.teleportManager.teleportActive);

        if (gunTurret != null)
        {
            gunTurret.isDrawable = this.isDrawable;
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
        App.entityData.removeEntity(_index);
    }

    public void startShooting()
    {
        if (!isShooting)
        {
            isShooting = true;
            shootCount = 0;
            shootRate  = 0;
        }
    }

    @Override
    public void explode()
    {
        ExplosionManager explosionManager = new ExplosionManager();
        explosionManager.createExplosion(GraphicID.G_EXPLOSION256, this);
        explosionManager.createExplosion(GraphicID.G_EXPLOSION256, gunTurret);

        setAction(ActionStates._EXPLODING);
        elapsedAnimTime = 0;

        gunTurret.setAction(ActionStates._EXPLODING);
        gunTurret.elapsedAnimTime = 0;
    }

    @Override
    public void setCollisionListener()
    {
        addCollisionListener(new ICollisionListener()
        {
            @Override
            public void onPositiveCollision(GraphicID graphicID)
            {
                if (getAction() == ActionStates._FALLING)
                {
                    GraphicID contactID = App.collisionUtils.getBoxHittingBottom(App.getGun()).gid;

                    switch (contactID)
                    {
                        case _GROUND:
                        {
                            direction.setY(Movement._DIRECTION_STILL);
                            speed.setY(0);
                            setAction(ActionStates._STANDING);
                        }
                        break;

                        case G_MISSILE_BASE:
                        case G_MISSILE_LAUNCHER:
                        case G_DEFENDER:
                        {
                            direction.setY(Movement._DIRECTION_STILL);
                            speed.setY(0);

                            explode();
                        }
                        break;

                        case G_ROVER:
                        case G_ROVER_BOOT:
                        {
                            isAttachedToRover = true;
                            direction.setY(Movement._DIRECTION_STILL);
                            speed.setY(0);
                            setAction(ActionStates._STANDING);

                            App.googleServices.unlockAchievement(PlayServicesID.achievement_gunman_jetman.getID());
                        }
                        break;
                    }
                }
            }

            @Override
            public void onNegativeCollision()
            {
                if ((sprite.getY() + frameHeight) < 0)
                {
                    setAction(ActionStates._DEAD);
                }
                else
                {
                    if (!isAttachedToPlayer
                        && !isAttachedToRover
                        && (collisionObject.idBottom == GraphicID.G_NO_ID))
                    {
                        setAction(ActionStates._FALLING);
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

package com.richikin.jetman.entities.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.enumslib.ActionStates;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.entities.managers.ExplosionManager;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.enumslib.GraphicID;
import com.richikin.utilslib.logging.StopWatch;
import com.richikin.utilslib.logging.Trace;

import java.util.concurrent.TimeUnit;

public class DefenceStation extends GdxSprite
{
    private       ZapSprite zapSprite;
    private       StopWatch shootTimer;
    private       float     shootInterval;

    public DefenceStation()
    {
        super(GraphicID.G_DEFENDER);
    }

    @Override
    public void initialise(SpriteDescriptor descriptor)
    {
        create(descriptor);

        initXYZ.set(sprite.getX(), sprite.getY(), zPosition);

        bodyCategory = Gfx.CAT_MISSILE_BASE;
        collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_VEHICLE | Gfx.CAT_PLAYER_WEAPON | Gfx.CAT_MOBILE_ENEMY;

        animation.setPlayMode(Animation.PlayMode.NORMAL);

        setAction(ActionStates._STANDING);

        shootTimer      = StopWatch.start();
        shootInterval   = App.roomManager.getFireRate();

        addDynamicPhysicsBody();
    }

    @Override
    public void update(int spriteNum)
    {
        switch (getAction())
        {
            case _STANDING:
            {
                sprite.setRegion(App.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
                elapsedAnimTime += Gdx.graphics.getDeltaTime();

                if (shootTimer.time(TimeUnit.MILLISECONDS) > shootInterval)
                {
                    App.missileBaseManager.shoot(this);

                    shootInterval = App.roomManager.getFireRate();
                    shootTimer.reset();
                }
            }
            break;

            case _KILLED:
            case _HURT:
            case _EXPLODING:
            {
            }
            break;

            case _DYING:
            {
                setAction(ActionStates._DEAD);
                zapSprite.setAction(ActionStates._DEAD);
            }
            break;

            default:
            {
                Trace.__FILE_FUNC_WithDivider();
                Trace.dbg("Unsupported spriteAction: " + getAction());
            }
            break;
        }

        updateCommon();
    }

    @Override
    public void tidy(int index)
    {
        collisionObject.kill();
        App.entityData.removeEntity(index);
    }

    public void explode()
    {
        ExplosionManager explosionManager = new ExplosionManager();
        explosionManager.createExplosion(GraphicID.G_EXPLOSION128, this);
        explosionManager.createExplosion(GraphicID.G_EXPLOSION64, zapSprite);

        setAction(ActionStates._EXPLODING);
        zapSprite.setAction(ActionStates._EXPLODING);
    }

    /**
     * Adds the spark zapper beam to the
     * defence station.
     */
    public void addZapper()
    {
        SpriteDescriptor descriptor = App.entities.getDescriptor(GraphicID.G_DEFENDER_ZAP);
        descriptor._POSITION.x    = (int) (sprite.getX() / Gfx.getTileWidth());
        descriptor._POSITION.y    = (int) ((sprite.getY() + frameHeight) / Gfx.getTileHeight());
        descriptor._POSITION.z    = App.entityUtils.getInitialZPosition(GraphicID.G_DEFENDER_ZAP);
        descriptor._SIZE          = GameAssets.getAssetSize(GraphicID.G_DEFENDER_ZAP);
        descriptor._INDEX         = App.entityData.entityMap.size;

        zapSprite = new ZapSprite(GraphicID.G_DEFENDER_ZAP, this);
        zapSprite.initialise(descriptor);
        App.entityData.addEntity(zapSprite);
    }
}

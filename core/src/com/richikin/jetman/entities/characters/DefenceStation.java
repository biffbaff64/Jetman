package com.richikin.jetman.entities.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.core.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.Entities;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.entities.managers.ExplosionManager;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.utils.logging.StopWatch;
import com.richikin.jetman.utils.logging.Trace;

import java.util.concurrent.TimeUnit;

public class DefenceStation extends GdxSprite
{
    private       ZapSprite zapSprite;
    private       StopWatch shootTimer;
    private       float     shootInterval;

    public DefenceStation(App _app)
    {
        super(GraphicID.G_DEFENDER, _app);
    }

    @Override
    public void initialise(SpriteDescriptor descriptor)
    {
        create(descriptor);

        initXYZ.set(sprite.getX(), sprite.getY(), zPosition);

        bodyCategory = Gfx.CAT_MISSILE_BASE;
        collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_VEHICLE | Gfx.CAT_PLAYER_WEAPON | Gfx.CAT_MOBILE_ENEMY;

        animation.setPlayMode(Animation.PlayMode.NORMAL);

        setAction(Actions._STANDING);

        shootTimer      = StopWatch.start();
        shootInterval   = app.roomManager.getFireRate();
    }

    @Override
    public void update(int spriteNum)
    {
        switch (getAction())
        {
            case _STANDING:
            {
                sprite.setRegion(app.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));
                elapsedAnimTime += Gdx.graphics.getDeltaTime();

                if (shootTimer.time(TimeUnit.MILLISECONDS) > shootInterval)
                {
                    app.missileBaseManager.shoot(this);

                    shootInterval = app.roomManager.getFireRate();
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
                setAction(Actions._DEAD);
                zapSprite.setAction(Actions._DEAD);
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
    public void tidy(int _index)
    {
        app.entityData.removeEntity(_index);
    }

    void explode()
    {
        ExplosionManager explosionManager = new ExplosionManager();
        explosionManager.createExplosion(GraphicID.G_EXPLOSION128, this, app);
        explosionManager.createExplosion(GraphicID.G_EXPLOSION64, zapSprite, app);

        Entities.explode(this);
        Entities.explode(zapSprite);
    }

    /**
     * Adds the spark zapper beam to the
     * defence station.
     */
    void addZapper()
    {
        SpriteDescriptor descriptor = Entities.getDescriptor(GraphicID.G_DEFENDER_ZAP);
        descriptor._PLAYMODE      = Animation.PlayMode.LOOP;
        descriptor._POSITION.x    = (int) (sprite.getX() / Gfx.getTileWidth());
        descriptor._POSITION.y    = (int) ((sprite.getY() + frameHeight) / Gfx.getTileHeight());
        descriptor._POSITION.z    = app.entityUtils.getInitialZPosition(GraphicID.G_DEFENDER_ZAP);
        descriptor._SIZE          = GameAssets.getAssetSize(GraphicID.G_DEFENDER_ZAP);
        descriptor._INDEX         = app.entityData.entityMap.size;

        zapSprite = new ZapSprite(GraphicID.G_DEFENDER_ZAP, this, app);
        zapSprite.initialise(descriptor);
        app.entityData.addEntity(zapSprite);
    }
}

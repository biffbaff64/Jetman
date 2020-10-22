package com.richikin.jetman.entities.characters;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.core.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.core.PointsManager;
import com.richikin.jetman.entities.Entities;
import com.richikin.jetman.entities.objects.GdxSprite;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.entities.managers.ExplosionManager;
import com.richikin.jetman.graphics.Gfx;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.graphics.camera.Shake;
import com.richikin.jetman.maths.SimpleVec2;
import com.richikin.jetman.utils.logging.Trace;

public class MissileBase extends GdxSprite
{
    public DefenceStation[] defenceStations;

    private GdxSprite     topSection;
    private TextureRegion emptyFrame;

    public MissileBase(App _app)
    {
        super(GraphicID.G_MISSILE_BASE, _app);
    }

    @Override
    public void initialise(SpriteDescriptor descriptor)
    {
        create(descriptor);

        initXYZ.set(sprite.getX(), sprite.getY(), zPosition);

        bodyCategory = Gfx.CAT_MISSILE_BASE;
        collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_VEHICLE | Gfx.CAT_PLAYER_WEAPON | Gfx.CAT_MOBILE_ENEMY;

        emptyFrame = app.assets.getAnimationRegion("launcher_top_empty");

        setAction(Actions._STANDING);

        addTopSection();

        app.gameProgress.baseDestroyed = false;
    }

    @Override
    public void update(int spriteNum)
    {
        topSection.preUpdate();

        switch(getAction())
        {
            case _STANDING:
            case _WAITING:
            case _EXPLODING:
            case _RESETTING:
            {
            }
            break;

            case _FIGHTING:
            {
                app.getHud().messageManager.addZoomMessage
                    (
                        "missilewarning",
                        2000,
                        185, (720 - 196)
                    );

                app.missileBaseManager.launch(topSection.sprite.getX(), topSection.sprite.getY());
                app.missileBaseManager.launch(topSection.sprite.getX(), topSection.sprite.getY());

                topSection.isFlippedX = (sprite.getX() < app.getPlayer().sprite.getX());

                setAction(Actions._WAITING);
            }
            break;

            case _HURT:
            {
                ExplosionManager explosionManager = new ExplosionManager();
                explosionManager.createExplosion(GraphicID.G_EXPLOSION256, this, app);
                explosionManager.createExplosion(GraphicID.G_EXPLOSION256, topSection, app);

                app.missileBaseManager.killMissiles();

                setAction(Actions._EXPLODING);

                Shake.start(app);
            }
            break;

            case _DYING:
            {
                Trace.__FILE_FUNC("---------- BASE DESTROYED ----------");

                app.getHud().messageManager.addZoomMessage("base_destroyed", 3000);

                app.gameProgress.baseDestroyed = true;

                app.gameProgress.score.add(PointsManager.getPoints(gid));

                setAction(Actions._DEAD);
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
        topSection.updateCommon();
    }

    @Override
    public void animate()
    {
        elapsedAnimTime += Gdx.graphics.getDeltaTime();
        sprite.setRegion(app.entityUtils.getKeyFrame(animation, elapsedAnimTime, true));

        if (topSection != null)
        {
            switch (getAction())
            {
                case _STANDING:
                {
                    topSection.elapsedAnimTime += Gdx.graphics.getDeltaTime();
                    topSection.sprite.setRegion(app.entityUtils.getKeyFrame
                        (
                            topSection.animation,
                            topSection.elapsedAnimTime,
                            true
                        ));
                }
                break;

                case _FIGHTING:
                case _WAITING:
                {
                    topSection.sprite.setRegion(emptyFrame);
                }
                break;

                default:
                {
                }
                break;
            }
        }
    }

    @Override
    public void draw(SpriteBatch spriteBatch)
    {
        topSection.sprite.setPosition(sprite.getX() + 15, sprite.getY() + this.frameHeight);

        super.draw(spriteBatch);
        topSection.draw(spriteBatch);
    }

    @Override
    public void setAction(Actions action)
    {
        super.setAction(action);

        if (topSection != null)
        {
            topSection.setAction(action);
        }
    }

    /**
     * Adds the launcher top section to the
     * missile base.
     */
    public void addTopSection()
    {
        Trace.__FILE_FUNC();

        SpriteDescriptor descriptor = Entities.getDescriptor(GraphicID.G_MISSILE_LAUNCHER);
        descriptor._PLAYMODE      = Animation.PlayMode.LOOP;
        descriptor._POSITION.x    = (int) (this.sprite.getX() / Gfx.getTileWidth());
        descriptor._POSITION.y    = (int) (this.sprite.getY() / Gfx.getTileHeight()) - 1;
        descriptor._POSITION.z    = app.entityUtils.getInitialZPosition(GraphicID.G_MISSILE_LAUNCHER);
        descriptor._INDEX         = app.entityData.entityMap.size;
        descriptor._SIZE          = GameAssets.getAssetSize(GraphicID.G_MISSILE_LAUNCHER);

        topSection = new GdxSprite(GraphicID.G_MISSILE_LAUNCHER, app);
        topSection.bodyCategory = Gfx.CAT_FIXED_ENEMY;
        topSection.collidesWith = Gfx.CAT_PLAYER | Gfx.CAT_PLAYER_WEAPON;
        topSection.create(descriptor);
        topSection.sprite.setPosition(topSection.sprite.getX() + 15, topSection.sprite.getY() + this.frameHeight);
        topSection.animation.setFrameDuration(0.5f / 6f);
        topSection.setAction(Actions._STANDING);

        app.entityData.addEntity(topSection);
    }

    /**
     * Adds the two defence stations either side
     * of the missile base.
     */
    public void addDefenceStations()
    {
        Trace.__FILE_FUNC();

        Array<SimpleVec2> vec2 = app.missileBaseManager.findMultiCoordinates(GraphicID.G_DEFENDER);

        //
        // Add a Defence Station to the left of the base
        SpriteDescriptor descriptor = Entities.getDescriptor(GraphicID.G_DEFENDER);
        descriptor._PLAYMODE      = Animation.PlayMode.LOOP;
        descriptor._POSITION.x    = vec2.get(0).x;
        descriptor._POSITION.y    = vec2.get(0).y;
        descriptor._POSITION.z    = app.entityUtils.getInitialZPosition(GraphicID.G_DEFENDER);
        descriptor._SIZE          = GameAssets.getAssetSize(GraphicID.G_DEFENDER);
        descriptor._INDEX         = app.entityData.entityMap.size;

        defenceStations = new DefenceStation[2];

        defenceStations[0] = new DefenceStation(app);
        defenceStations[0].initialise(descriptor);
        app.entityData.addEntity(defenceStations[0]);
        defenceStations[0].addZapper();

        //
        // Add a Defence Station to the right of the base
        descriptor._POSITION.x = vec2.get(1).x;
        descriptor._POSITION.y = vec2.get(1).y;

        defenceStations[1] = new DefenceStation(app);
        defenceStations[1].initialise(descriptor);
        app.entityData.addEntity(defenceStations[1]);
        defenceStations[1].addZapper();
    }

    @Override
    public void tidy(int _index)
    {
        app.missileBaseManager.free();
        app.entityData.removeEntity(_index);
    }
}

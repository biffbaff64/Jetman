package com.richikin.jetman.entities.managers;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.jetman.core.Actions;
import com.richikin.jetman.core.App;
import com.richikin.jetman.core.StateID;
import com.richikin.jetman.entities.SpriteDescriptor;
import com.richikin.jetman.entities.characters.Teleporter;
import com.richikin.jetman.entities.objects.TeleportBeam;
import com.richikin.jetman.graphics.GraphicID;
import com.richikin.jetman.maps.RoomManager;
import com.richikin.jetman.maths.SimpleVec2;
import com.richikin.jetman.maths.SimpleVec2F;
import com.richikin.jetman.physics.Movement;
import com.richikin.jetman.utils.Developer;
import com.richikin.jetman.utils.logging.Trace;

public class TeleportManager extends GenericEntityManager
{
    public Array<SimpleVec2> mapPositions;
    public int[]             index;
    public int               targetBooth;
    public boolean           teleportActive;
    public SimpleVec2F       targetDistance;
    public SimpleVec2        targetDirection;

    private SpriteDescriptor[] descriptors;

    public TeleportManager(App _app)
    {
        super(GraphicID.G_TRANSPORTER, _app);
    }

    @Override
    public void update()
    {
        if (activeCount < RoomManager._MAX_TELEPORTERS)
        {
            createTransporters();
        }
    }

    @Override
    public void free()
    {
        activeCount = 0;
    }

    private void createTransporters()
    {
        if (app.entityUtils.canUpdate(GraphicID.G_TRANSPORTER))
        {
            mapPositions   = new Array<>();
            activeCount    = 0;
            teleportActive = false;

            placeMarkerTiles();

            index = new int[2];

            for (int i = 0; i < 2; i++)
            {
                Teleporter teleporter = new Teleporter(app);
                teleporter.initialise(descriptors[i]);
                teleporter.teleporterNumber = activeCount;

                app.entityData.addEntity(teleporter);
                app.entityManager._teleportIndex[activeCount] = descriptors[i]._INDEX;

                // Store the map positions of each teleporter
                // for future use
                mapPositions.add(new SimpleVec2((int) teleporter.sprite.getX(), 0));

                index[activeCount] = descriptors[i]._INDEX;

                activeCount++;
            }
        }
    }

    private void placeMarkerTiles()
    {
        Array<SimpleVec2> coords = findMultiCoordinates(GraphicID.G_TRANSPORTER);

        descriptors = new SpriteDescriptor[2];

        descriptors[0]             = new SpriteDescriptor();
        descriptors[0]._ASSET      = GameAssets._TRANSPORTER_ASSET;
        descriptors[0]._FRAMES     = GameAssets._TRANSPORTER_FRAMES;
        descriptors[0]._PLAYMODE   = Animation.PlayMode.LOOP;
        descriptors[0]._POSITION.x = coords.get(0).x;
        descriptors[0]._POSITION.y = coords.get(0).y;
        descriptors[0]._POSITION.z = app.entityUtils.getInitialZPosition(GraphicID.G_TRANSPORTER);
        descriptors[0]._INDEX      = app.entityData.entityMap.size;
        descriptors[0]._SIZE       = GameAssets.getAssetSize(GraphicID.G_TRANSPORTER);

        descriptors[1] = new SpriteDescriptor(descriptors[0]);
        descriptors[1]._POSITION.x = coords.get(1).x;
        descriptors[1]._POSITION.y = coords.get(1).y;
    }

    public void teleportVisual(boolean _entered)
    {
        teleportActive                 = true;
        app.entityManager.teleportBeam = new TeleportBeam(app);

        if (_entered)
        {
            app.getHud().messageManager.enable();
            app.getHud().messageManager.addZoomMessage("TeleportMessage", 1500);

            app.entityManager.teleportBeam.entryVisual();

//            Sfx.inst().startSound(Sfx.inst().SFX_TELEPORT);
        }
        else
        {
            app.entityManager.teleportBeam.exitVisual();
        }
    }

    public void setTeleporting(int index)
    {
        Trace.__FILE_FUNC("index: " + index);

        targetDistance  = new SimpleVec2F();
        targetDirection = new SimpleVec2();

        app.appState.set(StateID._STATE_TELEPORTING);

        if (Developer.isDevMode())
        {
            Trace.dbg("Teleporter 1 X: " + app.getTeleporter(0).sprite.getX());
            Trace.dbg("Teleporter 2 X: " + app.getTeleporter(1).sprite.getX());
        }

        if (index == 0)
        {
            if (app.getTeleporter(0).sprite.getX() < app.getTeleporter(1).sprite.getX())
            {
                targetDirection.setX(Movement._DIRECTION_RIGHT);
                targetDistance.set
                    (
                        app.getTeleporter(1).sprite.getX() - app.getTeleporter(0).sprite.getX(),
                        Movement._DIRECTION_STILL
                    );
            }
            else
            {
                targetDirection.setX(Movement._DIRECTION_LEFT);
                targetDistance.set
                    (
                        app.getTeleporter(0).sprite.getX() - app.getTeleporter(1).sprite.getX(),
                        Movement._DIRECTION_STILL
                    );
            }

            targetBooth = 1;
        }
        else
        {
            if (app.getTeleporter(1).sprite.getX() > app.getTeleporter(0).sprite.getX())
            {
                targetDirection.setX(Movement._DIRECTION_LEFT);
                targetDistance.set
                    (
                        app.getTeleporter(1).sprite.getX() - app.getTeleporter(0).sprite.getX(),
                        Movement._DIRECTION_STILL
                    );
            }
            else
            {
                targetDirection.setX(Movement._DIRECTION_RIGHT);
                targetDistance.set
                    (
                        app.getTeleporter(0).sprite.getX() - app.getTeleporter(1).sprite.getX(),
                        Movement._DIRECTION_STILL
                    );
            }

            targetBooth = 0;
        }

        if (app.getTeleporter(targetBooth).sprite.getY() > app.getPlayer().sprite.getY())
        {
            targetDirection.setY(Movement._DIRECTION_UP);
        }
        else if (app.getTeleporter(targetBooth).sprite.getY() < app.getPlayer().sprite.getY())
        {
            targetDirection.setY(Movement._DIRECTION_DOWN);
        }

        app.getPlayer().teleport.start();

        if (Developer.isDevMode())
        {
            Trace.dbg("targetBooth: " + targetBooth);
            Trace.dbg("targetDirection: " + targetDirection);
            Trace.dbg("targetDistance: " + targetDistance);
            Trace.dbg("Teleport ready.");
        }
    }

    public void endTeleporting()
    {
        app.appState.set(StateID._STATE_GAME);

        app.getPlayer().isTeleporting = false;
        app.getPlayer().sprite.setX(app.getTeleporter(targetBooth).sprite.getX());
        app.getPlayer().sprite.setY(app.getTeleporter(targetBooth).sprite.getY());
        app.getPlayer().setAction(Actions._STANDING);
        app.getPlayer().actionButton.removeAction();

        teleportActive = false;

//        GdxSprite entity;
//
//        for (int i = 0; i < app.entityData.entityMap.size; i++)
//        {
//            entity = (GdxSprite) app.entityData.entityMap.get(i);
//
//            if ((entity != null) && (entity.gid == GraphicID.G_MESSAGE_BUBBLE))
//            {
//                if (((InfoBox) entity).parentID == GraphicID.G_TRANSPORTER)
//                {
//                    entity.spriteAction = Actions._DYING;
//                }
//            }
//        }
    }
}

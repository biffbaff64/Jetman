package com.richikin.jetman.entities.managers;

import com.badlogic.gdx.utils.Array;
import com.richikin.jetman.assets.GameAssets;
import com.richikin.enumslib.ActionStates;
import com.richikin.jetman.core.App;
import com.richikin.utilslib.states.StateID;
import com.richikin.jetman.entities.Entities;
import com.richikin.jetman.entities.objects.SpriteDescriptor;
import com.richikin.jetman.entities.characters.Teleporter;
import com.richikin.jetman.entities.objects.TeleportBeam;
import com.richikin.enumslib.GraphicID;
import com.richikin.jetman.maps.RoomManager;
import com.richikin.utilslib.maths.SimpleVec2;
import com.richikin.utilslib.maths.SimpleVec2F;
import com.richikin.utilslib.physics.Movement;
import com.richikin.utilslib.developer.Developer;
import com.richikin.utilslib.logging.Trace;

public class TeleportManager extends GenericEntityManager
{
    public Array<SimpleVec2> mapPositions;
    public int[]             index;
    public int               targetBooth;
    public boolean           teleportActive;
    public SimpleVec2F       targetDistance;
    public SimpleVec2        targetDirection;

    public TeleportManager(App _app)
    {
        super(GraphicID.G_TRANSPORTER, _app);
    }

    @Override
    public void init()
    {
        activeCount = 0;

        createTransporters();
    }

    @Override
    public void update()
    {
        if (activeCount == 0)
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
            Trace.__FILE_FUNC();

            mapPositions   = new Array<>();
            activeCount    = 0;
            teleportActive = false;

            Array<SimpleVec2> coords = findMultiCoordinates(GraphicID.G_TRANSPORTER);

            if (coords.isEmpty())
            {
                Trace.__FILE_FUNC("WARNING: Transporter count ZERO");
            }
            else
            {
                SpriteDescriptor descriptor;
                index       = new int[RoomManager._MAX_TELEPORTERS];

                for (int i = 0; i < RoomManager._MAX_TELEPORTERS; i++)
                {
                    descriptor             = Entities.getDescriptor(GraphicID.G_TRANSPORTER);
                    descriptor._SIZE       = GameAssets.getAssetSize(GraphicID.G_TRANSPORTER);
                    descriptor._POSITION.x = coords.get(i).x;
                    descriptor._POSITION.y = coords.get(i).y;
                    descriptor._POSITION.z = app.entityUtils.getInitialZPosition(GraphicID.G_TRANSPORTER);
                    descriptor._INDEX      = app.entityData.entityMap.size;

                    Teleporter teleporter = new Teleporter(app);
                    teleporter.initialise(descriptor);
                    teleporter.teleporterNumber = activeCount;

                    app.entityData.addEntity(teleporter);
                    app.entityManager._teleportIndex[activeCount] = descriptor._INDEX;

                    //
                    // Store the map positions of each teleporter
                    // for future use
                    mapPositions.add(new SimpleVec2((int) teleporter.sprite.getX(), 0));

                    index[activeCount] = descriptor._INDEX;

                    activeCount++;
                }
            }
        }
    }

    public void teleportVisual(boolean _entered)
    {
        teleportActive                 = true;
        app.entityManager.teleportBeam = new TeleportBeam(app);

        if (_entered)
        {
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
            for (int i=0; i<RoomManager._MAX_TELEPORTERS; i++)
            {
                Trace.dbg("Teleporter " + i + " X: " + app.getTeleporter(i).sprite.getX());
            }
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
        app.getPlayer().setAction(ActionStates._STANDING);
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

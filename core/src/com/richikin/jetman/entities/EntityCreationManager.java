package com.richikin.jetman.entities;

import com.richikin.jetman.core.App;
import com.richikin.jetman.entities.Entities;
import com.richikin.jetman.entities.SpriteDescriptor;
import com.richikin.jetman.entities.managers.PlayerManager;

public class EntityCreationManager
{
    private SpriteDescriptor spriteDescriptor;
    private final App app;

    public EntityCreationManager(App _app)
    {
        this.app = _app;
    }

    public void processPlacementTiles()
    {
        for (SpriteDescriptor descriptor : app.mapData.placementTiles)
        {
            spriteDescriptor = Entities.getDescriptor(descriptor._GID);

            switch (descriptor._GID)
            {
                case G_PLAYER:
                {
                    addPlayer();
                }
                break;

                case G_ROVER:
                {
                    addRover();
                }
                break;

                case G_TRANSPORTER:
                {
                    addTeleporters();
                }
                break;

                case G_MISSILE_BASE:
                {
                    addMissileBase();
                }
                break;

                default:
                    break;
            }
        }
    }

    private void addPlayer()
    {
        app.entityManager.playerManager = new PlayerManager(app);
        app.entityManager.playerManager.setSpawnPoint();
        app.entityManager.playerManager.createPlayer();
    }

    private void addRover()
    {
    }

    private void addTeleporters()
    {
    }

    private void addMissileBase()
    {
    }
}

package com.richikin.jetman.entities;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.jetman.entities.components.EntityManagerComponent;
import com.richikin.jetman.entities.objects.GameEntity;
import com.richikin.jetman.utils.logging.Trace;

import java.util.ArrayList;

public class EntityData implements Disposable
{
    public Array<GameEntity>                 entityMap;
    public ArrayList<EntityManagerComponent> managerList;

    public EntityData()
    {
    }

    public void createData()
    {
        Trace.__FILE_FUNC();

        entityMap   = new Array<>();
        managerList = new ArrayList<>();
    }

    public void update()
    {
        //
        // Scan through the entity map and update the
        // number of active entities for each type.
        int thisCount;

        for (final EntityManagerComponent managerComponent : managerList)
        {
            thisCount = 0;

            for (GameEntity gameEntity : new Array.ArrayIterator<>(entityMap))
            {
                if (gameEntity.gid == managerComponent.getGID())
                {
                    thisCount++;
                }
            }

            managerComponent.setActiveCount(thisCount);
        }
    }

    public int addManager(EntityManagerComponent manager)
    {
        managerList.add(manager);

        return managerList.size() - 1;
    }

    public void addEntity(GameEntity _entity)
    {
        if (_entity != null)
        {
            entityMap.add(_entity);
        }
        else
        {
            Trace.__FILE_FUNC("***** Attempt to add NULL Object, EntityMap current size: " + entityMap.size);
        }
    }

    public void removeEntity(int index)
    {
        entityMap.removeIndex(index);
    }

    @Override
    public void dispose()
    {
        for (int i = 0; i < entityMap.size; i++)
        {
            if (entityMap.get(i) != null)
            {
                entityMap.get(i).dispose();
            }
        }

        entityMap.clear();
        managerList.clear();

        entityMap   = null;
        managerList = null;
    }
}

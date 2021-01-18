package com.richikin.utilslib.google;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.richikin.utilslib.maths.Vec2String;

public class PlayServicesData implements Disposable
{
    private Array<Vec2String> achievementsIDs;
    private Array<Vec2String> leaderBoardIDs;

    public PlayServicesData()
    {
        this.achievementsIDs = new Array<>();
        this.leaderBoardIDs = new Array<>();
    }

    public void addAchievementID(String idName, String idValue)
    {
        if (achievementsIDs == null)
        {
            achievementsIDs = new Array<>();
        }

        achievementsIDs.add(new Vec2String(idName, idValue));
    }

    public void addLeaderBoardID(String idName, String idValue)
    {
        if (leaderBoardIDs == null)
        {
            leaderBoardIDs = new Array<>();
        }

        leaderBoardIDs.add(new Vec2String(idName, idValue));
    }

    @Override
    public void dispose()
    {
        this.achievementsIDs.clear();
        this.leaderBoardIDs.clear();

        this.achievementsIDs = null;
        this.leaderBoardIDs = null;
    }
}

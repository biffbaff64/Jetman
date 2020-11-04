
package com.richikin.utilslib.google;

import com.richikin.utilslib.logging.Trace;

public class PlayServicesDesktop implements IPlayServices
{
    @Override
    public void setup()
    {
    }

    @Override
    public void createApiClient()
    {
        Trace.__FILE_FUNC("Desktop: Services not enabled1");
    }

    @Override
    public void signIn()
    {
        Trace.__FILE_FUNC("Desktop: Services not enabled1");
    }

    @Override
    public void signInSilently()
    {
        Trace.__FILE_FUNC("Desktop: Services not enabled1");
    }

    @Override
    public void signOut()
    {
        Trace.__FILE_FUNC("Desktop: Services not enabled1");
    }

    @Override
    public boolean isSignedIn()
    {
        return false;
    }

    @Override
    public boolean isEnabled()
    {
        return false;
    }

    @Override
    public void submitScore(int score, int level)
    {
        Trace.__FILE_FUNC("Desktop: Services not enabled1 : " + score + ", " + level);
    }

    @Override
    public void unlockAchievement(String achievementId)
    {
        Trace.__FILE_FUNC("Desktop: Services not enabled1 : " + achievementId);
    }

    @Override
    public void showAchievementScreen()
    {
        Trace.__FILE_FUNC("Desktop: Services not enabled1");
    }

    @Override
    public void showLeaderboard()
    {
        Trace.__FILE_FUNC("Desktop: Services not enabled1");
    }
}


package com.richikin.utilslib.google;

import com.richikin.jetman.config.AppConfig;
import com.richikin.jetman.config.Settings;
import com.richikin.jetman.core.App;
import com.richikin.utilslib.logging.Trace;

public class PlayServicesDesktop implements IPlayServices
{
    private App app;

    @Override
    public void setup(App _app)
    {
        this.app = _app;
    }

    @Override
    public void createApiClient()
    {
        Trace.__FILE_FUNC();
    }

    @Override
    public void signIn()
    {
        if (isEnabled())
        {
            if (!app.settings.isEnabled(Settings._SIGN_IN_STATUS))
            {
                Trace.__FILE_FUNC();

                app.settings.enable(Settings._SIGN_IN_STATUS);
            }
        }
    }

    @Override
    public void signInSilently()
    {
        if (isEnabled())
        {
            Trace.__FILE_FUNC();

            signIn();
        }
    }

    @Override
    public void signOut()
    {
        if (isEnabled())
        {
            Trace.__FILE_FUNC();

            app.settings.disable(Settings._SIGN_IN_STATUS);
        }
    }

    @Override
    public boolean isSignedIn()
    {
        return isEnabled() && app.settings.isEnabled(Settings._SIGN_IN_STATUS);
    }

    @Override
    public boolean isEnabled()
    {
        return AppConfig.isAndroidApp() && app.settings.isEnabled(Settings._PLAY_SERVICES);
    }

    @Override
    public void submitScore(int score, int level)
    {
        if (isEnabled())
        {
            Trace.__FILE_FUNC("" + score + ", " + level);
        }
    }

    @Override
    public void unlockAchievement(String achievementId)
    {
    }

    @Override
    public void showAchievementScreen()
    {
        Trace.__FILE_FUNC();
    }

    @Override
    public void showLeaderboard()
    {
        Trace.__FILE_FUNC();
    }
}

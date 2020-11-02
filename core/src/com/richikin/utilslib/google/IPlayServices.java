
package com.richikin.utilslib.google;

import com.richikin.jetman.core.App;

public interface IPlayServices
{
    void setup(App _app);

    void createApiClient();

    void signIn();

    void signInSilently();

    void signOut();

    boolean isSignedIn();

    boolean isEnabled();

    void submitScore(int score, int level);

    void unlockAchievement(String achievementId);

    void showAchievementScreen();

    void showLeaderboard();
}


package com.richikin.jetman.utils.google;

import com.richikin.jetman.core.App;

public interface PlayServices
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


package com.richikin.utilslib.google;

public interface IPlayServices
{
    void setup();

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


package com.richikin.jetman;

import android.content.Intent;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.games.Games;
import com.google.android.gms.tasks.OnSuccessListener;
import com.richikin.utilslib.google.RCConstants;

public class AchievementsHandler implements OnSuccessListener<Intent>
{
    private final AndroidLauncher androidLauncher;

    AchievementsHandler(AndroidLauncher _androidLauncher)
    {
        this.androidLauncher = _androidLauncher;
    }

    @Override
    public void onSuccess(final Intent intent)
    {
        androidLauncher.startActivityForResult(intent, RCConstants.RC_ACHIEVEMENT_UI.value);
    }

    /**
     * Unlock the specified achievement.
     *
     * @param achievementId - String - The achievement ID.
     */
    void unlockAchievement(final String achievementId)
    {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(androidLauncher);

        if (account != null)
        {
            Games.getAchievementsClient(androidLauncher, account).unlock(achievementId);
        }
    }

    /**
     * Shows the achievements screen.
     */
    void showAchievementScreen()
    {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(androidLauncher);

        if (account != null)
        {
            Games.getAchievementsClient(androidLauncher, account)
                .getAchievementsIntent()
                .addOnSuccessListener(this);
        }
    }
}

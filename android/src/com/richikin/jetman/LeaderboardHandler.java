package com.richikin.jetman;

import android.content.Intent;
import com.google.android.gms.tasks.OnSuccessListener;
import com.richikin.jetman.utils.google.RCConstants;

class LeaderboardHandler implements OnSuccessListener<Intent>
{
    private final AndroidLauncher androidLauncher;

    LeaderboardHandler(AndroidLauncher _androidLauncher)
    {
        this.androidLauncher = _androidLauncher;
    }

    @Override
    public void onSuccess(final Intent intent)
    {
        androidLauncher.startActivityForResult(intent, RCConstants.RC_LEADERBOARD_UI.value);
    }

    /**
     * Submit score and level achieved.
     *
     * @param score - int - The score.
     * @param level - int - The level.
     */
    void submitScore(int score, int level)
    {
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(androidLauncher);
//
//        if (account != null)
//        {
//            Trace.__FILE_FUNC(androidLauncher.getString(R.string.leaderboard_leaderboard_tester) + ": " + score);
//
//            Games.getLeaderboardsClient(androidLauncher, account)
//            .submitScore(androidLauncher.getString(R.string.leaderboard_leaderboard_tester), score);
//        }
    }

    /**
     * Shows the global Leaderboard.
     */
    void showLeaderBoard()
    {
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(androidLauncher);
//
//        if (account != null)
//        {
//            Games.getLeaderboardsClient(androidLauncher, account)
//            .getLeaderboardIntent(androidLauncher.getString(R.string.leaderboard_leaderboard))
//            .addOnSuccessListener(this);
//        }
    }
}

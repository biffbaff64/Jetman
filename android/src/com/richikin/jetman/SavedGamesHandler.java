package com.richikin.jetman;

import android.content.Intent;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.games.Games;
import com.google.android.gms.tasks.OnSuccessListener;
import com.richikin.utilslib.google.RCConstants;

public class SavedGamesHandler implements OnSuccessListener<Intent>
{
    private final AndroidLauncher androidLauncher;

    SavedGamesHandler(AndroidLauncher _androidLauncher)
    {
        this.androidLauncher = _androidLauncher;
    }

    @Override
    public void onSuccess(final Intent intent)
    {
        androidLauncher.startActivityForResult(intent, RCConstants.RC_SAVED_GAMES_UI.value);
    }

    void showSavedGamesUI()
    {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(androidLauncher);

        if (account != null)
        {
            int maxNumberOfSavedGamesToShow = 5;

            Games.getSnapshotsClient(androidLauncher, account).getSelectSnapshotIntent
                    (
                            "Saved Games List",
                            true,
                            true,
                            maxNumberOfSavedGamesToShow
                    );
        }
    }
}

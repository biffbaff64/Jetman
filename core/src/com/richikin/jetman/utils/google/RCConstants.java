
package com.richikin.jetman.utils.google;

public enum RCConstants
{
    RC_SIGN_IN          (9001),
    RC_UNALLOCATED_2    (9002),
    RC_ACHIEVEMENT_UI   (9003),
    RC_LEADERBOARD_UI   (9004),
    RC_UNALLOCATED_5    (9005),
    RC_SELECT_PLAYERS   (9006),
    RC_WAITING_ROOM     (9007),
    RC_INVITATION_INBOX (9008),
    RC_SAVED_GAMES_UI   (9009),
    RC_UNALLOCATED_10   (9010);

    public final int value;

    RCConstants(int _value)
    {
        value = _value;
    }
}

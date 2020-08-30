
package com.richikin.jetman.utils.google;

public enum PlayServicesID
{
    // -----------------------------------------------
    // The codes for achievements and leaderboards will be
    // given via google play developers console when they
    // are submitted.

    // achievements
    achievement_1    ("CgkIvdearaEYEAIQBA"),
    achievement_2    ("CgkIvdearaEYEAIQBQ"),
    achievement_3    ("CgkIvdearaEYEAIQBg"),
    achievement_4    ("CgkIvdearaEYEAIQBw"),
    achievement_5    ("CgkIvdearaEYEAIQCA"),
    achievement_6    ("CgkIvdearaEYEAIQCQ"),
    achievement_7    ("CgkIvdearaEYEAIQDQ"),
    achievement_8    ("CgkIvdearaEYEAIQCg"),
    achievement_9    ("CgkIvdearaEYEAIQCw"),
    achievement_10   ("CgkIvdearaEYEAIQDA"),
    achievement_11   ("CgkIvdearaEYEAIQDg"),

    // leaderboard High Scores
    leaderboard_leaderboard         ("CgkIvdearaEYEAIQAQ"),
    leaderboard_leaderboard_tester  ("CgkIvdearaEYEAIQAw");

    final String ID;

    PlayServicesID(String _ID)
    {
        this.ID = _ID;
    }

    public String getID()
    {
        return ID;
    }
}

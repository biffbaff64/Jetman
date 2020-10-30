
package com.richikin.utilslib.google;

public enum PlayServicesID
{
    // -----------------------------------------------
    // The codes for achievements and leaderboards will be
    // given via google play developers console when they
    // are submitted.

    // TODO: 30/10/2020 - This should be a map, and initialised from the game code.

    // achievements
    achievement_base_destroyed      ("CgkIvdearaEYEAIQBA"),
    achievement_millionaire         ("CgkIvdearaEYEAIQBQ"),
    achievement_score_500k          ("CgkIvdearaEYEAIQBg"),
    achievement_score_100k          ("CgkIvdearaEYEAIQBw"),
    achievement_shoot_a_missile     ("CgkIvdearaEYEAIQCA"),
    achievement_courier_services    ("CgkIvdearaEYEAIQCQ"),
    achievement_bridge_building     ("CgkIvdearaEYEAIQDQ"),
    achievement_beam_me_up          ("CgkIvdearaEYEAIQCg"),
    achievement_gunman_jetman       ("CgkIvdearaEYEAIQCw"),
    achievement_bomb_collector      ("CgkIvdearaEYEAIQDA"),
    achievement_moon_rider          ("CgkIvdearaEYEAIQDg"),

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

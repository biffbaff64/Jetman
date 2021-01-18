package com.richikin.jetman.core;

public class LocalPlayServicesData
{
    private final String[][] achievements =
        {
            { "achievement_base_destroyed",      "CgkIvdearaEYEAIQBA" },
            { "achievement_millionaire",         "CgkIvdearaEYEAIQBQ" },
            { "achievement_score_500k",          "CgkIvdearaEYEAIQBg" },
            { "achievement_score_100k",          "CgkIvdearaEYEAIQBw" },
            { "achievement_shoot_a_missile",     "CgkIvdearaEYEAIQCA" },
            { "achievement_courier_services",    "CgkIvdearaEYEAIQCQ" },
            { "achievement_bridge_building",     "CgkIvdearaEYEAIQDQ" },
            { "achievement_beam_me_up",          "CgkIvdearaEYEAIQCg" },
            { "achievement_gunman_jetman",       "CgkIvdearaEYEAIQCw" },
            { "achievement_bomb_collector",      "CgkIvdearaEYEAIQDA" },
            { "achievement_moon_rider",          "CgkIvdearaEYEAIQDg" },
        };

    private final String[][] leaderboards =
        {
            { "leaderboard_leaderboard",        "CgkIvdearaEYEAIQAQ" },
            { "leaderboard_leaderboard_tester", "CgkIvdearaEYEAIQAw" },
        };

    public LocalPlayServicesData()
    {
    }

    public void setup()
    {
        for (String[] achievement : achievements)
        {
            App.playServicesData.addAchievementID(achievement[0], achievement[1]);
        }

        for (String[] leaderboard : leaderboards)
        {
            App.playServicesData.addLeaderBoardID(leaderboard[0], leaderboard[1]);
        }
    }
}

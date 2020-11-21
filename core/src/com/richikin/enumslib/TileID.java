
package com.richikin.enumslib;

public enum TileID
{
    _DEFAULT_TILE(0),

    // Row 1
    _PLAYER_TILE                (1),
    _TRANSPORTER_TILE           (2),
    _MISSILE_LAUNCHER_TILE      (3),
    _DEFENDER_TILE              (4),
    _STAIRCLIMBER_TILE          (5),
    _3BALLS_UFO_TILE            (6),
    _3LEGS_ALIEN_TILE           (7),
    _TOPSPIN_TILE               (8),
    _TWINKLES_TILE              (9),
    _STAR_SPINNER_TILE          (10),

    // Row 2
    _ASTEROID_TILE              (11),
    _BEAM_TILE                  (12),
    _DOG_TILE                   (13),
    _GREEN_BLOCK_TILE           (14),
    _SPINNING_BALL_TILE         (15),
    _BLOB_TILE                  (16),
    _ROVER_TILE                 (17),
    _BOMB_TILE                  (18),
    _MISSILE_BASE_TILE          (19),
    _BARRIER_BASE_TILE          (20),

    // Row 3
    _CRATER_TILE                (21),
    _ALIEN_WHEEL_TILE           (22),
    _ROVER_GUN_TILE             (23),
    _ROCKET_TILE                (24),
    _BRIDGE_TILE                (25),
    _TOWER_TILE                 (26),
    _U27_TILE                   (27),
    _U28_TILE                   (28),
    _U29_TILE                   (29),
    _U30_TILE                   (30),

    // Row 4
    _U31_TILE                   (31),
    _U32_TILE                   (32),
    _U33_TILE                   (33),
    _U34_TILE                   (34),
    _U35_TILE                   (35),
    _U36_TILE                   (36),
    _U37_TILE                   (37),
    _U38_TILE                   (38),
    _U39_TILE                   (39),
    _U40_TILE                   (40),

    // Row 5
    _U41_TILE                   (41),
    _U42_TILE                   (42),
    _U43_TILE                   (43),
    _U44_TILE                   (44),
    _U45_TILE                   (45),
    _U46_TILE                   (46),
    _U47_TILE                   (47),
    _U48_TILE                   (48),
    _U49_TILE                   (49),
    _U50_TILE                   (50),

    // Row 6
    _U51_TILE                   (51),
    _U52_TILE                   (52),
    _U53_TILE                   (53),
    _U54_TILE                   (54),
    _U55_TILE                   (55),
    _U56_TILE                   (56),
    _U57_TILE                   (57),
    _U58_TILE                   (58),
    _U59_TILE                   (59),
    _U60_TILE                   (60),

    // Row 7
    _U61_TILE                   (61),
    _U62_TILE                   (62),
    _U63_TILE                   (63),
    _U64_TILE                   (64),
    _U65_TILE                   (65),
    _U66_TILE                   (66),
    _U67_TILE                   (67),
    _U68_TILE                   (68),
    _U69_TILE                   (69),
    _U70_TILE                   (70),

    // Row 8
    _U71_TILE                   (71),
    _U72_TILE                   (72),
    _U73_TILE                   (73),
    _U74_TILE                   (74),
    _U75_TILE                   (75),
    _U76_TILE                   (76),
    _U77_TILE                   (77),
    _U78_TILE                   (78),
    _U79_TILE                   (79),
    _U80_TILE                   (80),

    // Row 9
    _U81_TILE                   (81),
    _U82_TILE                   (82),
    _U83_TILE                   (83),
    _U84_TILE                   (84),
    _U85_TILE                   (85),
    _U86_TILE                   (86),
    _U87_TILE                   (87),
    _U88_TILE                   (88),
    _U89_TILE                   (89),
    _U90_TILE                   (90),

    // Row 10
    _U91_TILE                   (91),
    _U92_TILE                   (92),
    _U93_TILE                   (93),
    _DEFENDER_ZAP_TILE          (94),
    _U95_TILE                   (95),
    _U96_TILE                   (96),
    _U97_TILE                   (97),
    _U98_TILE                   (98),
    _BLANK_TILE                 (99),
    _NO_ACTION_TILE             (100),

    // Tile IDs for entities that are never
    // placed into a tilemap, but are created
    // programmatically.
    _ROVER_WHEEL_TILE           (110),
    _ROVER_BOOT_TILE            (111),
    _EXPLOSION_TILE             (112),
    _LASER_TILE                 (113),
    _UFO_BULLET_TILE            (114),
    _ROVER_BULLET_TILE          (115),
    _TRANSPORTER_BEAM_TILE      (116),
    _TWINKLE_STAR_TILE          (117),
    _BACKGROUND_UFO_TILE        (118),
    _DEFENDER_BULLET_TILE       (119),

    // Tile IDs that are used in path finding
    // These tiles may not necessarily have images
    // defined in the marker_tiles png.
    _GROUND                     (150),
    _HOLE                       (151),
    _WATER                      (152),
    _GRASS                      (153),
    _WALL_EDGE                  (154),

    _UNKNOWN(255);

    private final int tileNumber;

    TileID(int value)
    {
        this.tileNumber = value;
    }

    public int get()
    {
        return tileNumber;
    }

    public static TileID fromValue(int value)
    {
        TileID returnValue = _UNKNOWN;

        for (TileID tileID : values())
        {
            if (tileID.get() == value)
            {
                returnValue = tileID;
            }
        }

        return returnValue;
    }
}

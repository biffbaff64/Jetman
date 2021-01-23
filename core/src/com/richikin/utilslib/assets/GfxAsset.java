
package com.richikin.utilslib.assets;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.richikin.enumslib.GraphicID;

/**
 * Creates an object describing essential asset data for entities.
 * Matches the following:-
 * 1. The GraphicID of the entity.
 * 2. A String holding the asset name.
 * 3. A String holding the enabled/disabled Preference flag.
 * 4. An int holding the number of animation frames.
 * 5. The default Animation Playmode.
 */
public class GfxAsset
{
    public final GraphicID graphicID;
    public final String    asset;
    public final String    preference;
    public final int                frames;
    public final Animation.PlayMode playMode;

    public GfxAsset(final GraphicID _gid, final String _preference, final String _asset)
    {
        this.graphicID  = _gid;
        this.preference = _preference;
        this.asset      = _asset;
        this.frames     = 1;
        this.playMode   = Animation.PlayMode.NORMAL;
    }

    public GfxAsset(final GraphicID _gid, final String _preference, final String _asset, int _frames)
    {
        this.graphicID  = _gid;
        this.preference = _preference;
        this.asset      = _asset;
        this.frames     = _frames;
        this.playMode   = Animation.PlayMode.NORMAL;
    }

    public GfxAsset(final GraphicID _gid, final String _preference, final String _asset, int _frames, Animation.PlayMode _playmode)
    {
        this.graphicID  = _gid;
        this.preference = _preference;
        this.asset      = _asset;
        this.frames     = _frames;
        this.playMode   = _playmode;
    }
}

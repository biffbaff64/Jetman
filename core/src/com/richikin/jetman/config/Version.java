
package com.richikin.jetman.config;

import com.richikin.jetman.core.App;
import com.richikin.utilslib.logging.Trace;

/**
 * Major Version - 0 == Development Version
 *               - 1 == Alpha release
 *               - 2 == Beta release
 *               - 3 == Master release
 *
 * Minor Version - 0 ==
 *               - 1 ==
 *               - 2 ==
 *               - etc...
 *
 * App Version details
 * ------------------------------------------------------------------
 * @version 0.0.1 Internal       initial issue
 */
@SuppressWarnings({"SameReturnValue", "WeakerAccess"})
public final class Version
{
    public static final int majorVersion    = 0;
    public static final int minorVersion    = 0;
    public static final int issueNumber     = 2;

    static final String appVersion  = "" + majorVersion + "." + minorVersion + "." + issueNumber;
    static final String projectID   = "Jetman";
    static final String googleAppID = "146820815538";

    //
    // Release Version
    static final String clientID    = "146820815538-aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.apps.googleusercontent.com";
    static final String sha1        = "00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00";

    //
    // Debug Version
    static final String clientID_debug = "146820815538-aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa.apps.googleusercontent.com";
    static final String sha1_debug     = "00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00:00";

    //
    // Google Base64-encoded RSA public key
    static final String googleRsaPublicKey = "ABCDefghIJKLmnopQRSTuvwxYZ";

    // ------------------------------------------------------

    public static void appDetails()
    {
        Trace.divider('*', 80);
        Trace.divider(80);

        Trace.dbg(getDisplayVersion());
        if (AppConfig.isAndroidApp())
        {
            Trace.dbg("Signed in to Google?: " + App.googleServices.isSignedIn());
        }

        Trace.divider(80);
        Trace.divider('*', 80);
    }

    /**
     * Gets the app Version string for displaying on the settings screen
     *
     * @return  String holding the version details.
     */
    public static String getDisplayVersion()
    {
        return "Version  " + googleAppID + " : " + appVersion + " : " + projectID;
    }

    /**
     * Gets the app Version string
     *
     * @return  String holding the version details.
     */
    public static String getAppVersion()
    {
        return "V." + appVersion;
    }

    public static String getProjectID()
    {
        return projectID;
    }
}

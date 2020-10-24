
package com.richikin.utilslib.google;

import com.richikin.utilslib.logging.Trace;

public class DummyAdsController implements AdsController
{
    @Override
    public void showBannerAd()
    {
        Trace.__FILE_FUNC();
    }

    @Override
    public void hideBannerAd()
    {
        Trace.__FILE_FUNC();
    }

    @Override
    public boolean isWifiConnected()
    {
        Trace.__FILE_FUNC();

        return false;
    }

    @Override
    public void showInterstitialAd(Runnable then)
    {
        Trace.__FILE_FUNC();

        if (then == null)
        {
            Trace.dbg("Runnable parameter is NULL");
        }
    }
}

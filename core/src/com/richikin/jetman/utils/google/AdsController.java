
package com.richikin.jetman.utils.google;

@SuppressWarnings({"SameReturnValue", "unused"})
public interface AdsController
{
    void showBannerAd();

    void hideBannerAd();

    void showInterstitialAd(Runnable runnable);

    boolean isWifiConnected();
}

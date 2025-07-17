
package com.example.pregnancytrackerignite.domain.managers;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.cubesolver.Managers.Constants;
import com.example.pregnancytrackerignite.R;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.RequestConfiguration;
import com.google.android.gms.ads.VideoController;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.initialization.AdapterStatus;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.example.pregnancytrackerignite.di.myApplication.MyApplication;

import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.hilt.android.qualifiers.ApplicationContext;

@Singleton
public class AdsManager {

    public enum NativeAdType {
        REGULAR_TYPE, MEDIUM_TYPE, SMALL_TYPE, NOMEDIA_MEDIUM
    }

    private InterstitialAd admobInterstitialAd;
    private final String TAG = AdsManager.class.getName();
    private final PreferencesManager preferenceManager;

    private RewardedAd mRewardedAd;
    private AlertDialog alertDialogAds;

    public void showAdLoadingDialog(Activity context) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        View dialogView = LayoutInflater.from(context).inflate(R.layout.layout_ad_loading, null);
        dialogBuilder.setView(dialogView);
        alertDialogAds = dialogBuilder.create();
        alertDialogAds.setCancelable(false);
        alertDialogAds.show();
        if (alertDialogAds.getWindow() != null) {
            alertDialogAds.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    public void hideDialogAds() {
        if (alertDialogAds != null && alertDialogAds.isShowing()) {
            alertDialogAds.dismiss();
        }
    }


    @Inject
    public AdsManager(@ApplicationContext Context context, PreferencesManager preferenceManager) {

        this.preferenceManager = preferenceManager;
    }

    public void initSDK(Context context, @NotNull ISDKinit isdKinit) {
        MobileAds.initialize(context, initializationStatus -> {
            Log.d(TAG, "AdsManager: initializes");

            // for bidding
            Map<String, AdapterStatus> statusMap = initializationStatus.getAdapterStatusMap();
            for (String adapterClass : statusMap.keySet()) {
                AdapterStatus status = statusMap.get(adapterClass);
                Log.d("MyApp", String.format(
                        "Adapter name: %s, Description: %s, Latency: %d",
                        adapterClass, status.getDescription(), status.getLatency()));
            }

        });
        MobileAds.setRequestConfiguration(
                new RequestConfiguration.Builder().setTestDeviceIds(Arrays.asList("F58A121CA6B48BE487E841B65137F635",
                                "F489CD7B6E9F529144AEEBD5D32D3417", "47BD8D71811EF0D2F25B46868A922D56", "32F9DA89E49FBDF09C1F69F89FF071E0"))
                        .build());

        // Initialize the Mobile Ads SDK.
        MobileAds.initialize(context, initializationStatus -> {
            Log.d(TAG, "AdsManager: initializes");
            isdKinit.onInitialized();
        });
    }


    private AdRequest prepareAdRequest() {
        AdRequest adRequest;
        adRequest = new AdRequest.Builder().build();
        return adRequest;
    }


    public void loadInterstitialAd(Activity activity, Runnable funcToInvoke) {
        if (!preferenceManager.getBoolean(PreferencesManager.Key.IS_APP_ADS_FREE, false)) {

            if (isNetWorkAvailable(activity)) {
                showAdLoadingDialog(activity);
                if (admobInterstitialAd != null) {
                    showInterstitialAd(activity, funcToInvoke);
                    return;
                }
                // Create a timer for a 10-second timeout
                final Handler handler = new Handler();
                Runnable timeoutRunnable = () -> {
                    hideDialogAds();
                    funcToInvoke.run();
                };
                handler.postDelayed(timeoutRunnable, 10000); // Delay for 10 seconds


                InterstitialAdLoadCallback interstitialAdLoadCallback = new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        super.onAdLoaded(interstitialAd);
                        Log.d(TAG, "onLoad: admob interstitial");
                        handler.removeCallbacks(timeoutRunnable); // Remove timer if ad loads
                        admobInterstitialAd = interstitialAd;
                        showInterstitialAd(activity, funcToInvoke);
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        Log.d(TAG, "onAdFailedToLoad: admob interstitial. Loading facebook ad");
                        handler.removeCallbacks(timeoutRunnable); // Remove timer on failure
                        hideDialogAds();
                        funcToInvoke.run();
                    }
                };
                InterstitialAd.load(activity, activity.getString(R.string.ADMOB_INTERSTITIAL_V2), prepareAdRequest(), interstitialAdLoadCallback);
            } else {
                funcToInvoke.run();
            }
        } else {
            funcToInvoke.run();
        }
    }

    public void showInterstitialAd(Activity activity, Runnable funcAfterAdHidden) {
        if (admobInterstitialAd != null) {
            admobInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                @Override
                public void onAdDismissedFullScreenContent() {
                    super.onAdDismissedFullScreenContent();
                    funcAfterAdHidden.run();
                    Log.d(TAG, "onAdDismissedFullScreenContent: ");
                    admobInterstitialAd = null;
                    MyApplication.mInstance.setInterShowing(false);
                }

                @Override
                public void onAdFailedToShowFullScreenContent(@NonNull AdError adError) {
                    hideDialogAds();
                    super.onAdFailedToShowFullScreenContent(adError);
                    Log.d(TAG, "onAdFailedToShowFullScreenContent: ");
                    funcAfterAdHidden.run();
                }

                @Override
                public void onAdShowedFullScreenContent() {
                    hideDialogAds();
                    MyApplication.mInstance.setInterShowing(true);
                    super.onAdShowedFullScreenContent();
                }
            });
            admobInterstitialAd.show(activity);
        } else {
            hideDialogAds();
            funcAfterAdHidden.run();
        }
    }

    //------------------------------------------ Non loading -------------------------------------//
    public void loadInterstitialAdWithoutLoading(Activity activity) {
        if (!preferenceManager.getBoolean(PreferencesManager.Key.IS_APP_ADS_FREE, false)) {
            if (isNetWorkAvailable(activity)) {
                InterstitialAdLoadCallback interstitialAdLoadCallback = new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        super.onAdLoaded(interstitialAd);
                        Log.d(TAG, "onLoad: admob interstitial");
                        admobInterstitialAd = interstitialAd;
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        super.onAdFailedToLoad(loadAdError);
                        Log.d(TAG, "onAdFailedToLoad: admob interstitial. Loading facebook ad");

                    }
                };
                InterstitialAd.load(activity, activity.getString(R.string.ADMOB_INTERSTITIAL_V2), prepareAdRequest(), interstitialAdLoadCallback);
            }
        }
    }

    public void showInterstitialAdWithoutLoading(Activity activity,Runnable funcAfterAdHidden) {
        if (!preferenceManager.getBoolean(PreferencesManager.Key.IS_APP_ADS_FREE, false)) {
            if (admobInterstitialAd != null) {
                admobInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdDismissedFullScreenContent() {
                        super.onAdDismissedFullScreenContent();
                        Log.d(TAG, "onAdDismissedFullScreenContent: ");
                        admobInterstitialAd = null;
                        MyApplication.mInstance.setInterShowing(false);
//                        resetInterstitialWaitTime();
                        funcAfterAdHidden.run();
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        super.onAdFailedToShowFullScreenContent(adError);
                        Log.d(TAG, "onAdFailedToShowFullScreenContent: ");
                        funcAfterAdHidden.run();
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        super.onAdShowedFullScreenContent();
                        MyApplication.mInstance.setInterShowing(true);
                    }
                });
                admobInterstitialAd.show(activity);
            } else {
                funcAfterAdHidden.run();
            }
        }
    }
    //-------------------------------------------------------------------------------------------------//
    public static boolean isNetWorkAvailable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetWorkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetWorkInfo != null && activeNetWorkInfo.isConnected();
    }

    private AdSize getAdSize(Activity activity) {
        // Step 2 - Determine the screen width (less decorations) to use for the ad width.
        Display display = activity.getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;
        int adWidth = (int) (widthPixels / density);
        // Step 3 - Get adaptive ad size and return for setting on the ad view.
        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(activity, adWidth);
    }

    public void loadNativeAd(Activity activity, FrameLayout frameLayout, NativeAdType nativeAdViewType, String nativeAdId, ShimmerFrameLayout shimmer) {

        if (!preferenceManager.getBoolean(PreferencesManager.Key.IS_APP_ADS_FREE, false)) {
            if (shimmer != null) shimmer.startShimmer();
            AdLoader.Builder builder = new AdLoader.Builder(activity, nativeAdId);
            builder.forNativeAd(nativeAd -> {
                NativeAdView nativeAdView = null;
                if (nativeAdViewType == NativeAdType.REGULAR_TYPE)
                    nativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.admob_native_regular_layout, null);
                if (nativeAdViewType == NativeAdType.MEDIUM_TYPE)
                    nativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.admob_native_medium_layout, null);
                if (nativeAdViewType == NativeAdType.SMALL_TYPE)
                    nativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.admob_native_small_layout, null);
                if (nativeAdViewType == NativeAdType.NOMEDIA_MEDIUM)
                    nativeAdView = (NativeAdView) activity.getLayoutInflater().inflate(R.layout.admob_native_nomedi_medium_layout, null);

                if (nativeAdView != null)
                    populateUnifiedNativeAdView(nativeAd, nativeAdView, nativeAdViewType, activity, nativeAdId);
                if (frameLayout == null) {
                    Log.d(TAG, "FRAME_LAYOUT_NULL: ");
                } else {
                    frameLayout.removeAllViews();
                    frameLayout.addView(nativeAdView);
                    Log.d(TAG, "onNativeAdLoaded: ");
                }

            });

            VideoOptions videoOptions = new VideoOptions.Builder().setStartMuted(true).build();
            NativeAdOptions adOptions = new NativeAdOptions.Builder().setVideoOptions(videoOptions).build();
            builder.withNativeAdOptions(adOptions);
            AdLoader adLoader = builder.withAdListener(new AdListener() {
                @Override
                public void onAdFailedToLoad(LoadAdError loadAdError) {
                    Log.d(TAG, "onAdFailedToLoad: " + loadAdError);
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    if (shimmer != null) {
                        shimmer.stopShimmer();
                        shimmer.hideShimmer();
                    }

                }
            }).build();

            adLoader.loadAd(prepareAdRequest());

        } else {
            frameLayout.setVisibility(View.GONE);
        }
    }

    private void populateUnifiedNativeAdView(NativeAd nativeAd, NativeAdView adView, NativeAdType nativeAdType, Activity activity, String nativeAdId) {
        // Set the media view.
        if (nativeAdType == NativeAdType.REGULAR_TYPE || nativeAdType == NativeAdType.MEDIUM_TYPE)
            adView.setMediaView(adView.findViewById(R.id.ad_media));

        // Set other ad assets.
        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));

        try {
            adView.findViewById(R.id.remove_ad_btn).setOnClickListener(l -> {
                Toast.makeText(activity.getApplicationContext(), activity.getString(R.string.please_wait), Toast.LENGTH_SHORT).show();
                MyApplication.mInstance.billingManagerV5.oneTimePurchase(activity, Constants.Companion.getITEM_SKU_REMOVE_ADS_ONLY());
            });
        } catch (Exception e) {
            Log.d(TAG, "populateUnifiedNativeAdView: " + e.getLocalizedMessage());
        }

        // The headline and mediaContent are guaranteed to be in every UnifiedNativeAd.
        ((TextView) adView.getHeadlineView()).setText(nativeAd.getHeadline());
        if (nativeAdType == NativeAdType.REGULAR_TYPE || nativeAdType == NativeAdType.MEDIUM_TYPE)
            adView.getMediaView().setMediaContent(nativeAd.getMediaContent());

        // These assets aren't guaranteed to be in every UnifiedNativeAd, so it's important to
        // check before trying to display them.
        if (nativeAd.getBody() == null) {
            adView.getBodyView().setVisibility(View.INVISIBLE);
        } else {
            Log.d(TAG, "populateUnifiedNativeAdView: " + nativeAd.getBody());
            adView.getBodyView().setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());

        }

        if (nativeAd.getCallToAction() == null) {
            adView.getCallToActionView().setVisibility(View.INVISIBLE);
        } else {
            adView.getCallToActionView().setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            adView.getIconView().setVisibility(View.GONE);
        } else {
            ((ImageView) adView.getIconView()).setImageDrawable(nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }


        if (nativeAd.getStore() == null) {
            adView.getStoreView().setVisibility(View.INVISIBLE);
        } else {
            adView.getStoreView().setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

      /*  if (nativeAd.getStarRating() == null) {
            adView.getStarRatingView().setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) adView.getStarRatingView())
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }*/

        // This method tells the Google Mobile Ads SDK that you have finished populating your
        // native ad view with this native ad.
        adView.setNativeAd(nativeAd);
        // have a video asset.
        VideoController vc = nativeAd.getMediaContent().getVideoController();

        // Updates the UI to say whether or not this ad has a video asset.
        if (vc.hasVideoContent()) {
            vc.setVideoLifecycleCallbacks(new VideoController.VideoLifecycleCallbacks() {
                @Override
                public void onVideoEnd() {
                    super.onVideoEnd();
                }
            });
        }
    }

    // **********Reward Video *********//

    public void loadRewardVideoAd(Activity activity, IRewardVideo iRewardVideo) {
//        if (!preferenceManager.getBoolean(PreferenceManager.Key.REMOTE_CONFIG_IS_AD_ENABLE, true)) {
//            return;
//        }
        RewardedAd.load(activity, activity.getString(R.string.ADMOB_REWARD_VIDEO), prepareAdRequest(), new RewardedAdLoadCallback() {
            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error.
                Log.d(TAG, loadAdError.toString() + " RewardVideoAd");
                mRewardedAd = null;
                iRewardVideo.onFailedToLoad();
            }

            @Override
            public void onAdLoaded(@NonNull RewardedAd rewardedAd) {
                mRewardedAd = rewardedAd;
                Log.d(TAG, "RewardVideoAd Ad was loaded.");
                iRewardVideo.onRewardVideoLoad();
            }
        });
    }

    public void showRewardVideoAd(Activity activity, IRewardVideo iRewardVideo) {
        mRewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdClicked() {
                // Called when a click is recorded for an ad.
                Log.d(TAG, "Ad was clicked.");
            }

            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.
                Log.d(TAG, " RewardVideoAd Ad dismissed fullscreen content.");
                mRewardedAd = null;
                iRewardVideo.onRewardedSuccess();
            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when ad fails to show.
                Log.e(TAG, " RewardVideoAd Ad failed to show fullscreen content.");
                mRewardedAd = null;
                iRewardVideo.onFailedToShow();

            }

            @Override
            public void onAdImpression() {
                // Called when an impression is recorded for an ad.
                Log.d(TAG, "RewardVideoAd Ad recorded an impression.");
            }

            @Override
            public void onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Log.d(TAG, "RewardVideoAd Ad showed fullscreen content.");
            }
        });

        if (mRewardedAd != null) {
            mRewardedAd.show(activity, new OnUserEarnedRewardListener() {
                @Override
                public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                    // Handle the reward.
                    Log.d(TAG, "The user earned the reward.");
                    int rewardAmount = rewardItem.getAmount();
                    String rewardType = rewardItem.getType();
                }
            });
        } else {
            Log.d(TAG, "The rewarded ad wasn't ready yet.");
        }
    }

    public interface IRewardVideo {
        void onFailedToLoad();

        void onRewardVideoLoad();

        void onFailedToShow();

        void onRewardedSuccess();

    }

    // ********* BANNER ADS ***********//
    public void showBanner(Context context, AdSize size, FrameLayout adFrame, String adId, ShimmerFrameLayout shimmer) {
//        if (!preferenceManager.getBoolean(PreferenceManager.Key.REMOTE_CONFIG_IS_AD_ENABLE, true)) {
//            adFrame.setVisibility(View.GONE);
//            return;
//        }
        if (preferenceManager.getBoolean(PreferencesManager.Key.IS_APP_ADS_FREE, false)) {
            adFrame.setVisibility(View.GONE);
            return;
        }
        Log.d("BANNER_AD", "BANNER_AD_FUN");
        shimmer.startShimmer();

        if (isNetWorkAvailable(context)) {
            AdView adView = new AdView(context);
            adView.setAdSize(size);
            adView.setAdUnitId(adId);
            adFrame.addView(adView);
            adView.loadAd(prepareAdRequest());

            adView.setAdListener(new AdListener() {
                @Override
                public void onAdClicked() {
                    super.onAdClicked();
                }

                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                }

                @Override
                public void onAdImpression() {
                    super.onAdImpression();
                }

                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    shimmer.stopShimmer();
                    shimmer.hideShimmer();
                }

                @Override
                public void onAdOpened() {
                    super.onAdOpened();
                }
            });
        }
    }


    public void showAdaptiveBanner(Activity activity, FrameLayout adViewContainer, ShimmerFrameLayout shimmer) {
        if (MyApplication.mInstance.preferenceManager.getBoolean(PreferencesManager.Key.IS_APP_ADS_FREE, false)) {
            adViewContainer.setVisibility(View.GONE);
            shimmer.setVisibility(View.GONE);
            return;
        }
        if (isNetWorkAvailable(activity.getApplicationContext())) {
            shimmer.startShimmer();
            AdView adView;
            adView = new AdView(activity);
            adView.setAdUnitId(activity.getString(R.string.ADMOB_BANNER_V2));
            adViewContainer.removeAllViews();
            adViewContainer.addView(adView);
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    shimmer.stopShimmer();
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    adViewContainer.setVisibility(View.GONE);
                    shimmer.setVisibility(View.GONE);
                }
            });
            adView.setAdSize(getAdSize(activity));
            Bundle extras = new Bundle();
            extras.putString("collapsible", "bottom");
            AdRequest adRequest = new AdRequest.Builder().addNetworkExtrasBundle(AdMobAdapter.class, extras).build();
            adView.loadAd(adRequest);

        } else {
            adViewContainer.setVisibility(View.GONE);
            shimmer.setVisibility(View.GONE);
        }
    }
//    private void saveAdActivityInPref(PreferenceManager.Key key) {
//        int oldValue = preferenceManager.getInt(key, 0);
//        int newValue = oldValue + 1;
//        preferenceManager.put(key, newValue);
//    }

    /**
     * Rewarded Interstitial Ads
     */
    public void showCollapsableBanner(Activity activity, FrameLayout adViewContainer, ShimmerFrameLayout shimmer) {
        if (MyApplication.mInstance.preferenceManager.getBoolean(PreferencesManager.Key.IS_APP_ADS_FREE, false)) {
            adViewContainer.setVisibility(View.GONE);
            return;
        }
        if (isNetWorkAvailable(activity.getApplicationContext())) {
            shimmer.startShimmer();
            AdView adView;
            adView = new AdView(activity);
            adView.setAdUnitId(activity.getString(R.string.ADMOB_BANNER_COLLAPSIBLE));
            adViewContainer.removeAllViews();
            adViewContainer.addView(adView);
            adView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    shimmer.stopShimmer();
                }

                @Override
                public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                    super.onAdFailedToLoad(loadAdError);
                    adViewContainer.setVisibility(View.GONE);
                }
            });

            adView.setAdSize(getAdSize(activity));
            Bundle extras = new Bundle();
            extras.putString("collapsible", "top");
            AdRequest adRequest = new AdRequest.Builder()
                    .addNetworkExtrasBundle(AdMobAdapter.class, extras)
                    .build();
            adView.loadAd(adRequest);

        }else {
            adViewContainer.setVisibility(View.GONE);
        }
    }

    public interface ISDKinit {
        void onInitialized();
    }
}
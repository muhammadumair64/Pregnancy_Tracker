package com.example.pregnancytrackerignite.di.myApplication

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle

import android.util.Log
import androidx.hilt.work.HiltWorkerFactory
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import androidx.work.Configuration
import com.example.pregnancytrackerignite.R
import com.example.pregnancytrackerignite.data.models.BpModel
import com.example.pregnancytrackerignite.domain.managers.AdsManager
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import com.iobits.rubik_cube_solver.domain.managers.BillingManagerV5
import com.example.pregnancytrackerignite.domain.managers.PreferencesManager
import dagger.hilt.android.HiltAndroidApp
import java.util.Date
import javax.inject.Inject

@HiltAndroidApp
class MyApplication: Application(),Configuration.Provider, Application.ActivityLifecycleCallbacks, LifecycleObserver {

    @Inject
    lateinit var preferenceManager: PreferencesManager
    @Inject
    lateinit var adsManager: AdsManager
    @Inject
    lateinit var billingManagerV5: BillingManagerV5
    @Inject
    lateinit var hiltWorkerFactory: HiltWorkerFactory

    var showIAP = true


    private lateinit var appOpenAdManager: AppOpenAdManager

    private lateinit var currentActivity: Activity

    private val LOG_TAG = "MyApplication"

    var isInterShowing = false
    override fun onCreate() {
        super.onCreate()
        mInstance = this
        mContext = applicationContext
        registerActivityLifecycleCallbacks(this)
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
        appOpenAdManager = AppOpenAdManager()
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder().setWorkerFactory(hiltWorkerFactory).build()

    //open ad
    /** LifecycleObserver method that shows the app open ad when the app moves to foreground. */
    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onMoveToForeground() {
//  Show the ad (if available) when the app moves to foreground.
//  currentActivity?.let { appOpenAdManager.showAdIfAvailable(it) }
//        if (isInterShowing) return
//
//
//        appOpenAdManager.showAdIfAvailable(currentActivity)

//        when (currentActivity) {
//            is AdActivity -> {}
//            else -> {
//                appOpenAdManager.showAdIfAvailable(currentActivity)
//            }
//        }
    }

    /** ActivityLifecycleCallback methods. */
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

    override fun onActivityStarted(activity: Activity) {
        // An ad activity is started when an ad is showing, which could be AdActivity class from Google
        // SDK or another activity class implemented by a third party mediation partner. Updating the
        // currentActivity only when an ad is not showing will ensure it is not an ad activity, but the
        // one that shows the ad.
        if (!appOpenAdManager.isShowingAd) {
            currentActivity = activity
        }
    }

    override fun onActivityResumed(activity: Activity) {}

    override fun onActivityPaused(activity: Activity) {}

    override fun onActivityStopped(activity: Activity) {}

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

    override fun onActivityDestroyed(activity: Activity) {}

    /**
     * Shows an app open ad.
     *
     * @param activity the activity that shows the app open ad
     * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
     */
    fun showAdIfAvailable(activity: Activity, onShowAdCompleteListener: OnShowAdCompleteListener) {
        // We wrap the showAdIfAvailable to enforce that other classes only interact with MyApplication
        // class.
        appOpenAdManager.showAdIfAvailable(activity, onShowAdCompleteListener)
    }

    /**
     * since we are showing open ad in only splash screen
     * and not in onResume
     * so we have to load manually
     * */
    fun loadOpenAdManually(activity: Activity){
        appOpenAdManager.loadAd(activity)
        Log.d(LOG_TAG, "loadOpenAdManually: ")
    }

    /**
     * Interface definition for a callback to be invoked when an app open ad is complete (i.e.
     * dismissed or fails to show).
     */
    interface OnShowAdCompleteListener {
        fun onShowAdComplete()
    }

    /** Inner class that loads and shows app open ads. */
    private inner class AppOpenAdManager {

        private var appOpenAd: AppOpenAd? = null
        private var isLoadingAd = false
        var isShowingAd = false


        /** Keep track of the time an app open ad is loaded to ensure you don't show an expired ad. */
        private var loadTime: Long = 0

        /**
         * Load an ad.
         *
         * @param context the context of the activity that loads the ad
         */
        fun loadAd(context: Context) {
            // if one time ad is shown don't send the next load request
//            if (oneTimeAdShown)
//                return
            if (preferenceManager.getBoolean(PreferencesManager.Key.IS_APP_ADS_FREE, false))
                return

            // Do not load ad if there is an unused ad or one is already loading.
            if (isLoadingAd || isAdAvailable()) {
                return
            }

            isLoadingAd = true
            val request = AdRequest.Builder().build()
            AppOpenAd.load(
                context,
                getString(R.string.ADMOB_OPEN_AD),
                request,
                AppOpenAd.APP_OPEN_AD_ORIENTATION_PORTRAIT,
                object : AppOpenAd.AppOpenAdLoadCallback() {
                    /**
                     * Called when an app open ad has loaded.
                     * @param ad the loaded app open ad.
                     */
                    override fun onAdLoaded(ad: AppOpenAd) {
                        appOpenAd = ad
                        isLoadingAd = false
                        loadTime = Date().time
                        Log.d(LOG_TAG, "onAdLoaded.")
                    }

                    /**
                     * Called when an app open ad has failed to load.
                     *
                     * @param loadAdError the error.
                     */
                    override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                        isLoadingAd = false
                        Log.d(LOG_TAG, "onAdFailedToLoad: " + loadAdError.message)
                    }
                }
            )
        }

        /** Check if ad was loaded more than n hours ago. */
        private fun wasLoadTimeLessThanNHoursAgo(numHours: Long): Boolean {
            val dateDifference: Long = Date().time - loadTime
            val numMilliSecondsPerHour: Long = 3600000
            return dateDifference < numMilliSecondsPerHour * numHours
        }

        /** Check if ad exists and can be shown. */
        private fun isAdAvailable(): Boolean {
            // Ad references in the app open beta will time out after four hours, but this time limit
            // may change in future beta versions. For details, see:
            // https://support.google.com/admob/answer/9341964?hl=en
            return appOpenAd != null && wasLoadTimeLessThanNHoursAgo(4)
        }

        /**
         * Show the ad if one isn't already showing.
         *
         * @param activity the activity that shows the app open ad
         */
        fun showAdIfAvailable(activity: Activity) {
            showAdIfAvailable(
                activity,
                object : OnShowAdCompleteListener {
                    override fun onShowAdComplete() {
                        // Empty because the user will go back to the activity that shows the ad.
                    }
                }
            )
        }
        /**
         * Show the ad if one isn't already showing.
         *
         * @param activity the activity that shows the app open ad
         * @param onShowAdCompleteListener the listener to be notified when an app open ad is complete
         */
        fun showAdIfAvailable(
            activity: Activity,
            onShowAdCompleteListener: OnShowAdCompleteListener
        ) {
            // If the app open ad is already showing, do not show the ad again.
            if (isShowingAd) {
                Log.d(LOG_TAG, "The app open ad is already showing.")
                return
            }

            // If the app open ad is not available yet, invoke the callback then load the ad.
            if (!isAdAvailable()) {
                Log.d(LOG_TAG, "The app open ad is not ready yet.")
                onShowAdCompleteListener.onShowAdComplete()
                loadAd(activity)
                return
            }

            Log.d(LOG_TAG, "Will show ad.")

            appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                /** Called when full screen content is dismissed. */
                override fun onAdDismissedFullScreenContent() {
                    // Set the reference to null so isAdAvailable() returns false.
//                    oneTimeAdShown = true
                    appOpenAd = null
                    isShowingAd = false
                    Log.d(LOG_TAG, "onAdDismissedFullScreenContent.")


                    onShowAdCompleteListener.onShowAdComplete()
                    loadAd(activity)
                }

                /** Called when fullscreen content failed to show. */
                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    appOpenAd = null
                    isShowingAd = false
                    Log.d(LOG_TAG, "onAdFailedToShowFullScreenContent: " + adError.message)

                    onShowAdCompleteListener.onShowAdComplete()
                    loadAd(activity)
                }

                /** Called when fullscreen content is shown. */
                override fun onAdShowedFullScreenContent() {
                    Log.d(LOG_TAG, "onAdShowedFullScreenContent.")
                }
            }
            isShowingAd = true
            appOpenAd!!.show(activity)
        }
    }

    companion object {
        lateinit var mInstance: MyApplication

        private lateinit var mContext: Context

        var tempBpModelToSave = MutableLiveData<BpModel>()
        var isOutForRating = false
    }
}

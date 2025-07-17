package com.example.pregnancytrackerignite.data.utils

object AdsCounter {
    private var showAD = 0
    private var showPro = 4
    var rattingCounter = 1

    fun showAd(): Boolean {
        showAD++
        return showAD % 2 == 0
    }
    fun showPro():Boolean{
        showPro++
        return showPro % 5 == 0
    }

    fun isShowRatting(): Boolean {
        rattingCounter++
        return rattingCounter % 5 == 0
    }
}

package com.cubesolver.Managers

interface Constants {
    companion object {
        const val AVAILABLE_RAM = "availableram"

        //in app item sku
        var ITEM_SKU_REMOVE_ADS_ONLY = "remove_ads" // this will only remove ads .
        var ITEM_SKU_PRO_USER_SUB = "pro_version" // this will only remove ads .

        var ITEM_SKU_GET_PREMIUM =
            "get_premium" //this will remove ads and also enable premium sounds

        var ITEM_SKU_UNLOCK_IP_DETAILS =
            "ip_details" //this will remove ads and also enable premium sounds

        val PLAYERS = "PLAYERS"

        var oneTimeProductPremiumPrice = "2.99$"
        var showRatting = false
    }
}
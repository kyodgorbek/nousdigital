package com.data.helpers

import com.data.BuildConfig

/**
 * This object provides some information about app build and android environment.
 */
class BuildInfoHelper {

    /* Build ====== */

    val isDebug: Boolean
        get() = BuildConfig.DEBUG

    val productName = "iSoftware"


    /* Apis Info ====== */

    val webApiUrl: String
        get() = BuildConfig.WEB_API_URL
}
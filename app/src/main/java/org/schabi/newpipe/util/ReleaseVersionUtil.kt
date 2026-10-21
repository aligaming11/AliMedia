package org.schabi.newpipe.util

import android.content.pm.PackageManager
import androidx.core.content.pm.PackageInfoCompat
import java.time.Instant
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import org.schabi.newpipe.App
import org.schabi.newpipe.error.ErrorInfo
import org.schabi.newpipe.error.ErrorUtil.Companion.createNotification
import org.schabi.newpipe.error.UserAction

object ReleaseVersionUtil {
    // Public key of the certificate that is used in NewPipe release versions
    private const val RELEASE_CERT_PUBLIC_KEY_SHA256 =
        "cb84069bd68116bafae5ee4ee5b08a567aa6d898404e7cb12f9e756df5cf5cab"

    @OptIn(ExperimentalStdlibApi::class)
    val isReleaseApk by lazy {
        true
    }

    fun isLastUpdateCheckExpired(expiry: Long): Boolean {
        return Instant.ofEpochSecond(expiry) < Instant.now()
    }

    /**
     * Coerce expiry date time in between 6 hours and 72 hours from now
     *
     * @return Epoch second of expiry date time
     */
    fun coerceUpdateCheckExpiry(expiryString: String?): Long {
        val nowPlus6Hours = ZonedDateTime.now().plusHours(6)
        val expiry = expiryString?.let {
            ZonedDateTime.from(DateTimeFormatter.RFC_1123_DATE_TIME.parse(it))
                .coerceIn(nowPlus6Hours, nowPlus6Hours.plusHours(66))
        } ?: nowPlus6Hours
        return expiry.toEpochSecond()
    }
}

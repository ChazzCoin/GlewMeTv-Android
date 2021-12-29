package io.aokihome.glewmetv.utils

import java.text.SimpleDateFormat
import java.util.*

/* Date Utils */

//-VERIFIED-\\
fun String.toFullDateString(inPattern: String="yyyy-MM-d", outPattern:String="EEEE, MMMM d, yyyy"): String? {
    val toDate = SimpleDateFormat(inPattern, Locale.US).parse(this) ?: return null
    return SimpleDateFormat(outPattern, Locale.US).format(toDate) ?: return null
}


package pe.edu.upc.rumi.util

import java.text.SimpleDateFormat
import java.util.*

object Functions {
    fun DatetoStringForApi(date: Date): String {
        val format = "yyyy-MM-dd HH:mm:ss"
        val locale = Locale.getDefault()
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(date)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }
}
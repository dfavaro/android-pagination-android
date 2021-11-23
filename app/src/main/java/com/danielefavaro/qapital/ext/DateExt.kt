package com.danielefavaro.qapital.ext

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

// Our plus, so back in time
fun Date.plusTwoWeeks(): Date {
    val c = Calendar.getInstance()
    c.time = this
    c.add(Calendar.WEEK_OF_YEAR, -2)
    return c.time
}

// Our minus, so look at the future
fun Date.minusTwoWeeks(): Date {
    val c = Calendar.getInstance()
    c.time = this
    c.add(Calendar.WEEK_OF_YEAR, 2)
    return c.time
}

fun Date.toApiFormat(): String = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss+00:00", Locale.getDefault()).format(this)
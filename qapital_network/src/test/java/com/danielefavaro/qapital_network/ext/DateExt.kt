package com.danielefavaro.qapital_network.ext

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun String.fromApiFormat(): Date? = SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss+00:00", Locale.getDefault()).parse(this)
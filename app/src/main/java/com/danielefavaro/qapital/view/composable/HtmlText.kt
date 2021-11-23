package com.danielefavaro.qapital.view.composable

import android.util.TypedValue
import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat

@Composable
fun HtmlText(text: String) {
    AndroidView(factory = { context ->
        TextView(context).apply {
            setText(HtmlCompat.fromHtml(text, HtmlCompat.FROM_HTML_MODE_LEGACY))
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
        }
    })
}
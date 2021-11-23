package com.danielefavaro.qapital.view.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.danielefavaro.qapital.R

@Composable
fun Toolbar(title: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround,
    ) {
        Text(
            text = title,
            modifier = Modifier.padding(
                top = dimensionResource(id = R.dimen.toolbar_title_margin_top),
                bottom = dimensionResource(id = R.dimen.toolbar_title_margin_bottom)
            )
        )
    }
}
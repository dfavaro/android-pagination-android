package com.danielefavaro.qapital.view.composable

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.danielefavaro.qapital.R

@Composable
fun HomePage() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Toolbar(title = stringResource(id = R.string.homepage_title))
        ActivityFeedList()
    }
}
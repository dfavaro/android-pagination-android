package com.danielefavaro.qapital.view.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.danielefavaro.qapital.R
import com.danielefavaro.qapital.viewmodel.model.ActivityFeedUI

@Composable
fun ActivityFeed(activityFeedUI: ActivityFeedUI) {
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top,
        ) {
            Image(
                painter = if (activityFeedUI.avatarUrl != null) {
                    rememberImagePainter(
                        data = activityFeedUI.avatarUrl,
                        builder = {
                            transformations(CircleCropTransformation())
                            crossfade(true)
                        }
                    )
                } else {
                    painterResource(R.drawable.ic_launcher_foreground)
                },
                contentDescription = null,
                modifier = Modifier
                    .weight(1.5f)
                    .size(64.dp) // TODO Need to figure out how to fill the size
            )
            Spacer(modifier = Modifier.weight(0.25f))
            Column(
                modifier = Modifier
                    .weight(if (activityFeedUI.amount != null) 6f else 8.25f)
                    .fillMaxHeight(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                HtmlText(text = activityFeedUI.message ?: stringResource(id = R.string.no_message))
                activityFeedUI.date?.let {
                    Text(
                        text = it,
                        fontSize = 12.sp
                    )
                }
            }
            activityFeedUI.amount?.let {
                Spacer(modifier = Modifier.weight(0.25f))
                Text(
                    text = "$$it",
                    fontSize = 14.sp,
                    modifier = Modifier
                        .weight(2f),
                    textAlign = TextAlign.End
                )
            }
        }
    }
    Divider(
        modifier = Modifier.padding(top = dimensionResource(id = R.dimen.activity_feed_divider_margin_top))
    )
}
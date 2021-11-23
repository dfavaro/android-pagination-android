package com.danielefavaro.qapital.view.composable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemsIndexed
import com.danielefavaro.qapital.R
import com.danielefavaro.qapital.viewmodel.ActivityFeedViewModel
import kotlinx.coroutines.reactive.asFlow

@Composable
fun ActivityFeedList(viewModel: ActivityFeedViewModel = viewModel()) {

    val lazyPagingItems = viewModel.feedList.asFlow().collectAsLazyPagingItems()

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.activity_feed_list_vertical_margin)),
        contentPadding = PaddingValues(
            horizontal = dimensionResource(id = R.dimen.activity_feed_list_horizontal_margin),
            vertical = dimensionResource(id = R.dimen.activity_feed_list_vertical_margin)
        ),
    ) {
        itemsIndexed(lazyPagingItems) { _, item ->
            item?.let {
                ActivityFeed(activityFeedUI = it)
            }
        }

        lazyPagingItems.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
                loadState.append is LoadState.Loading -> {
                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        }
    }
}
package com.automotivecodelab.featurerssfeeds.ui.rssfeedsscreen

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.automotivecodelab.coreui.ui.ShowErrorSnackbar
import com.automotivecodelab.coreui.ui.SnackbarWithInsets
import com.automotivecodelab.coreui.ui.injectViewModel
import com.automotivecodelab.featuredetailsbottomsheet.di.DaggerDetailsComponent
import com.automotivecodelab.featuredetailsbottomsheet.di.DetailsComponentDeps
import com.automotivecodelab.featurerssfeeds.R
import com.automotivecodelab.featurerssfeeds.di.DaggerRssFeedsComponent
import com.automotivecodelab.featurerssfeeds.di.RssFeedsDeps
import com.google.accompanist.insets.navigationBarsHeight
import com.google.accompanist.insets.statusBarsHeight
import kotlinx.coroutines.launch

@ExperimentalMaterialApi
@Composable
fun RssFeedsScreen(
    onMenuItemClick: () -> Unit,
    navigateToFeedEntriesScreen: (title: String, threadId: String) -> Unit,
    rssFeedsDeps: RssFeedsDeps,
    detailsComponentDeps: DetailsComponentDeps
) {
    val component = remember {
        DaggerRssFeedsComponent.builder()
            .rssFeedsDeps(rssFeedsDeps)
            .detailsComponent(
                DaggerDetailsComponent.builder()
                    .detailsComponentDeps(detailsComponentDeps)
                    .build()
            )
            .build()
    }

    val viewModel: RssFeedsViewModel = injectViewModel { component.rssFeedsViewModel() }

    val scaffoldState = rememberScaffoldState()

    val scope = rememberCoroutineScope()

    viewModel.error?.ShowErrorSnackbar(scaffoldState)

    if (viewModel.addRssChannelDialogState != AddRssChannelDialogState.HIDDEN) {
        AddRssChannelDialog(
            state = viewModel.addRssChannelDialogState,
            onStateChange = viewModel::onDialogStateChange,
            onConfirm = viewModel::addRssChannel,
            url = viewModel.newRssUrl,
            onUrlChange = viewModel::onRssUrlChange
        )
    }

    Scaffold(
        snackbarHost = {
            SnackbarWithInsets(snackbarHostState = it)
        },
        scaffoldState = scaffoldState,
        topBar = {
            Surface(elevation = AppBarDefaults.TopAppBarElevation) {
                Column {
                    Surface(
                        modifier = Modifier
                            .statusBarsHeight()
                            .fillMaxWidth(),
                        color = Color.Transparent,
                        elevation = 0.dp
                    ) {}
                    TopAppBar(
                        title = { Text(stringResource(id = R.string.threads)) },
                        navigationIcon = {
                            IconButton(onClick = onMenuItemClick) {
                                Icon(
                                    Icons.Filled.Menu,
                                    null,
                                    tint = MaterialTheme.colors.onSurface
                                )
                            }
                        },
                        actions = {
                            IconButton(onClick = {
                                viewModel.onDialogStateChange(AddRssChannelDialogState.SHOWN)
                            }) {
                                Icon(
                                    Icons.Filled.Add,
                                    null,
                                    tint = MaterialTheme.colors.onSurface
                                )
                            }
                        },
                        backgroundColor = MaterialTheme.colors.surface,
                        elevation = 0.dp
                    )
                }
            }
        }
    ) {
        val rssChannels = viewModel.rssChannels.collectAsState()
        Crossfade(targetState = rssChannels.value.isNotEmpty()) {
            if (it) {
                LazyColumn {
                    items(
                        items = rssChannels.value,
                        // for swipe to dismiss works properly:
                        key = { rssChannel -> rssChannel.uuid }
                    ) { rssChannel ->
                        val subscribeMessage = stringResource(
                            id = R.string.subscribed,
                            rssChannel.title
                        )
                        val unsubscribeMessage = stringResource(
                            id = R.string.unsubscribed,
                            rssChannel.title
                        )
                        RssFeedCard(
                            title = rssChannel.title,
                            link = "rutracker.org/forum/viewforum.php?f=" + rssChannel.threadId,
                            onDelete = { viewModel.deleteRssChannel(rssChannel) },
                            isSubscribed = rssChannel.isSubscribed,
                            onToggleSubscription = { isSelected ->
                                if (isSelected) {
                                    viewModel.subscribeToRssChannel(rssChannel)
                                    scope.launch {
                                        scaffoldState.snackbarHostState.currentSnackbarData
                                            ?.dismiss()
                                        scaffoldState.snackbarHostState
                                            .showSnackbar(subscribeMessage)
                                    }
                                } else {
                                    viewModel.unsubscribeFromRssChannel(rssChannel)
                                    scope.launch {
                                        scaffoldState.snackbarHostState.currentSnackbarData
                                            ?.dismiss()
                                        scaffoldState.snackbarHostState
                                            .showSnackbar(unsubscribeMessage)
                                    }
                                }
                            },
                            onClick = {
                                navigateToFeedEntriesScreen(rssChannel.title, rssChannel.threadId)
                            },
                        )
                    }
                    item {
                        Spacer(modifier = Modifier.navigationBarsHeight())
                    }
                }
            }
        }
    }
}
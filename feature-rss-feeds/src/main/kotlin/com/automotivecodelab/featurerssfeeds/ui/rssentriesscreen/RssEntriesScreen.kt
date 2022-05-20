package com.automotivecodelab.featurerssfeeds.ui.rssentriesscreen

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.automotivecodelab.coreui.ui.*
import com.automotivecodelab.featurerssfeeds.di.DaggerRssFeedsComponent
import com.automotivecodelab.featurerssfeeds.di.RssFeedsDeps
import kotlinx.coroutines.CoroutineScope

@ExperimentalMaterialApi
@Composable
fun RssEntriesScreen(
    threadName: String,
    threadId: String,
    torrentId: String?,
    navigateUp: () -> Unit,
    openDetails: (
        torrentId: String,
        category: String,
        author: String,
        title: String,
        url: String,
    ) -> Unit,
    rssFeedsDeps: RssFeedsDeps,
    scaffoldState: ScaffoldState,
    coroutineScope: CoroutineScope,
) {
    val component = remember {
        DaggerRssFeedsComponent.builder()
            .rssFeedsDeps(rssFeedsDeps)
            .build()
    }

    val viewmodel: RssEntriesViewModel = injectViewModel {
        component.rssEntriesViewModelFactory().create(threadId = threadId, torrentId = torrentId)
    }

    viewmodel.error?.ShowErrorSnackbar(
        scaffoldState = scaffoldState,
        coroutineScope = coroutineScope
    )

    viewmodel.closeScreenEvent?.let {
        if (!it.hasBeenHandled) {
            it.getContent()
            navigateUp()
        }
    }

    viewmodel.openDetailsEvent?.let {
        if (!it.hasBeenHandled) {
            val rssChannelEntry = it.getContent()
            openDetails(
                rssChannelEntry.id,
                threadName,
                rssChannelEntry.author,
                rssChannelEntry.title,
                rssChannelEntry.link
            )
        }
    }

    ListWithCollapsingToolbar(
        items = viewmodel.entries,
        itemComposable = {
            TorrentCard(
                title = it.title,
                updated = it.updated,
                author = it.author,
                category = null,
                formattedSize = null,
                seeds = null,
                leeches = null
            ) {
                openDetails(
                    it.id,
                    threadName,
                    it.author,
                    it.title,
                    it.link
                )
            }
            Divider()
        },
        navigationIcon = {
            IconButton(onClick = navigateUp) {
                Icon(
                    Icons.Filled.ArrowBack,
                    null,
                    tint = MaterialTheme.colors.onSurface
                )
            }
        },
        toolbarColor = MaterialTheme.colors.surface,
        toolbarText = threadName,
        isLoading = viewmodel.isLoading
    )
}

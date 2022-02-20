package com.automotivecodelab.featurerssfeeds.domain

import com.automotivecodelab.featurerssfeeds.domain.models.RssChannel
import kotlinx.coroutines.flow.Flow

interface RssChannelRepository {
    fun observeAll(): Flow<List<RssChannel>>
    suspend fun deleteRssChannel(rssChannel: RssChannel)
    suspend fun getRssChannel(threadId: String): Result<RssChannel>
    suspend fun subscribeToRssChannel(rssChannel: RssChannel): Result<Unit>
    suspend fun unsubscribeFromRssChannel(rssChannel: RssChannel): Result<Unit>
}
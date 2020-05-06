package com.data.features.item

import com.core.converter.MoshiConverter
import com.data.BuildConfig
import com.data.features.item.remote.ItemRemoteSource
import com.data.helpers.WebService
import com.domain.features.item.ItemRepository
import com.domain.models.Item
import com.domain.result.http.HttpResult


class ItemRepositoryImpl(
    private val itemRemoteSource: ItemRemoteSource,
    private val webService: WebService,
    private val moshiConverter: MoshiConverter
) : ItemRepository {

    override suspend fun getItems(): HttpResult<Item> =
        try {
            HttpResult.success(itemRemoteSource.getItems())
        } catch (e: Exception) {
            HttpResult.requestError(e)
        }
}
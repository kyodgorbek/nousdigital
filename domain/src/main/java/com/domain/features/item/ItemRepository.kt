package com.domain.features.item

import com.domain.models.Item
import com.domain.result.http.HttpResult


interface ItemRepository {

    suspend fun getItems(): HttpResult<Item>
}
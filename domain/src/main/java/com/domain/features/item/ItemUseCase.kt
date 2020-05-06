package com.domain.features.item

import com.domain.models.Item
import com.domain.result.http.HttpResult

interface ItemUseCase {

    suspend fun getItems(): HttpResult<Item>
}
package com.domain.features.item

import com.domain.models.Item
import com.domain.result.http.HttpResult
import com.domain.result.http.or

class ItemUseCaseImpl(
    private val itemRepository: ItemRepository
) : ItemUseCase {

    override suspend fun getItems(): HttpResult<Item> =
        itemRepository.getItems().safeResponse() or { HttpResult.requestError(it) }
}
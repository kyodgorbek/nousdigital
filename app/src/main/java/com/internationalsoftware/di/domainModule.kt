package com.internationalsoftware.di

import com.domain.features.item.ItemRepository
import com.domain.features.item.ItemUseCase
import com.domain.features.item.ItemUseCaseImpl
import org.koin.dsl.module.applicationContext


val domainModule = applicationContext {

    // ========== ITEM

    factory {
        ItemUseCaseImpl(
            get<ItemRepository>()
        ) as ItemUseCase
    }
}
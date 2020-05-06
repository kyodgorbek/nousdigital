package com.home.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.core.base.BaseViewModel
import com.core.extensions.safeLet
import com.domain.features.item.ItemUseCase
import com.domain.models.Item
import com.domain.result.http.run
import kotlinx.coroutines.launch

class HomeViewModel(
    private val itemUseCase: ItemUseCase
) : BaseViewModel() {

    private val loading: MutableLiveData<Boolean> = MutableLiveData<Boolean>()
    fun loading(): LiveData<Boolean> = loading

    private val items: MutableLiveData<Item> = MutableLiveData<Item>()
    fun items(): LiveData<Item> = items

    private val error: MutableLiveData<Unit> = MutableLiveData<Unit>()
    fun error(): LiveData<Unit> = error


    fun fetchItems() {
        launch {
            loading.value = true
            itemUseCase
                .getItems()
                .run(
                    success = { result ->
                        safeLet(result.data) { list ->
                            if (list.isNotEmpty()) items.value = result else error.value = Unit
                        }
                    },
                    error = {
                        error.value = Unit
                    },
                    finish = {
                        loading.value = false
                    }
                )
        }
    }
}
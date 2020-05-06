package com.domain

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.domain.features.item.ItemRepository
import com.domain.features.item.ItemUseCaseImpl
import com.domain.models.Data
import com.domain.models.Item
import com.domain.result.http.HttpResult
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class ItemUseCaseImplTest {

    @get:Rule
    val instantTask = InstantTaskExecutorRule()

    @MockK
    private lateinit var itemRepository: ItemRepository

    private val itemUseCaseImpl by lazy {
        spyk(ItemUseCaseImpl(itemRepository = itemRepository))
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this, relaxUnitFun = true, relaxed = false)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun `GIVEN a fake information WHEN getting it THEN return success value`() = runBlocking {
        // Given
        val item = Item(
            data = listOf(
                Data(
                    id = 1,
                    title = "Test",
                    description = "Test",
                    image = ""
                )
            )
        )

        // When
        coEvery {
            itemRepository.getItems()
        } returns HttpResult.success(item)

        val result: HttpResult<Item> = itemUseCaseImpl.getItems()

        //Then
        assertEquals(item.data[0].id, result.getResponse().data[0].id)
        assertEquals(item.data[0].title, result.getResponse().data[0].title)
        assertEquals(item.data[0].description, result.getResponse().data[0].description)
        assertEquals(item.data[0].image, result.getResponse().data[0].image)
    }
}
package com.splash

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.domain.features.item.ItemUseCase
import com.domain.models.Data
import com.domain.models.Item
import com.domain.result.http.HttpResult
import com.splash.views.SplashViewModel
import io.mockk.*
import io.mockk.impl.annotations.MockK
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.lang.Exception

/**
 * This class contains some test cases for [SplashViewModel].
 */

@ExperimentalCoroutinesApi
class SplashViewModelTest {

    @get:Rule
    val instantTask = InstantTaskExecutorRule()

    @MockK
    private lateinit var itemUseCase: ItemUseCase

    private val viewModel by lazy {
        spyk(SplashViewModel(itemUseCase = itemUseCase))
    }

    @Before
    fun setUp(){
        MockKAnnotations.init(this, relaxUnitFun = true, relaxed = false)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @Test
    fun `GIVEN a fake information WHEN getting it THEN return success value`() {
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
            itemUseCase.getItems()
        } returns HttpResult.success(item)

        viewModel.fetchItems()

        //Then
        verify(exactly = 1){
            viewModel.fetchItems()
        }

        viewModel.items().observeForever { result ->
            assertEquals(item.data[0].id, result.data[0].id)
            assertEquals(item.data[0].title, result.data[0].title)
            assertEquals(item.data[0].description, result.data[0].description)
            assertEquals(item.data[0].image, result.data[0].image)
        }
    }

    @Test
    fun `GIVEN a fake information WHEN getting it THEN return error`() {
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
            itemUseCase.getItems()
        } returns HttpResult.error(Exception())

        viewModel.fetchItems()

        //Then
        verify(exactly = 1){
            viewModel.fetchItems()
        }

        viewModel.error().observeForever { result ->
            assertEquals(Unit, result)
        }
    }
}
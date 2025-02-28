package com.keepcoding.dragonball

import app.cash.turbine.test
import com.keepcoding.dragonball.domain.Hero
import com.keepcoding.dragonball.heroes.HeroesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before


class HeroesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val viewModel = HeroesViewModel()

    @OptIn(ExperimentalCoroutinesApi::class)
    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }


    val initialHero = Hero(
        id = "1",
        name = "Name",
        photo = "----",
        description = "heroInfo",
        totalHealth = 100,
        currentHealth = 100,
        timesSelected = 0,
    )

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `golpearPersonaje baja la vida del personaje`() {
        val heroExpected = initialHero.copy(
            currentHealth = 90
        )
        viewModel.damageHero(initialHero)
        assertEquals(heroExpected, initialHero)
    }

    @Test
    fun `cuando se selecciona un personaje, el state se actualiza a PersonajeSelecciado con ese personaje`() = runTest {
        viewModel.uiState.test {
            assertEquals(HeroesViewModel.State.Loading, awaitItem())
            viewModel.selectHero(initialHero)
            assertEquals(HeroesViewModel.State.HeroSelected(initialHero), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `cuando se deselecciona un personaje, el state se actualiza a Success`() = runTest {
        viewModel.uiState.test {
            viewModel.userRepository.setToken("eyJraWQiOiJwcml2YXRlIiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJleHBpcmF0aW9uIjo2NDA5MjIxMTIwMCwiZW1haWwiOiJjZHRsQHBydWVibWFpbC5lcyIsImlkZW50aWZ5IjoiRDIwRTAwQTktODY0NC00MUYyLUE0OUYtN0ZDRUY2MTVFMTQ3In0.wMqJfh5qcs5tU6hu2VxT4OV9Svd7BGBA7HsVpKhx5-8")
            assertEquals(HeroesViewModel.State.Loading, awaitItem())
            viewModel.unselectHero()
            assertTrue(awaitItem() is HeroesViewModel.State.Success)
            cancelAndIgnoreRemainingEvents()
        }
    }

}
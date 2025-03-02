package com.keepcoding.dragonball

import android.content.SharedPreferences
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
import org.mockito.Mockito.mock


class HeroesViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val viewModel = HeroesViewModel()
    private val preference: SharedPreferences = mock()

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
    fun `hit heroes actually takes healthPoints`() {
        viewModel.damageHero(initialHero, preference)
        assertEquals(initialHero.currentHealth < 100, true)
    }

    @Test
    fun `heal heroes actually add healthPoints`() {
        //golpeamos al hero, hace entre 10 y 60 de daño
        viewModel.damageHero(initialHero, preference)

        //como las curaciones regeneran 20 ptos de vida, usamos 3 curaciones para asegurarnos que curaría la vida
        //al completo en caso de que se hubieran hecho 60 de daño
        //como solo se puede curar hastaun maximo de 100, si el daño fuera más bajo de 60 no influye
        viewModel.healHero(initialHero, preference)
        viewModel.healHero(initialHero, preference)
        viewModel.healHero(initialHero, preference)

        //Comprobamos que la vida se haya regenerado
        assertEquals(initialHero.currentHealth == 100, true)
    }

    @Test
    fun `cuando se selecciona un personaje, el state se actualiza a PersonajeSelecciado con ese personaje`() = runTest {
        viewModel.uiState.test {
            assertEquals(HeroesViewModel.State.Loading, awaitItem())
            viewModel.selectHero(initialHero, preference)
            assertEquals(HeroesViewModel.State.HeroSelected(initialHero), awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `when hero is selected, timesSelected increments`() {
        viewModel.selectHero(initialHero, preference)
        assertEquals(1, initialHero.timesSelected)
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

    @Test
    fun `when fetching heroes, the state is updated to Success`() = runTest {
        viewModel.uiState.test {
            viewModel.userRepository.setToken("eyJraWQiOiJwcml2YXRlIiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJleHBpcmF0aW9uIjo2NDA5MjIxMTIwMCwiZW1haWwiOiJjZHRsQHBydWVibWFpbC5lcyIsImlkZW50aWZ5IjoiRDIwRTAwQTktODY0NC00MUYyLUE0OUYtN0ZDRUY2MTVFMTQ3In0.wMqJfh5qcs5tU6hu2VxT4OV9Svd7BGBA7HsVpKhx5-8")
            assertEquals(HeroesViewModel.State.Loading, awaitItem())
            viewModel.heroesRepository.fetchHeroes("eyJraWQiOiJwcml2YXRlIiwidHlwIjoiSldUIiwiYWxnIjoiSFMyNTYifQ.eyJleHBpcmF0aW9uIjo2NDA5MjIxMTIwMCwiZW1haWwiOiJjZHRsQHBydWVibWFpbC5lcyIsImlkZW50aWZ5IjoiRDIwRTAwQTktODY0NC00MUYyLUE0OUYtN0ZDRUY2MTVFMTQ3In0.wMqJfh5qcs5tU6hu2VxT4OV9Svd7BGBA7HsVpKhx5-8", preference)
            viewModel.getHeroes(preference)
            assertTrue(awaitItem() is HeroesViewModel.State.Success)
            cancelAndIgnoreRemainingEvents()
        }
    }
}

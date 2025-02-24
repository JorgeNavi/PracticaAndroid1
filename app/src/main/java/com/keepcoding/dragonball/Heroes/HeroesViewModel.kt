package com.keepcoding.dragonball.Heroes

import androidx.lifecycle.ViewModel
import com.keepcoding.dragonball.Domain.Hero
import com.keepcoding.dragonball.Login.LoginViewModel.State
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HeroesViewModel: ViewModel()  {

    sealed class State {
        data object Loading: State()
        data class Success(val heroes: List<Hero>): State()
        data class Error(val message: String, val errorCode: Int): State()

    }

    private val _uiState = MutableStateFlow<State>(State.Loading)
    val uiState: StateFlow<State> = _uiState

    //funcion de descargar personajes desde la API:
    fun getHeroes() {

    }
}
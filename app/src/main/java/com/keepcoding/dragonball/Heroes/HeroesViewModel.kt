package com.keepcoding.dragonball.heroes

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import androidx.lifecycle.viewModelScope
import com.keepcoding.dragonball.domain.Hero
import com.keepcoding.dragonball.repositories.HeroesRepository
import com.keepcoding.dragonball.repositories.UserRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jetbrains.annotations.VisibleForTesting
import kotlin.random.Random

class HeroesViewModel: ViewModel() {


    sealed class State {
        data object Loading: State()
        data class Success(val heroes: List<Hero>): State()
        data class Error(val message: String): State()
        data class HeroSelected(val hero: Hero): State()
    }


    private val _uiState = MutableStateFlow<State>(State.Loading)

    @VisibleForTesting
    val heroesRepository = HeroesRepository()
    @VisibleForTesting
    val userRepository = UserRepository()


    val uiState: StateFlow<State> = _uiState.asStateFlow()

    fun damageHero(hero: Hero, preferences: SharedPreferences) {
        val damage = Random.nextInt(10, 60)
        hero.currentHealth = (hero.currentHealth - damage).coerceAtLeast(0)
        heroesRepository.updateHeroesInPreferences(preferences)
    }

    fun healHero(hero: Hero, preferences: SharedPreferences) {
        val heal = 20
       hero.currentHealth = (hero.currentHealth + 20).coerceAtMost(100)
        heroesRepository.updateHeroesInPreferences(preferences)
    }

    fun selectHero(hero: Hero, preferences: SharedPreferences) {
        hero.timesSelected++
        _uiState.value = State.HeroSelected(hero)
        heroesRepository.updateHeroesInPreferences(preferences)
    }

    fun unselectHero() {
        val result = heroesRepository.fetchHeroes(userRepository.getToken())
        when (result) {
            is HeroesRepository.HeroesResponse.Success -> {
                _uiState.value = State.Success(result.heroes)
            }

            is HeroesRepository.HeroesResponse.Error -> {
                _uiState.value = State.Error(result.message)
            }
        }
    }

    fun getHeroes(sharedPreferences: SharedPreferences) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = State.Loading
            val token = sharedPreferences.getString("token", "") ?: "" //recibimos el token directamente desde sharedPreferences
            val result = heroesRepository.fetchHeroes(token, sharedPreferences)
            when (result) {
                is HeroesRepository.HeroesResponse.Success -> {
                    _uiState.value = State.Success(result.heroes)
                }

                is HeroesRepository.HeroesResponse.Error -> {
                    _uiState.value = State.Error(result.message)
                }
            }
        }
    }
}
package com.keepcoding.dragonball.Login

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.random.Random

class LoginViewModel: ViewModel() {

    private val _uiState = MutableStateFlow<State>(State.Idle) //uistate mutable de tipo State para el viewModel, cuyo valor por defecto es en espera
    val uiState: StateFlow<State> = _uiState //ui state inmutable de tipo State para la UI

    sealed class State { //estados posibles del Login
        data object Idle : State() //en espera
        data object Loading : State()
        data class Success(val token: String) : State()
        data class Error(val message: String, val errorCode: Int) : State()
    }


    fun iniciarLogin(usuario: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = State.Loading //el estado pasa a cargando
            delay(2000L)
            if(Random.nextBoolean()) { //si sale true
                _uiState.value = State.Success("------ token ---------")//success
            } else {
                _uiState.value = State.Error("Error", 401)//si no, error
            }
        }
    }

    //hacemos una función para guardar usuario y contraseña
    fun guardarUsuario(preferences: SharedPreferences?, usuario: String, password: String) { //metemos el preferencies donde queremos guardar la información por parámetro
        viewModelScope.launch(Dispatchers.IO) { //para hacerlo en segundo plano. Launch lanza una corrutina sin bloquear el hilo
            delay(1000L)
            preferences?.edit()?.apply { //se aplica el sharedPreferences
                putString("Usuario", usuario) //putString para guardar el usuario
                putString("Pasword", password) //putString para guardar la contraseña
                apply() //aplicar
            }
        }
    }



}
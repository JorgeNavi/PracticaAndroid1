package com.keepcoding.dragonball.login

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.keepcoding.dragonball.R
import com.keepcoding.dragonball.databinding.ActivityLoginBinding
import com.keepcoding.dragonball.heroes.HeroesActivity
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private val viewModel : LoginViewModel by viewModels()
    private lateinit var binding : ActivityLoginBinding ///by viewBinding(ActivityLoginBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setObservers()

        val imagen = ContextCompat.getDrawable(this, R.mipmap.fondo_login)
        // Es posible acceder uno a uno a las vistas de un layout así:
        // val boton = findViewById<MaterialButton>(R.id.bLogin)
        binding.bLogin.setOnClickListener {
            val rememberMe = binding.cbRememberUser.isChecked
            viewModel.iniciarLogin(
                usuario = binding.etUser.text.toString(),
                password = binding.etPassword.text.toString(),
                rememberMe = rememberMe,
                preferences = getSharedPreferences("LoginPreferences", MODE_PRIVATE)
            )
        }
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when(state){
                    is LoginViewModel.State.Idle -> {}
                    is LoginViewModel.State.Loading -> {
                        binding.pbLoading.visibility = View.VISIBLE
                    }
                    is LoginViewModel.State.Success -> {
                        binding.pbLoading.visibility = View.INVISIBLE
                        startHeroesActivity()
                    }
                    is LoginViewModel.State.Error -> {
                        binding.pbLoading.visibility = View.INVISIBLE
                        Toast.makeText(this@LoginActivity, "Ha ocurrido un error. ${state.message} ${state.errorCode}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }


    private fun startHeroesActivity() {
        HeroesActivity.startHeroesActivity(this)
        finish()
    }

}
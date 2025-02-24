package com.keepcoding.dragonball.Heroes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.keepcoding.dragonball.R
import com.keepcoding.dragonball.databinding.ActivityHeroesBinding
import kotlinx.coroutines.launch


class HeroesActivity: AppCompatActivity() {

    companion object { //funciones estaticas se declaran asi

        private val TAG_TOKEN = "token"

        fun runHeroesActivity(context: Context, token: String) { //el token del login es el que nos va a llevar a la siguiente pantalla
            val intent = Intent(context, HeroesActivity::class.java) //intent lo gestiona android, le estas diciendo que desde context, quieres ir a HeroesActivity
            intent.putExtra(TAG_TOKEN, token) //le pasamos el token
            context.startActivity(intent) //y lo ejecutamos
        }
    }

    private val viewModel : HeroesViewModel by viewModels()
    private lateinit var binding : ActivityHeroesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityHeroesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setObservers()

        val image = ContextCompat.getDrawable(this, R.mipmap.fondo_heroes)
        val token = intent.getStringExtra("token")
        token?.let {
            viewModel.updateToken(token)
        } ?: run {
            Toast.makeText(this, "Missing token", Toast.LENGTH_LONG).show()
            finish()
        }

        viewModel.getHeroes() //pillamos los heroes descargados desde el viewmodel
        binding.rvHeroes.setOnClickListener {
            //viewModel. //Aqui pasariamos a la vista detalle?
        }
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when(state){ //cuando el estado:
                    is HeroesViewModel.State.Loading -> { //si esta cargando
                        binding.pbLoading.visibility = View.VISIBLE //mostramos el loading (el progressbar del xml)
                    }
                    is HeroesViewModel.State.Success -> { //si esta en success:
                        binding.pbLoading.visibility = View.INVISIBLE //escondemos el loading (el progressbar del xml)

                    }
                    is HeroesViewModel.State.Error -> {
                        binding.pbLoading.visibility = View.INVISIBLE
                        Toast.makeText(this@HeroesActivity, "Ha ocurrido un error. ${state.message} ${state.errorCode}", Toast.LENGTH_LONG).show() //mostramos el mensaje y el codigo del error que hemos establecido en el viewmodel
                    }
                }

            }
        }
    }
}

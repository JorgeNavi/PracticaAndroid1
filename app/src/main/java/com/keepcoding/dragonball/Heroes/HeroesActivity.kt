package com.keepcoding.dragonball.Heroes

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

    private val viewModel : HeroesViewModel by viewModels()
    private lateinit var binding : ActivityHeroesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityHeroesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setObservers()
        viewModel.getHeroes() //pillamos los heroes descargados desde el viewmodel
        val imagen = ContextCompat.getDrawable(this, R.mipmap.fondo_heroes)
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

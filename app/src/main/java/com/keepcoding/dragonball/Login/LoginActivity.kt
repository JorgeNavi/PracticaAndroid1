package com.keepcoding.dragonball.Login

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
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private val viewModel : LoginViewModel by viewModels() //hemos creado la clase loginViewModel y la instanciamos aqui
    //Aqui ponemos el binding cuyo tipo equivale al activity_login.xml. Es como conectar este fichero con el xml correspondiente
    private lateinit var binding : ActivityLoginBinding ///by viewBinding(ActivityLoginBinding::inflate) //para acceder a los elementos de la vista

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) //es lo primero que se ejecuta en el ciclo de vida de la activity (la pantalla)
        enableEdgeToEdge()//se carga la barra de arriba de reloj, bateria, etc

        binding = ActivityLoginBinding.inflate(layoutInflater) //el layoutInflater es una propiedad de la activity. coge el xml y genera la pantalla como tal
        setContentView(binding.root) //Aqui se dice que se establece que el contenido de la vista es "la root del binding", es decir, el xml
        setObservers() //vamos a establecer el metodo de suscribirse al publicador
        viewModel.guardarUsuario(
            preferences = getSharedPreferences("LoginPreferences", MODE_PRIVATE), //instanciamos el sharedpreferencies como contexto donde se van a guardar
            usuario = "pepe", //usuario a guardar
            password = "1234" //password a guardar
        )
        //los Toast son mensajes que se mandan al usuario de forma básica
        Toast.makeText(this, "App abierta correctamente", Toast.LENGTH_LONG).show()//el contexto es esta activity, el texto que muestra: App abierta correctamente y el tiempo que dura.
        val imagen = ContextCompat.getDrawable(this, R.mipmap.fondo_login) //esta es la forma de acceder a los recursos con el contexto de esta actividad y el recurso en si
        // Es posible acceder uno a uno a las vistas de un layout así:
        // val boton = findViewById<MaterialButton>(R.id.bLogin)
        binding.bLogin.setOnClickListener { //para acceder a los elementos de la vista, en este caso el boton del login. setOnClickListener es un listener de que ocurre cuando se clicka el login
            viewModel.iniciarLogin(
                usuario = binding.etUser.text.toString(), //etUser es como hemos llamado al edithText en el xml
                password = binding.etPassword.text.toString() //se le pasa el valor de text a String por si cambia el tipo
            )
        }
    }

    private fun setObservers() {
        lifecycleScope.launch {
            viewModel.uiState.collect { state -> //nos quedamos esuchando los estados
                when(state){ //cuando el estado:
                    is LoginViewModel.State.Idle -> {} //em espera: no hacemos nada
                    is LoginViewModel.State.Loading -> { //si esta cargando
                        binding.pbLoading.visibility = View.VISIBLE //mostramos el loading (el progressbar del xml)
                    }
                    is LoginViewModel.State.Success -> { //si esta en success:
                        // TODO ir a la siguiente pantalla
                        binding.pbLoading.visibility = View.INVISIBLE //escondemos el loading (el progressbar del xml)
                        Toast.makeText(this@LoginActivity, "El token es. ${state.token}", Toast.LENGTH_LONG).show() //mostramos el token

                    }
                    is LoginViewModel.State.Error -> {
                        binding.pbLoading.visibility = View.INVISIBLE
                        Toast.makeText(this@LoginActivity, "Ha ocurrido un error. ${state.message} ${state.errorCode}", Toast.LENGTH_LONG).show() //mostramos el mensaje y el codigo del error que hemos establecido en el viewmodel
                    }
                }

            }
        }
    }
}
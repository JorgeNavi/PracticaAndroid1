package com.keepcoding.dragonball.repositories

import android.content.SharedPreferences
import com.google.gson.Gson
import com.keepcoding.dragonball.domain.Hero
import com.keepcoding.dragonball.domain.HeroDto
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

class HeroesRepository {

    private val BASE_URL = "https://dragonball.keepcoding.education/api/"
    private var heroesList = listOf<Hero>()

    sealed class HeroesResponse {
        data class Success(val heroes: List<Hero>) : HeroesResponse()
        data class Error(val message: String) : HeroesResponse()
    }

    fun fetchHeroes(token: String, sharedPreferences: SharedPreferences? = null): HeroesResponse {
        if (heroesList.isNotEmpty())
            return HeroesResponse.Success(heroesList)

        // Este es un ejemplo de como guardar en las shared preferences toda la lista de personajes. Falta que se actualice cuando reciban golpes, se puede implentar esto cuando reciban daño o se curen
        sharedPreferences?.let {
            val heroesListJson = it.getString("heroesList", "")
            if (!heroesListJson.isNullOrEmpty()) {
                val storedHeroes: Array<Hero>? = Gson().fromJson(heroesListJson, Array<Hero>::class.java)
                if (!storedHeroes.isNullOrEmpty()) {
                    heroesList = storedHeroes.toList()
                    return HeroesResponse.Success(heroesList)
                }
            }
        }

        val client = OkHttpClient()
        val url = "${BASE_URL}heros/all"

        val formBody = FormBody.Builder()
            .add("name", "")
            .build()

        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .addHeader("Authorization", "Bearer $token")
            .build()

        val call = client.newCall(request)
        val response = call.execute()

        return if (response.isSuccessful) {
            val heroesDto: Array<HeroDto> =
                Gson().fromJson(response.body?.string(), Array<HeroDto>::class.java)
            // Aqui hemos descargado la lista

            heroesList = heroesDto.map {
                Hero(
                    id = it.id,
                    name = it.name,
                    photo = it.photo,
                    currentHealth = 100,
                    totalHealth = 100,
                    description = it.description,
                    timesSelected = 0
                )
            }
            sharedPreferences?.edit()?.apply {
                putString("heroesList", Gson().toJson(heroesList))
                apply()
            }
            HeroesResponse.Success(heroesList)
        } else {
            HeroesResponse.Error("Error al descargar los personajes. ${response.message}")
        }

    }

    //metodo para actualizar la lista de heroes en el preferences
    fun updateHeroesInPreferences(preferences: SharedPreferences?) {
        preferences?.edit()?.apply {
            putString("heroesList", Gson().toJson(heroesList))
            apply()
        }
    }

}
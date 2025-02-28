package com.keepcoding.dragonball.repositories

import android.util.Log
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.jetbrains.annotations.VisibleForTesting

class UserRepository {

    private val BASE_URL = "https://dragonball.keepcoding.education/api/auth/login"
    private var token = ""

    sealed class LoginResponse {
        data class Success(val token: String) : LoginResponse()
        data class Error(val message: String) : LoginResponse()
    }

    fun login(user: String, password: String): LoginResponse {
        val client = OkHttpClient()
        val url = BASE_URL

        val formBody = FormBody.Builder()
            .add("username", user)
            .add("password", password)
            .build()

        val request = Request.Builder()
            .url(url)
            .post(formBody)
            .build()

        return try {
            val response = client.newCall(request).execute()
            Log.d("LOGIN", "Código de respuesta: ${response.code} - ${response.message}")
            if (response.isSuccessful) {
                val responseBody = response.body?.string()
                Log.d("LOGIN", "Respuesta: $responseBody")
                if (!responseBody.isNullOrEmpty()) {
                    token = responseBody
                    return LoginResponse.Success(token)
                } else {
                    LoginResponse.Error("Error token is missing")
                }
            } else {
                LoginResponse.Error("Login Error: ${response.code} ${response.message}")
            }
        } catch (e: Exception) {
            LoginResponse.Error("Exception: ${e.localizedMessage}")
        }
    }
    fun getToken(): String = token

    @VisibleForTesting
    fun setToken(token: String) {
        this.token = token
    }
}



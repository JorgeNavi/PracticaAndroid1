package com.keepcoding.dragonball.heroes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.keepcoding.dragonball.databinding.ActivityHeroesBinding
import com.keepcoding.dragonball.heroDetail.DetailFragment
import kotlin.random.Random

interface HeroesOptions{
    fun goToHeroes()
    fun goToDetail()
}

class HeroesActivity: AppCompatActivity(), HeroesOptions {

    companion object{
        fun startHeroesActivity(context: Context) {
            val intent = Intent(context, HeroesActivity::class.java)
            context.startActivity(intent)
        }
    }

    private val viewModel: HeroesViewModel by viewModels()
    private lateinit var binding: ActivityHeroesBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHeroesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getHeroes(getSharedPreferences("my_preferences", Context.MODE_PRIVATE))
        initFragments()
    }

    private fun initFragments() {
        goToHeroes()
    }

    override fun goToHeroes() {
        supportFragmentManager.beginTransaction().apply {
            replace(binding.flHome.id, HeroesFragment())
            commit()
        }
    }
    override fun goToDetail() {
        supportFragmentManager.beginTransaction().apply {
            replace(binding.flHome.id, DetailFragment())
            addToBackStack(Random.nextInt().toString())
            commit()
        }
    }

}
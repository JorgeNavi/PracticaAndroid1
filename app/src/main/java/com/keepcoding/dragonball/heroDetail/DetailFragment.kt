package com.keepcoding.dragonball.heroDetail

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.keepcoding.dragonball.R
import com.keepcoding.dragonball.databinding.FragmentDetailBinding
import com.keepcoding.dragonball.domain.Hero
import com.keepcoding.dragonball.heroes.HeroesOptions
import com.keepcoding.dragonball.heroes.HeroesViewModel

import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class DetailFragment: Fragment() {

    private lateinit var binding: FragmentDetailBinding
    private val viewModel: HeroesViewModel by activityViewModels()
    private var job: Job? = null
    private lateinit var preferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        preferences =
            requireContext().getSharedPreferences("LoginPreferences", Context.MODE_PRIVATE)
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        initObservers()
        return binding.root
    }

    private fun initViews(hero: Hero) {
        with(binding) {
            tvName.text = hero.name
            tvCounter.text = hero.timesSelected.toString()
            Glide
                .with(binding.root)
                .load(hero.photo)
                .centerInside()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(binding.ivHeroPhoto)
            pbHealth.progress = hero.currentHealth
            bHit.setOnClickListener {
                viewModel.damageHero(hero, preferences)
                pbHealth.progress = hero.currentHealth
                if (hero.currentHealth == 0) {
                    (activity as? HeroesOptions)?.goToHeroes()
                }
            }
            bHeal.setOnClickListener {
                viewModel.healHero(hero, preferences)
                pbHealth.progress = hero.currentHealth
            }
        }
    }

    private fun initObservers() {
        job = lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                when (state) {
                    is HeroesViewModel.State.HeroSelected -> {
                        initViews(state.hero)
                        if (state.hero.currentHealth == 0) {
                            (activity as? HeroesOptions)?.goToHeroes()
                        }
                    }

                    else -> Unit
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        job?.cancel()
        if (isRemoving || requireActivity().isFinishing) {
            viewModel.unselectHero()
        }
    }
}
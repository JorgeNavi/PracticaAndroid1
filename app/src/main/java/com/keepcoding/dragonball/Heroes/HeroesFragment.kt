package com.keepcoding.dragonball.heroes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch
import androidx.fragment.app.activityViewModels
import com.keepcoding.dragonball.databinding.FragmentHeroesBinding
import kotlinx.coroutines.Job


class HeroesFragment: Fragment() {

    private val heroAdapter = HeroAdapter(
        onHeroClicked = { hero ->
            viewModel.selectHero(hero)
        }
    )
    private val viewModel: HeroesViewModel by activityViewModels()

    private lateinit var binding: FragmentHeroesBinding
    private var job: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHeroesBinding.inflate(inflater, container, false)
        initViews()
        initObservers()
        return binding.root
    }

    private fun initViews() {
        binding.rvHeroes.layoutManager = LinearLayoutManager(this.context)
        binding.rvHeroes.adapter = heroAdapter
    }

    private fun initObservers() {
        job = lifecycleScope.launch {
            viewModel.uiState.collect{ state ->
                when(state){
                    is HeroesViewModel.State.Loading -> {
                        binding.pbLoading.visibility = View.VISIBLE
                    }
                    is HeroesViewModel.State.Success -> {
                        binding.pbLoading.visibility = View.GONE
                        heroAdapter.updateHeroes(state.heroes)
                    }
                    is HeroesViewModel.State.Error -> {
                        binding.pbLoading.visibility = View.GONE
                    }
                    is HeroesViewModel.State.HeroSelected -> {
                        (activity as? HeroesOptions)?.goToDetail()
                    }
                }
            }
        }
    }

    override fun onStop() {
        super.onStop()
        job?.cancel()
    }

}
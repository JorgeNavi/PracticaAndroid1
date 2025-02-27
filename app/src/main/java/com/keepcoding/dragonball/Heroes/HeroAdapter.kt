package com.keepcoding.dragonball.heroes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.keepcoding.dragonball.R
import com.keepcoding.dragonball.databinding.ItemHeroBinding
import com.keepcoding.dragonball.domain.Hero


class HeroAdapter(
    private var onHeroClicked: (Hero) -> Unit,
): RecyclerView.Adapter<HeroAdapter.HeroViewHolder>() {

    private var heroesList = listOf<Hero>()

    fun updateHeroes(heroes: List<Hero>) {
        this.heroesList = heroes
        notifyDataSetChanged()
    }

    class HeroViewHolder(
        private val binding: ItemHeroBinding,
        private var onHeroClicked: (Hero) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(hero: Hero) {
            binding.tvName.text = hero.name
            Glide
                .with(binding.root)
                .load(hero.photo)
                .centerInside()
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(binding.ivPhoto)
            binding.tvHealth.text = "${hero.currentHealth} / ${hero.totalHealth}"
            binding.root.setOnClickListener {
                onHeroClicked(hero)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
        return HeroViewHolder(
            binding = ItemHeroBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onHeroClicked = onHeroClicked,
        )
    }

    override fun getItemCount(): Int {
        return heroesList.size
    }

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        holder.bind(heroesList[position])
    }


}
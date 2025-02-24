package com.keepcoding.dragonball

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.keepcoding.dragonball.Domain.Hero
import com.keepcoding.dragonball.databinding.ActivitySinglecharacterBinding


class HeroAdapter(
    private var onHeroClicked: (Hero) -> Unit,
): RecyclerView.Adapter<HeroAdapter.HeroViewHolder>() {

    private var heroes = listOf<Hero>()

    fun updateHeroes(heroes: List<Hero>) {
        this.heroes = heroes
        notifyDataSetChanged() //enviamos que se ha actualizado la lista
    }

    class HeroViewHolder(
        private val binding: ActivitySinglecharacterBinding,
        private var onHeroClicked: (Hero) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(hero: Hero) {
            binding.tvName.text = hero.name
            /*
            Glide
                .with(binding.root) //en que activity se va a mostrar
                .load(hero.photo)
                .centerInside() //donde se va a colocar
                .placeholder(R.drawable.ic_launcher_foreground) //que foto quieres poner si no se descarga correctamente
                .into(binding.ivImage) //en que vista se va a mostrar
            */
            binding.root.setOnClickListener {
                onHeroClicked(hero)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HeroViewHolder {
        return HeroViewHolder(
            binding = ActivitySinglecharacterBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            onHeroClicked = onHeroClicked,
        )
    }

    override fun getItemCount(): Int {
        return heroes.size //el tamaño de la lista es el tamaño de los personajes
    }

    override fun onBindViewHolder(holder: HeroViewHolder, position: Int) {
        holder.bind(heroes[position])
    }


}
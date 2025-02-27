package com.keepcoding.dragonball.domain

class HeroDto(
    val id: String,
    val photo: String,
    var favorite: Boolean,
    val name: String,
    val description: String,
)
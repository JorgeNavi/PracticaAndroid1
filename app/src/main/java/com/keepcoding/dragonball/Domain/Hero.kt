package com.keepcoding.dragonball.domain

data class Hero(val id: String, val name: String, val description: String, val photo: String, var currentHealth: Int, val totalHealth: Int = 100, var timesSelected: Int = 0) {
}

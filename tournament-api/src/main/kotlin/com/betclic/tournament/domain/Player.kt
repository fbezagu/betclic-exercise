package com.betclic.tournament.domain

data class Player(
    val nickname: String,
    val id: String = "",
    val score: Score = Score(0)
)


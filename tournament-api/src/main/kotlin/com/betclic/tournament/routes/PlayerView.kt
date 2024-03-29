package com.betclic.tournament.routes

import kotlinx.serialization.Serializable

@Serializable
data class PlayerView(
    val nickname: String,
    val id: String = "",
    val score: Int = 0
) {
}
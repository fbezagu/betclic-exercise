package com.betclic.tournament.routes

import com.betclic.tournament.domain.Player
import kotlinx.serialization.Serializable

@Serializable
data class PlayerView(
    val nickname: String,
    val id: String = "",
    val score: Int = 0,
    val rank: Int = 0
) {
    companion object {
        fun fromPlayer(player: Player): PlayerView {
            return PlayerView(player.nickname, player.id, player.score)
        }
    }
}
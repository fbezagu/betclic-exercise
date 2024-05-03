package com.betclic.tournament.domain.updatePlayerScore

import com.betclic.tournament.domain.model.Player
import com.betclic.tournament.domain.model.Score

interface UpdatePlayerScore {
    fun updatePlayerScore(player: Player, newScore: Score): Player
}
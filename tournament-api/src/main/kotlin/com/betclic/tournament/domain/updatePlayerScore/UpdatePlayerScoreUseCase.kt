package com.betclic.tournament.domain.updatePlayerScore

import com.betclic.tournament.domain.model.Player
import com.betclic.tournament.domain.model.Repositories
import com.betclic.tournament.domain.model.Score

class UpdatePlayerScoreUseCase(val repositories: Repositories) : UpdatePlayerScore {
    override fun updatePlayerScore(player: Player, newScore: Score): Player {
        val playerWithNewScore = player.copy(score = newScore)
        repositories.players().update(playerWithNewScore)
        return playerWithNewScore
    }
}
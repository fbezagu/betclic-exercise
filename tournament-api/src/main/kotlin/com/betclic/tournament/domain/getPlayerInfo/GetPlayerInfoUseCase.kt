package com.betclic.tournament.domain.getPlayerInfo

import com.betclic.tournament.domain.model.Player
import com.betclic.tournament.domain.model.Repositories

class GetPlayerInfoUseCase(val repositories: Repositories) : GetPlayerInfo {
    override fun getPlayerInfo(id: String): Player? {
        return repositories.players().getById(id)
    }

    override fun playerRank(player: Player): Int {
        return repositories.players().countWithScoreHigherThan(player.score) + 1
    }
}
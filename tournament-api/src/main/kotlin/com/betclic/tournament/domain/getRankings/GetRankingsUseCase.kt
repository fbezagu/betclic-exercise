package com.betclic.tournament.domain.getRankings

import com.betclic.tournament.domain.model.Player
import com.betclic.tournament.domain.model.Repositories

class GetRankingsUseCase(val repositories: Repositories) : GetRankings {
    override fun getRankings(): List<Player> {
        return repositories.players().allSortedByScore()
    }
}
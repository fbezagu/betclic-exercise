package com.betclic.tournament.domain.getAllPlayers

import com.betclic.tournament.domain.model.Player
import com.betclic.tournament.domain.model.Repositories

class GetAllPlayersUseCase(val repositories: Repositories) : GetAllPlayers {
    override fun getPlayers(): List<Player> = repositories.players().all()
}
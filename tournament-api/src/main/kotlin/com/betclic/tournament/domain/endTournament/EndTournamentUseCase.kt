package com.betclic.tournament.domain.endTournament

import com.betclic.tournament.domain.model.Repositories

class EndTournamentUseCase(val repositories: Repositories) : EndTournament {
    override fun end() = repositories.players().clear()

}
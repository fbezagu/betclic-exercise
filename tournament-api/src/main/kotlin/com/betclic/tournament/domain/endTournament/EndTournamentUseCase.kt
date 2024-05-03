package com.betclic.tournament.domain.endTournament

import com.betclic.tournament.domain.model.Repositories

class EndTournamentUseCase(val repositories: Repositories) {
    fun end() = repositories.players().clear()

}
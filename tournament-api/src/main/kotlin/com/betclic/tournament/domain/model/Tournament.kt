package com.betclic.tournament.domain.model


class Tournament(val repositories: Repositories) {
    fun getPlayers(): List<Player> = repositories.players().all()

}

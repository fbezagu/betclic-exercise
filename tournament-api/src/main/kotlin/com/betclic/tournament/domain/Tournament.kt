package com.betclic.tournament.domain

class Tournament {
    fun getPlayers(): List<Player> = repositories.players().all()
    fun end() = repositories.players().clear()
}

var currentTournament = Tournament()

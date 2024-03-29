package com.betclic.tournament.domain

class Tournament {
    fun getPlayers(): List<Player> = repositories.players().all()

    fun end() = repositories.players().clear()

    fun addPlayer(nickname: String): Player {
        if (repositories.players().getByNickname(nickname) != null) {
            throw PlayerAlreadyExistsException()
        }
        val player = Player(nickname)
        repositories.players().add(player)
        return player
    }

    fun updatePlayerScore(player: Player, newScore: Int) {
        player.score = newScore
        repositories.players().update(player)
    }
}

var currentTournament = Tournament()

package com.betclic.tournament.domain.model


class Tournament(val repositories: Repositories) {
    fun getPlayers(): List<Player> = repositories.players().all()

    fun end() = repositories.players().clear()

    fun playerRank(player: Player): Int {
        return repositories.players().countWithScoreHigherThan(player.score) + 1
    }
}

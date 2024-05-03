package com.betclic.tournament.domain


class Tournament(val repositories: Repositories) {
    fun getPlayers(): List<Player> = repositories.players().all()

    fun end() = repositories.players().clear()

    fun addPlayer(nickname: String): Player {
        if (repositories.players().getByNickname(nickname) != null) {
            throw PlayerAlreadyExistsException()
        }
        return repositories.players().add(Player(nickname))
    }

    fun updatePlayerScore(player: Player, newScore: Int): Player {
        val playerWithNewScore = player.copy(score = newScore)
        repositories.players().update(playerWithNewScore)
        return playerWithNewScore
    }

    fun playerRank(player: Player): Int {
        return repositories.players().countWithScoreHigherThan(player.score) + 1
    }

    fun getRankings(): List<Player> {
        return repositories.players().allSortedByScore()
    }
}

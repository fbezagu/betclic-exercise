package com.betclic.tournament.db

import com.betclic.tournament.domain.model.PlayerRepository
import com.betclic.tournament.domain.model.Player
import com.betclic.tournament.domain.model.Repositories
import com.betclic.tournament.domain.model.Score
import java.util.*

class MemoryRepositories : Repositories() {
    private val playerRepository = MemoryPlayerRepository()

    override fun init() {
        println("Init memory repositories: nothing to do")
    }

    override fun players(): PlayerRepository {
        return playerRepository
    }
}

class MemoryPlayerRepository : PlayerRepository {
    private val items = mutableListOf<Player>()

    override fun add(player: Player): Player {
        val playerWithId = player.copy(id = UUID.randomUUID().toString())
        items.add(playerWithId)
        return playerWithId
    }

    override fun all(): List<Player> = items
    override fun clear() {
        items.clear()
    }

    override fun getByNickname(nickname: String): Player? = items.find { it.nickname == nickname }
    override fun update(player: Player) {
        val index = items.indexOfFirst { it.id == player.id }
        items[index] = player
    }

    override fun getById(id: String): Player? = items.find { it.id == id }

    override fun countWithScoreHigherThan(score: Score): Int {
        return items.filter { it.score.i > score.i }.size
    }

    override fun allSortedByScore(): List<Player> {
        return items.sortedByDescending { it.score.i }
    }

    override val count: Int
        get() = items.size

}

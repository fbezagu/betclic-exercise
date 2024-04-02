package com.betclic.tournament.db

import com.betclic.tournament.domain.Player
import com.betclic.tournament.domain.PlayerRepository
import com.betclic.tournament.domain.Repositories
import java.util.*

class MemoryRepositories : Repositories() {
    private val playerRepository = MemoryPlayerRepository()

    override fun players(): PlayerRepository {
        return playerRepository
    }
}

class MemoryPlayerRepository : PlayerRepository() {
    private val items = mutableListOf<Player>()
    val updatedItems: MutableList<Player> = mutableListOf()

    override fun add(player: Player) {
        player.id = UUID.randomUUID().toString()
        items.add(player)
    }

    override fun all(): List<Player> = items
    override fun clear() {
        items.clear()
    }

    override fun getByNickname(nickname: String): Player? = items.find { it.nickname == nickname }
    override fun update(player: Player) {
        updatedItems.add(player)
    }

    override fun getById(id: String): Player? = items.find { it.id == id }

    override fun countWithScoreHigherThan(score: Int): Int {
        return items.filter { it.score > score }.size
    }

    override fun allSortedByScore(): List<Player> {
        return items.sortedByDescending { it.score }
    }

    override val count: Int
        get() = items.size

}

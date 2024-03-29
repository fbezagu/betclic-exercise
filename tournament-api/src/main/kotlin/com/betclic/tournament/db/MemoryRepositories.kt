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

    override fun add(player: Player) {
        player.id = UUID.randomUUID().toString()
        items.add(player)
    }

    override fun all(): List<Player> = items
    override fun clear() {
        items.clear()
    }

    override fun getByNickname(nickname: String): Player? = items.find { it.nickname == nickname }

    override val count: Int
        get() = items.size

}

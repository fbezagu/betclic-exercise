package com.betclic.tournament.domain

abstract class Repositories {
    abstract fun players(): PlayerRepository
    open fun init() {}
}

interface PlayerRepository {
    val count: Int
    fun add(player: Player)
    fun all(): List<Player>
    fun clear()
    fun getByNickname(nickname: String): Player?
    fun update(player: Player)
    fun getById(id: String): Player?
    fun countWithScoreHigherThan(score: Int): Int
    fun allSortedByScore(): List<Player>
}

class NoRepositories : Repositories() {
    override fun players(): PlayerRepository {
        throw Exception("Repositories aren’t initialized")
    }
}

var repositories: Repositories = NoRepositories()
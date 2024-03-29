package com.betclic.tournament.domain

abstract class Repositories {
    abstract fun players(): PlayerRepository
}

abstract class PlayerRepository {
    abstract fun add(player: Player)
    abstract fun all(): List<Player>
    abstract fun clear()
    abstract val count: Int
}

class NoRepositories : Repositories() {
    override fun players(): PlayerRepository {
        throw Exception("Repositories aren’t initialized")
    }
}

var repositories: Repositories = NoRepositories()
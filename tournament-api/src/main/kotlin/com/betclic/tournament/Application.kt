package com.betclic.tournament

import com.betclic.tournament.db.MemoryRepositories
import com.betclic.tournament.domain.Player
import com.betclic.tournament.domain.repositories
import com.betclic.tournament.plugins.configureRouting
import com.betclic.tournament.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {
    repositories = MemoryRepositories()
    val pierre = Player("Pierre")
    pierre.score = 5
    val paul = Player("Paul")
    paul.score = 8
    val jack = Player("Jack")
    jack.score = 2
    repositories.players().add(pierre)
    repositories.players().add(paul)
    repositories.players().add(jack)

    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
}

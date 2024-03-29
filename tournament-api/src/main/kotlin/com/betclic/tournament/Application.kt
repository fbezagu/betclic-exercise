package com.betclic.tournament

import com.betclic.tournament.db.MemoryRepositories
import com.betclic.tournament.domain.Player
import com.betclic.tournament.domain.repositories
import com.betclic.tournament.plugins.configureRouting
import com.betclic.tournament.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {
    repositories = MemoryRepositories()
    repositories.players().add(Player("Pierre"))
    repositories.players().add(Player("Paul"))
    repositories.players().add(Player("Jack"))

    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
}

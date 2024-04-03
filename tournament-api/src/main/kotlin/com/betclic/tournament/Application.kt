package com.betclic.tournament

import com.betclic.tournament.db.DynamoRepositories
import com.betclic.tournament.domain.repositories
import com.betclic.tournament.plugins.configureRouting
import com.betclic.tournament.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
    repositories = DynamoRepositories()
    repositories.init()

    EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
}

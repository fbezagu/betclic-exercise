package com.betclic.tournament

import com.betclic.tournament.plugins.configureRouting
import com.betclic.tournament.plugins.configureSerialization
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cors.routing.*

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    configureSerialization()
    configureRouting()
}

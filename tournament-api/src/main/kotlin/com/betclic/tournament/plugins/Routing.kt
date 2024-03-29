package com.betclic.tournament.plugins

import com.betclic.tournament.routes.playersRouting
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        playersRouting()
        get("/") {
            call.respondText("Bienvenue dans le tournoi !")
        }
    }
}

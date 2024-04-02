package com.betclic.tournament.plugins

import com.betclic.tournament.routes.playerRouting
import com.betclic.tournament.routes.playersRouting
import com.betclic.tournament.routes.rankingsRouting
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
    routing {
        playersRouting()
        playerRouting()
        rankingsRouting()
        get("/") {
            call.respondText("Bienvenue dans le tournoi !")
        }
    }
}

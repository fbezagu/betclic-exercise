package com.betclic.tournament.routes

import com.betclic.tournament.domain.currentTournament
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.rankingsRouting() {
    route("/rankings") {
        get {
            val players = currentTournament.getRankings()

            call.respond(players.map { PlayerView.fromPlayer(it) })
        }
    }
}
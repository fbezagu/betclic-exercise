package com.betclic.tournament.routes

import com.betclic.tournament.domain.PlayerAlreadyExistsException
import com.betclic.tournament.domain.currentTournament
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.playersRouting() {
    route("/players") {
        get {
            val players = currentTournament.getPlayers()

            call.respond(players.map { PlayerView.fromPlayer(it) })
        }
        delete {
            call.respond(HttpStatusCode.OK)
            currentTournament.end()
        }
        post {
            val requestPlayer = call.receive<PlayerView>()
            try {
                val player = currentTournament.addPlayer(requestPlayer.nickname)
                call.respond(HttpStatusCode.Created, PlayerView.fromPlayer(player))
            } catch (e: PlayerAlreadyExistsException) {
                call.respond(HttpStatusCode.BadRequest, "Duplicate player nickname")
            }
        }
    }
}
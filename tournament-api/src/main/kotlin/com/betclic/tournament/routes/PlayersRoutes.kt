package com.betclic.tournament.routes

import com.betclic.tournament.domain.currentTournament
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class PlayerView(val id: String, val nickname: String)

fun Route.playersRouting() {
    route("/players") {
        get {
            val players = currentTournament.getPlayers()

            call.respond(players.map { PlayerView(it.id, it.nickname) })
        }
        delete {
            call.respond(HttpStatusCode.OK)
            currentTournament.end()
        }

    }
}
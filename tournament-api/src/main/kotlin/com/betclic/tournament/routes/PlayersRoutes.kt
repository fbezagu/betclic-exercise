package com.betclic.tournament.routes

import com.betclic.tournament.domain.currentTournament
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.Serializable

@Serializable
data class PlayerView(
    val nickname: String,
    val id: String = "",
    val score: Int = 0
) {
}

fun Route.playersRouting() {
    route("/players") {
        get {
            val players = currentTournament.getPlayers()

            call.respond(players.map { PlayerView(it.nickname) })
        }
        delete {
            call.respond(HttpStatusCode.OK)
            currentTournament.end()
        }
        post {
            val requestPlayer = call.receive<PlayerView>()
            val player = currentTournament.addPlayer(requestPlayer.nickname)
            call.respond(HttpStatusCode.Created, PlayerView(player.nickname, player.id, 0))
        }
    }
}
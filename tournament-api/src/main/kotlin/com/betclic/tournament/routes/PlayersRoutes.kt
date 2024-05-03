package com.betclic.tournament.routes

import com.betclic.tournament.domain.addPlayer.AddPlayer
import com.betclic.tournament.domain.addPlayer.AddPlayerUseCase
import com.betclic.tournament.domain.model.PlayerAlreadyExistsException
import com.betclic.tournament.domain.model.Repositories
import com.betclic.tournament.domain.model.Tournament
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.playersRouting() {
    val repositories by inject<Repositories>()

    route("/players") {
        get {
            val currentTournament = Tournament(repositories)
            val players = currentTournament.getPlayers()

            call.respond(players.map { PlayerView.fromPlayer(it) })
        }
        delete {
            call.respond(HttpStatusCode.OK)
            val currentTournament = Tournament(repositories)
            currentTournament.end()
        }
        post {
            val requestPlayer = call.receive<PlayerView>()
            try {
                val player = AddPlayerUseCase(repositories).add(requestPlayer.nickname)
                call.respond(HttpStatusCode.Created, PlayerView.fromPlayer(player))
            } catch (e: PlayerAlreadyExistsException) {
                call.respond(HttpStatusCode.BadRequest, "Duplicate player nickname")
            }
        }
    }
}
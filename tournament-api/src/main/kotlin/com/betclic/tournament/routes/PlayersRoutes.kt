package com.betclic.tournament.routes

import com.betclic.tournament.domain.addPlayer.AddPlayer
import com.betclic.tournament.domain.endTournament.EndTournament
import com.betclic.tournament.domain.endTournament.EndTournamentUseCase
import com.betclic.tournament.domain.getAllPlayers.GetAllPlayersUseCase
import com.betclic.tournament.domain.model.PlayerAlreadyExistsException
import com.betclic.tournament.domain.model.Repositories
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.playersRouting() {
    val repositories by inject<Repositories>()
    val addPlayer by inject<AddPlayer>()
    val endTournament by inject<EndTournament>()

    route("/players") {
        get {
            val useCase = GetAllPlayersUseCase(repositories)
            val players = useCase.getPlayers()
            call.respond(players.map { PlayerView.fromPlayer(it) })
        }
        delete {
            call.respond(HttpStatusCode.OK)
            endTournament.end()
        }
        post {
            val requestPlayer = call.receive<PlayerView>()
            try {
                val player = addPlayer.add(requestPlayer.nickname)
                call.respond(HttpStatusCode.Created, PlayerView.fromPlayer(player))
            } catch (e: PlayerAlreadyExistsException) {
                call.respond(HttpStatusCode.BadRequest, "Duplicate player nickname")
            }
        }
    }
}
package com.betclic.tournament.routes

import com.betclic.tournament.domain.getAllPlayers.GetAllPlayers
import com.betclic.tournament.domain.getPlayerInfo.GetPlayerInfoUseCase
import com.betclic.tournament.domain.model.Repositories
import com.betclic.tournament.domain.model.Score
import com.betclic.tournament.domain.updatePlayerScore.UpdatePlayerScore
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.playerRouting() {
    val repositories by inject<Repositories>()
    val updatePlayerScore by inject<UpdatePlayerScore>()
    val getAllPlayers by inject<GetAllPlayers>()

    route("/players/{id?}") {
        put {
            val id = call.parameters["id"] ?: return@put call.respond(HttpStatusCode.BadRequest, "No id provided")
            val newScore = Score(call.receive<PlayerView>().score)

            val player = repositories.players().getById(id) ?: return@put call.respond(
                HttpStatusCode.BadRequest,
                "Player with id <${id}> not found"
            )
            updatePlayerScore.updatePlayerScore(player, newScore)
            call.respond(HttpStatusCode.OK)
        }

        get {
            val id = call.parameters["id"] ?: return@get call.respond(
                getAllPlayers.getPlayers().map { PlayerView(it.nickname) })
            val getPlayerInfoUseCase = GetPlayerInfoUseCase(repositories)
            val player =
                getPlayerInfoUseCase.getPlayerInfo(id) ?: return@get call.respond(HttpStatusCode.NotFound)
            val playerView =
                PlayerView(player.nickname, player.id, player.score.i, getPlayerInfoUseCase.playerRank(player))
            call.respond(HttpStatusCode.OK, playerView)
        }
    }
}
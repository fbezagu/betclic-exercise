package com.betclic.tournament.routes

import com.betclic.tournament.domain.getRankings.GetRankingsUseCase
import com.betclic.tournament.domain.model.Repositories
import com.betclic.tournament.domain.model.Tournament
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.rankingsRouting() {
    val repositories by inject<Repositories>()

    route("/rankings") {
        get {
            val getRankings = GetRankingsUseCase(repositories)
            val players = getRankings.getRankings()

            call.respond(players.map { PlayerView.fromPlayer(it) })
        }
    }
}
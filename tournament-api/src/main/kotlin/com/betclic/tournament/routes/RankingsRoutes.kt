package com.betclic.tournament.routes

import com.betclic.tournament.domain.getRankings.GetRankings
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.rankingsRouting() {
    val getRankings by inject<GetRankings>()

    route("/rankings") {
        get {
            val players = getRankings.getRankings()

            call.respond(players.map { PlayerView.fromPlayer(it) })
        }
    }
}
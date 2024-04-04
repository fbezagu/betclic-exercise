package com.betclic.tournament.routes

import com.betclic.tournament.domain.Repositories
import com.betclic.tournament.domain.Tournament
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.koin.ktor.ext.inject

fun Route.rankingsRouting() {
    val repositories by inject<Repositories>()

    route("/rankings") {
        get {
            val currentTournament = Tournament(repositories)
            val players = currentTournament.getRankings()

            call.respond(players.map { PlayerView.fromPlayer(it) })
        }
    }
}
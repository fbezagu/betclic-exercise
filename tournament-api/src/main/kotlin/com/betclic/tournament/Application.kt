package com.betclic.tournament

import com.betclic.tournament.db.DynamoRepositories
import com.betclic.tournament.domain.addPlayer.AddPlayer
import com.betclic.tournament.domain.addPlayer.AddPlayerUseCase
import com.betclic.tournament.domain.model.Repositories
import com.betclic.tournament.domain.updatePlayerScore.UpdatePlayerScore
import com.betclic.tournament.domain.updatePlayerScore.UpdatePlayerScoreUseCase
import com.betclic.tournament.plugins.configureRouting
import com.betclic.tournament.plugins.configureSerialization
import io.ktor.server.application.*
import io.ktor.server.netty.*
import org.koin.dsl.module
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.koin

fun main(args: Array<String>) {
    EngineMain.main(args)
}

fun Application.module(testing: Boolean = false) {
    if (!testing) {
        koin {
            modules(appModule)
        }
        val repos by inject<Repositories>()

        repos.init()
    }

    configureSerialization()
    configureRouting()
}

val appModule = module {
    single<Repositories> { DynamoRepositories() }
    single<AddPlayer> { AddPlayerUseCase(get()) }
    single<UpdatePlayerScore> { UpdatePlayerScoreUseCase(get()) }
}

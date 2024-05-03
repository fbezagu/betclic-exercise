package com.betclic.tournament.routes

import com.betclic.tournament.db.MemoryRepositories
import com.betclic.tournament.domain.addPlayer.AddPlayer
import com.betclic.tournament.domain.addPlayer.AddPlayerUseCase
import com.betclic.tournament.domain.model.Repositories
import com.betclic.tournament.domain.updatePlayerScore.UpdatePlayerScore
import com.betclic.tournament.domain.updatePlayerScore.UpdatePlayerScoreUseCase
import com.betclic.tournament.module
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.koin.core.context.loadKoinModules
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get

open class BaseRoutesTest : KoinTest {
    companion object {
        @JvmStatic
        @BeforeAll
        fun `start koin`() {
            startKoin {
                modules(module(moduleDeclaration = moduleDeclaration()))
            }
        }

        @JvmStatic
        @AfterAll
        fun `stop koin`() {
            stopKoin()
        }

        private fun moduleDeclaration(): Module.() -> Unit = {
            single<Repositories> { MemoryRepositories() }
            single<AddPlayer> { AddPlayerUseCase(get()) }
            single<UpdatePlayerScore> { UpdatePlayerScoreUseCase(get()) }
        }
    }

    fun <R> withApp(test: suspend ApplicationTestBuilder.() -> R) = testApplication {
        environment { config = MapApplicationConfig() }
        application { module(testing = true) }
        loadKoinModules(module(moduleDeclaration = moduleDeclaration()))
        test()
    }

    protected fun ApplicationTestBuilder.createClient() = createClient { install(ContentNegotiation) { json() } }
    protected val playerRepository get() = repositories().players()
    private fun repositories(): Repositories = get()

}
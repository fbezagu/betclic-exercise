package com.betclic.tournament.routes

import com.betclic.tournament.db.MemoryRepositories
import com.betclic.tournament.domain.Player
import com.betclic.tournament.domain.repositories
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals

class PlayersRoutesTest {
    @Test
    fun canGetPlayersWhenNone() = testApplication {
        application {
            repositories = MemoryRepositories()
        }

        val response = client.get("/players")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("[]", response.bodyAsText())
    }

    @Test
    fun canGetPlayersWhenNotEmpty() = testApplication {
        application {
            repositories = MemoryRepositories()
            repositories.players().add(Player("menfin"))
            repositories.players().add(Player("Paul"))
        }

        val response = createClient().get("/players")

        assertEquals(HttpStatusCode.OK, response.status)
        val players = response.body<List<PlayerView>>()
        assertEquals(2, players.size)
        assertEquals("menfin", players.get(0).nickname)
        assertEquals("Paul", players.get(1).nickname)
    }

    private fun ApplicationTestBuilder.createClient() = createClient { install(ContentNegotiation) { json() } }

}
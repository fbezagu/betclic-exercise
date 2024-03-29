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
import kotlin.test.assertNotNull

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

    @Test
    fun canRemoveAllPlayers() = testApplication {
        application {
            repositories = MemoryRepositories()
            repositories.players().add(Player(""))
        }

        val response = client.delete("/players")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(0, repositories.players().count)
    }

    @Test
    fun canAddPlayer() = testApplication {
        application {
            repositories = MemoryRepositories()
            repositories.players().clear()
        }
        val client = createClient()

        val response = client.post("/players") {
            contentType(ContentType.Application.Json)
            setBody(PlayerView("Michel"))
        }

        assertEquals(HttpStatusCode.Created, response.status)
        val players = repositories.players()
        assertEquals(1, players.count)
        val playerInRepository = players.all().get(0)
        assertEquals("Michel", playerInRepository.nickname)
        val playerReturned = response.body<PlayerView>()
        assertNotNull(playerReturned)
        assertEquals(0, playerReturned.score)
        assertEquals(playerInRepository.id, playerReturned.id)
    }


    @Test
    fun canAddPlayerWithOnlyNickname() = testApplication {
        application {
            repositories = MemoryRepositories()
            repositories.players().clear()
        }
        val client = createClient()

        val response = client.post("/players") {
            contentType(ContentType.Application.Json)
            setBody("{\"nickname\":\"michel\"}")
        }

        assertEquals(HttpStatusCode.Created, response.status)
    }

    private fun ApplicationTestBuilder.createClient() = createClient { install(ContentNegotiation) { json() } }
}
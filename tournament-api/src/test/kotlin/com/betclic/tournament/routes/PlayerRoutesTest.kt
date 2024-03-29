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

class PlayerRoutesTest {
    @Test
    fun canUpdatePlayerScore() = testApplication {
        application { }
        val pierre = Player("Pierre")
        repositories = MemoryRepositories()
        repositories.players().add(pierre)
        val client = createClient()

        val response = client.put("/players/${pierre.id}") {
            contentType(ContentType.Application.Json)
            setBody(PlayerView("Pierre", score = 18))
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(18, pierre.score)
    }

    @Test
    fun errorThrownOnPutWhenNoId() = testApplication {
        application {
            repositories = MemoryRepositories()
        }
        val client = createClient { install(ContentNegotiation) { json() } }

        val response = client.put("/players/") {
            contentType(ContentType.Application.Json)
            setBody(PlayerView("Pierre", "", 18))
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals("No id provided", response.bodyAsText())
    }

    @Test
    fun errorThrownOnPutWhenPlayerWithIdNotFound() = testApplication {
        application {
            repositories = MemoryRepositories()
        }
        val client = createClient { install(ContentNegotiation) { json() } }

        val response = client.put("/players/unknown") {
            contentType(ContentType.Application.Json)
            setBody(PlayerView("Pierre", "", 18))
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals("Player with id <unknown> not found", response.bodyAsText())
    }


    @Test
    fun canGetOnePlayer() = testApplication {
        application {}
        val pierre = Player("Pierre")
        repositories = MemoryRepositories()
        repositories.players().add(pierre)
        val client = createClient()

        val response = client.get("/players/${pierre.id}")

        assertEquals(HttpStatusCode.OK, response.status)
        val player = response.body<PlayerView>()
        assertNotNull(player)
        assertEquals("Pierre", player.nickname)
        assertEquals(1, player.rank)
    }

    @Test
    fun canHandlePlayerNotFound() = testApplication {
        application { }
        repositories = MemoryRepositories()

        val response = client.get("/players/abcd")

        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun returnAllPlayersWhenNoNicknameProvided() = testApplication {
        application { }
        repositories = MemoryRepositories()
        repositories.players().add(Player("Michel"))
        val client = createClient()

        val response = client.get("/players/")

        assertEquals(HttpStatusCode.OK, response.status)
        val players = response.body<List<PlayerView>>()
        assertEquals(1, players.size)
    }

    private fun ApplicationTestBuilder.createClient() = createClient { install(ContentNegotiation) { json() } }
}
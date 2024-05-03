package com.betclic.tournament.routes

import com.betclic.tournament.domain.Player
import com.betclic.tournament.domain.Score
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class PlayerRoutesTest : BaseRoutesTest() {
    @Test
    fun `can update player score`() = withApp {
        val pierre = playerRepository.add(Player("Pierre"))
        val client = createClient()

        val response = client.put("/players/${pierre.id}") {
            contentType(ContentType.Application.Json)
            setBody(PlayerView("Pierre", score = 18))
        }

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(Score(18), playerRepository.getById(pierre.id)?.score)
    }

    @Test
    fun `error thrown on put when no id`() = withApp {
        val client = createClient()

        val response = client.put("/players/") {
            contentType(ContentType.Application.Json)
            setBody(PlayerView("Pierre", "", 18))
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals("No id provided", response.bodyAsText())
    }

    @Test
    fun `error thrown on put when player with id not found`() = withApp {
        val client = createClient()

        val response = client.put("/players/unknown") {
            contentType(ContentType.Application.Json)
            setBody(PlayerView("Pierre", "", 18))
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals("Player with id <unknown> not found", response.bodyAsText())
    }

    @Test
    fun `can get one player`() = withApp {
        val pierre = playerRepository.add(Player("Pierre"))
        val client = createClient()

        val response = client.get("/players/${pierre.id}")

        assertEquals(HttpStatusCode.OK, response.status)
        val player = response.body<PlayerView>()
        assertNotNull(player)
        assertEquals("Pierre", player.nickname)
        assertEquals(1, player.rank)
    }

    @Test
    fun `can handle player not found`() = withApp {
        val response = client.get("/players/abcd")

        assertEquals(HttpStatusCode.NotFound, response.status)
    }

    @Test
    fun `return all players when no nickname provided`() = withApp {
        playerRepository.add(Player("Michel"))
        val client = createClient()

        val response = client.get("/players/")

        assertEquals(HttpStatusCode.OK, response.status)
        val players = response.body<List<PlayerView>>()
        assertEquals(1, players.size)
    }

    @Test
    fun `can get one player rank`() = withApp {
        val pierre = playerRepository.add(Player("Pierre"))
        playerRepository.add(Player("Michel", score = Score(8)))
        val client = createClient()

        val response = client.get("/players/${pierre.id}")

        assertEquals(2, response.body<PlayerView>().rank)
    }
}


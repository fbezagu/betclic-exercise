package com.betclic.tournament.routes

import com.betclic.tournament.domain.Player
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class PlayersRoutesTest : BaseRoutesTest() {
    @Test
    fun `can get players when none`() = withApp {
        val response = client.get("/players")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals("[]", response.bodyAsText())
    }

    @Test
    fun `can get players when not empty`() = withApp {
        playerRepository.add(Player("menfin"))
        playerRepository.add(Player("Paul"))

        val response = createClient().get("/players")

        assertEquals(HttpStatusCode.OK, response.status)
        val players = response.body<List<PlayerView>>()
        assertEquals(2, players.size)
        assertEquals("menfin", players[0].nickname)
        assertEquals("Paul", players[1].nickname)
    }

    @Test
    fun `can remove all players`() = withApp {
        playerRepository.add(Player(""))

        val response = client.delete("/players")

        assertEquals(HttpStatusCode.OK, response.status)
        assertEquals(0, playerRepository.count)
    }

    @Test
    fun `can add player`() = withApp {
        playerRepository.clear()
        val client = createClient()

        val response = client.post("/players") {
            contentType(ContentType.Application.Json)
            setBody(PlayerView("Michel"))
        }

        assertEquals(HttpStatusCode.Created, response.status)
        assertEquals(1, playerRepository.count)
        val playerInRepository = playerRepository.all()[0]
        assertEquals("Michel", playerInRepository.nickname)
        val playerReturned = response.body<PlayerView>()
        assertNotNull(playerReturned)
        assertEquals(0, playerReturned.score)
        assertEquals(playerInRepository.id, playerReturned.id)
    }

    @Test
    fun `can add player with only nickname`() = withApp {
        playerRepository.clear()
        val client = createClient()

        val response = client.post("/players") {
            contentType(ContentType.Application.Json)
            setBody("{\"nickname\":\"michel\"}")
        }

        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun `can handle duplicate player nickname`() = withApp {
        playerRepository.add(Player("Michel"))
        val client = createClient()

        val response = client.post("/players") {
            contentType(ContentType.Application.Json)
            setBody(PlayerView("Michel"))
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
        assertEquals("Duplicate player nickname", response.bodyAsText())
    }

    @Test
    fun `id and score returned when getting all players`() = withApp {
        val menfin = Player("menfin", score = 43)
        playerRepository.add(menfin)

        val response = createClient().get("/players")

        val players = response.body<List<PlayerView>>()
        assertEquals(playerRepository.getByNickname("menfin")?.id, players[0].id)
        assertEquals(43, players[0].score)
    }
}
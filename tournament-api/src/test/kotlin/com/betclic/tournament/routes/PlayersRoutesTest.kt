package com.betclic.tournament.routes

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.betclic.tournament.domain.model.Player
import com.betclic.tournament.domain.model.Score
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

        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        assertThat("[]", response.bodyAsText())
    }

    @Test
    fun `can get players when not empty`() = withApp {
        playerRepository.add(Player("menfin"))
        playerRepository.add(Player("Paul"))

        val response = createClient().get("/players")

        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        val players = response.body<List<PlayerView>>()
        assertThat(players.size).isEqualTo(2)
        assertThat(players[0].nickname).isEqualTo("menfin")
        assertThat(players[1].nickname).isEqualTo("Paul")
    }

    @Test
    fun `can remove all players`() = withApp {
        playerRepository.add(Player(""))

        val response = client.delete("/players")

        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        assertThat(playerRepository.count).isEqualTo(0)
    }

    @Test
    fun `can add player`() = withApp {
        playerRepository.clear()
        val client = createClient()

        val response = client.post("/players") {
            contentType(ContentType.Application.Json)
            setBody(PlayerView("Michel"))
        }

        assertThat(response.status).isEqualTo(HttpStatusCode.Created)
        assertThat(playerRepository.count).isEqualTo(1)
        val playerInRepository = playerRepository.all()[0]
        assertThat("Michel", playerInRepository.nickname)
        val playerReturned = response.body<PlayerView>()
        assertNotNull(playerReturned)
        assertThat(playerReturned.score).isEqualTo(0)
        assertThat(playerInRepository.id, playerReturned.id)
    }

    @Test
    fun `can add player with only nickname`() = withApp {
        playerRepository.clear()
        val client = createClient()

        val response = client.post("/players") {
            contentType(ContentType.Application.Json)
            setBody("{\"nickname\":\"michel\"}")
        }

        assertThat(response.status).isEqualTo(HttpStatusCode.Created)
    }

    @Test
    fun `can handle duplicate player nickname`() = withApp {
        playerRepository.add(Player("Michel"))
        val client = createClient()

        val response = client.post("/players") {
            contentType(ContentType.Application.Json)
            setBody(PlayerView("Michel"))
        }

        assertThat(response.status).isEqualTo(HttpStatusCode.BadRequest)
        assertThat(response.bodyAsText()).isEqualTo("Duplicate player nickname")
    }

    @Test
    fun `id and score returned when getting all players`() = withApp {
        val menfin = Player("menfin", score = Score(43))
        playerRepository.add(menfin)

        val response = createClient().get("/players")

        val players = response.body<List<PlayerView>>()
        assertThat(players[0].id).isEqualTo(playerRepository.getByNickname("menfin")?.id)
        assertThat(players[0].score).isEqualTo(43)
    }
}
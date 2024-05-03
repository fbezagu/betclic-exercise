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

        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        assertThat(playerRepository.getById(pierre.id)?.score).isEqualTo(Score(18))
    }

    @Test
    fun `error thrown on put when no id`() = withApp {
        val client = createClient()

        val response = client.put("/players/") {
            contentType(ContentType.Application.Json)
            setBody(PlayerView("Pierre", "", 18))
        }

        assertThat(response.status).isEqualTo(HttpStatusCode.BadRequest)
        assertThat("No id provided", response.bodyAsText())
    }

    @Test
    fun `error thrown on put when player with id not found`() = withApp {
        val client = createClient()

        val response = client.put("/players/unknown") {
            contentType(ContentType.Application.Json)
            setBody(PlayerView("Pierre", "", 18))
        }

        assertThat(response.status).isEqualTo(HttpStatusCode.BadRequest)
        assertThat("Player with id <unknown> not found", response.bodyAsText())
    }

    @Test
    fun `can get one player`() = withApp {
        val pierre = playerRepository.add(Player("Pierre"))
        val client = createClient()

        val response = client.get("/players/${pierre.id}")

        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        val player = response.body<PlayerView>()
        assertNotNull(player)
        assertThat("Pierre", player.nickname)
        assertThat(player.rank).isEqualTo(1)
    }

    @Test
    fun `can handle player not found`() = withApp {
        val response = client.get("/players/abcd")

        assertThat(response.status).isEqualTo(HttpStatusCode.NotFound)
    }

    @Test
    fun `return all players when no nickname provided`() = withApp {
        playerRepository.add(Player("Michel"))
        val client = createClient()

        val response = client.get("/players/")

        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        val players = response.body<List<PlayerView>>()
        assertThat(players.size).isEqualTo(1)
    }

    @Test
    fun `can get one player rank`() = withApp {
        val pierre = playerRepository.add(Player("Pierre"))
        playerRepository.add(Player("Michel", score = Score(8)))
        val client = createClient()

        val response = client.get("/players/${pierre.id}")

        assertThat(response.body<PlayerView>().rank).isEqualTo(2)
    }
}


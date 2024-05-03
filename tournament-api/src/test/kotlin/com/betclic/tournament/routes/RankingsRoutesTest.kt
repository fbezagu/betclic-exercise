package com.betclic.tournament.routes

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.betclic.tournament.domain.model.Player
import com.betclic.tournament.domain.model.Score
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class RankingsRoutesTest : BaseRoutesTest() {
    @Test
    fun `can get player rankings`() = withApp {
        playerRepository.add(Player("menfin", score = Score(8)))
        playerRepository.add(Player("Paul", score = Score(16)))
        val client = createClient()

        val response = client.get("/rankings")

        assertThat(response.status).isEqualTo(HttpStatusCode.OK)
        val players = response.body<List<PlayerView>>()
        assertThat(players.size).isEqualTo(2)
        assertThat(players[0].nickname).isEqualTo("Paul")
        assertThat(players[1].nickname).isEqualTo("menfin")
    }
}
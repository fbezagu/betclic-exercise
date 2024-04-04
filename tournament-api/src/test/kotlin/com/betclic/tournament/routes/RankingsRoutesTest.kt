package com.betclic.tournament.routes

import com.betclic.tournament.domain.Player
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class RankingsRoutesTest : BaseRoutesTest(){
    @Test
    fun `can get player rankings`() = withApp {
        playerRepository.add(playerWithScore("menfin", 8))
        playerRepository.add(playerWithScore("Paul", 16))
        val client = createClient()

        val response = client.get("/rankings")

        assertEquals(HttpStatusCode.OK, response.status)
        val players = response.body<List<PlayerView>>()
        assertEquals(2, players.size)
        assertEquals("Paul", players[0].nickname)
        assertEquals("menfin", players[1].nickname)
    }

    private fun playerWithScore(nickname: String, score: Int): Player {
        val player = Player(nickname)
        player.score = score
        return player
    }

}
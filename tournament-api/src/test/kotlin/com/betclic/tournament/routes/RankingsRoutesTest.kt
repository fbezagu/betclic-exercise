package com.betclic.tournament.routes

import com.betclic.tournament.db.MemoryRepositories
import com.betclic.tournament.domain.Player
import com.betclic.tournament.domain.repositories
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals

class RankingsRoutesTest {
    @Test
    fun canGetPlayerRankings() = testApplication {
        application { }
        repositories = MemoryRepositories()
        repositories.players().add(playerWithScore("menfin", 8))
        repositories.players().add(playerWithScore("Paul", 16))
        val client = createClient()

        val response = client.get("/rankings")

        assertEquals(HttpStatusCode.OK, response.status)
        val players = response.body<List<PlayerView>>()
        assertEquals(2, players.size)
        assertEquals("Paul", players.get(0).nickname)
        assertEquals("menfin", players.get(1).nickname)
    }

    private fun playerWithScore(nickname: String, score: Int): Player {
        val player = Player(nickname)
        player.score = score
        return player
    }

    private fun ApplicationTestBuilder.createClient() = createClient { install(ContentNegotiation) { json() } }

}
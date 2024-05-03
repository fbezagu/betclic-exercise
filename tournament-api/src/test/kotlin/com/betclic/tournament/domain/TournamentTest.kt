package com.betclic.tournament.domain

import com.betclic.tournament.db.MemoryRepositories
import com.betclic.tournament.domain.model.Player
import com.betclic.tournament.domain.model.Score
import com.betclic.tournament.domain.model.Tournament
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class TournamentTest {

    private val repositories = MemoryRepositories()


    @Test
    fun `can get players`() {
        val tournament = Tournament(repositories)
        val paul = Player("Paul")
        val pierre = Player("Pierre")
        repositories.players().add(paul)
        repositories.players().add(pierre)

        val players = tournament.getPlayers()

        assertEquals(2, players.size)
        assertEquals("Paul", players[0].nickname)
        assertEquals("Pierre", players[1].nickname)
    }


}
package com.betclic.tournament.domain

import com.betclic.tournament.db.MemoryRepositories
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class TournamentTest {
    @Before
    fun before() {
        repositories = MemoryRepositories()
    }

    @Test
    fun canEndTournament() {
        repositories.players().add(Player("Paul"))

        Tournament().end()

        assertEquals(0, repositories.players().count)
    }

    @Test
    fun canGetPlayers() {
        val paul = Player("Paul")
        val pierre = Player("pierre")
        repositories.players().add(paul)
        repositories.players().add(pierre)

        val players = Tournament().getPlayers()

        assertEquals(2, players.size)
        assertEquals(paul, players[0])
        assertEquals(pierre, players[1])
    }
}
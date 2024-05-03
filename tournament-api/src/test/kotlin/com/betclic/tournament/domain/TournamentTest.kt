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


    @Test
    fun `player is ranked first when alone in tournament`() {
        val tournament = Tournament(repositories)
        val pierre = repositories.players().add(Player("Pierre"))

        val rank = tournament.playerRank(pierre)

        assertEquals(1, rank)
    }

    @Test
    fun `can get player rank when several player have higher score`() {
        val tournament = Tournament(repositories)
        val pierre = playerInTournamentWithScore(tournament, "Pierre", Score(5))
        playerInTournamentWithScore(tournament, "Michel", Score(8))
        playerInTournamentWithScore(tournament, "Paul", Score(10))

        val rank = tournament.playerRank(pierre)

        assertEquals(3, rank)
    }

    private fun playerInTournamentWithScore(tournament: Tournament, nick: String, score: Score): Player {
        return repositories.players().add(Player(nick, score = score))
    }

}
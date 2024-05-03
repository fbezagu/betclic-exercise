package com.betclic.tournament.domain

import com.betclic.tournament.db.MemoryPlayerRepository
import com.betclic.tournament.db.MemoryRepositories
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class TournamentTest {

    private val repositories = MemoryRepositories()

    @Test
    fun `can end tournament`() {
        val tournament = Tournament(repositories)
        repositories.players().add(Player("Paul"))

        tournament.end()

        assertEquals(0, repositories.players().count)
    }

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
    fun `can add new player`() {
        val tournament = Tournament(repositories)

        val player = tournament.addPlayer("Michel")

        assertEquals("Michel", player.nickname)
        assertEquals(1, repositories.players().count)
    }

    @Test
    fun `cant add player with same nickname`() {
        val tournament = Tournament(repositories)
        repositories.players().add(Player("Paul"))

        Assertions.assertThrows(PlayerAlreadyExistsException::class.java) {
            tournament.addPlayer("Paul")
        }

        assertEquals(1, repositories.players().count)
    }

    @Test
    fun `can update player score`() {
        val tournament = Tournament(repositories)
        val michel = repositories.players().add(Player("Michel"))

        tournament.updatePlayerScore(michel, Score(42))

        assertEquals(Score(42), repositories.players().all()[0].score)
    }

    @Test
    fun `can update player score when not first in repository`() {
        val tournament = Tournament(repositories)
        repositories.players().add(Player("Michel"))
        val paul = repositories.players().add(Player("Paul"))

        tournament.updatePlayerScore(paul, Score(8))

        assertEquals(Score(8), repositories.players().all()[1].score)
    }

    @Test
    fun `player is ranked first when alone in tournament`() {
        val tournament = Tournament(repositories)
        val pierre = tournament.addPlayer("Pierre")

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
        val player = tournament.addPlayer(nick)
        return tournament.updatePlayerScore(player, score)
    }

}
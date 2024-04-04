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
    fun canEndTournament() {
        val tournament = Tournament(repositories)
        repositories.players().add(Player("Paul"))

        tournament.end()

        assertEquals(0, repositories.players().count)
    }

    @Test
    fun canGetPlayers() {
        val tournament = Tournament(repositories)
        val paul = Player("Paul")
        val pierre = Player("pierre")
        repositories.players().add(paul)
        repositories.players().add(pierre)

        val players = tournament.getPlayers()

        assertEquals(2, players.size)
        assertEquals(paul, players[0])
        assertEquals(pierre, players[1])
    }

    @Test
    fun canAddNewPlayer() {
        val tournament = Tournament(repositories)

        val player = tournament.addPlayer("Michel")

        assertEquals("Michel", player.nickname)
        assertEquals(1, repositories.players().count)
    }

    @Test
    fun cantAddPlayerWithSameNickname() {
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
        val michel = Player("Michel")
        repositories.players().add(michel)

        tournament.updatePlayerScore(michel, 42)

        assertEquals(42, repositories.players().all()[0].score)
        val playerRepository = repositories.players() as MemoryPlayerRepository
        assertContains(playerRepository.updatedItems, michel)
    }

    @Test
    fun `can update player score when not first in repository`() {
        val tournament = Tournament(repositories)
        repositories.players().add(Player("Michel"))
        val paul = Player("Paul")
        repositories.players().add(paul)

        tournament.updatePlayerScore(paul, 8)

        assertEquals(8, repositories.players().all()[1].score)
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
        val pierre = tournament.addPlayer("Pierre")
        pierre.score = 5
        tournament.addPlayer("Michel").score = 8
        tournament.addPlayer("Paul").score = 10

        val rank = tournament.playerRank(pierre)

        assertEquals(3, rank)
    }

}
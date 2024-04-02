package com.betclic.tournament.domain

import com.betclic.tournament.db.MemoryPlayerRepository
import com.betclic.tournament.db.MemoryRepositories
import org.junit.Before
import org.junit.Test
import kotlin.test.assertContains
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

    @Test
    fun canAddNewPlayer() {
        val player = Tournament().addPlayer("Michel")

        assertEquals("Michel", player.nickname)
        assertEquals(1, repositories.players().count)
    }

    @Test(expected = PlayerAlreadyExistsException::class)
    fun cantAddPlayerWithSameNickname() {
        repositories.players().add(Player("Paul"))

        Tournament().addPlayer("Paul")

        assertEquals(1, repositories.players().count)
    }

    @Test
    fun canUpdatePlayerScore() {
        val michel = Player("Michel")
        repositories.players().add(michel)

        Tournament().updatePlayerScore(michel, 42)

        assertEquals(42, repositories.players().all()[0].score)
        val playerRepository = repositories.players() as MemoryPlayerRepository
        assertContains(playerRepository.updatedItems, michel)
    }

    @Test
    fun canUpdatePlayerScoreWhenNotFirstInRepository() {
        repositories.players().add(Player("Michel"))
        val paul = Player("Paul")
        repositories.players().add(paul)

        Tournament().updatePlayerScore(paul, 8)

        assertEquals(8, repositories.players().all()[1].score)
    }

    @Test
    fun playerIsRankedFirstWhenAloneInTournament() {
        val tournament = Tournament()
        val pierre = tournament.addPlayer("Pierre")

        val rank = tournament.playerRank(pierre)

        assertEquals(1, rank)
    }

    @Test
    fun canGetPlayerRankWhenSeveralPlayerHaveHigherScore() {
        val tournament = Tournament()
        val pierre = tournament.addPlayer("Pierre")
        pierre.score = 5
        tournament.addPlayer("Michel").score = 8
        tournament.addPlayer("Paul").score = 10

        val rank = tournament.playerRank(pierre)

        assertEquals(3, rank)
    }

}
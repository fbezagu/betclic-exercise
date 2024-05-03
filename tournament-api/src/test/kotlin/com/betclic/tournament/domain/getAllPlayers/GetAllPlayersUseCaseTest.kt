package com.betclic.tournament.domain.getAllPlayers

import com.betclic.tournament.db.MemoryRepositories
import com.betclic.tournament.domain.model.Player
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GetAllPlayersUseCaseTest {
    private val repositories = MemoryRepositories()

    @Test
    fun `can get players`() {
        val useCase = GetAllPlayersUseCase(repositories)
        val paul = Player("Paul")
        val pierre = Player("Pierre")
        repositories.players().add(paul)
        repositories.players().add(pierre)

        val players = useCase.getPlayers()

        assertEquals(2, players.size)
        assertEquals("Paul", players[0].nickname)
        assertEquals("Pierre", players[1].nickname)
    }
}
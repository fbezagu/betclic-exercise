package com.betclic.tournament.domain.getAllPlayers

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.betclic.tournament.db.MemoryRepositories
import com.betclic.tournament.domain.model.Player
import org.junit.jupiter.api.Test

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

        assertThat( players.size).isEqualTo(2)
        assertThat( players[0].nickname).isEqualTo("Paul")
        assertThat( players[1].nickname).isEqualTo("Pierre")
    }
}
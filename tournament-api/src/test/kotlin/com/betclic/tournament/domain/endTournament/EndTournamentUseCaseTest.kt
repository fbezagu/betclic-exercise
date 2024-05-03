package com.betclic.tournament.domain.endTournament

import com.betclic.tournament.db.MemoryRepositories
import com.betclic.tournament.domain.model.Player
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class EndTournamentUseCaseTest {
    private val repositories = MemoryRepositories()

    @Test
    fun `can end tournament`() {
        val uc = EndTournamentUseCase(repositories)
        repositories.players().add(Player("Paul"))

        uc.end()

        assertEquals(0, repositories.players().count)
    }

}
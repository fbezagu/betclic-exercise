package com.betclic.tournament.domain.updatePlayerScore

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.betclic.tournament.db.MemoryRepositories
import com.betclic.tournament.domain.model.Player
import com.betclic.tournament.domain.model.Score
import org.junit.jupiter.api.Test

class UpdatePlayerScoreUseCaseTest {
    private val repositories = MemoryRepositories()

    @Test
    fun `can update player score`() {
        val useCase = UpdatePlayerScoreUseCase(repositories)
        val michel = repositories.players().add(Player("Michel"))

        useCase.updatePlayerScore(michel, Score(42))

        assertThat(repositories.players().all()[0].score).isEqualTo(Score(42))
    }

    @Test
    fun `can update player score when not first in repository`() {
        val useCase = UpdatePlayerScoreUseCase(repositories)
        repositories.players().add(Player("Michel"))
        val paul = repositories.players().add(Player("Paul"))

        useCase.updatePlayerScore(paul, Score(8))

        assertThat( repositories.players().all()[1].score).isEqualTo(Score(8))
    }

}
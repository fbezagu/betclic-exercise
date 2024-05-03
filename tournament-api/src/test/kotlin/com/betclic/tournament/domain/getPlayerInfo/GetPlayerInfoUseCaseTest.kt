package com.betclic.tournament.domain.getPlayerInfo

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.betclic.tournament.db.MemoryRepositories
import com.betclic.tournament.domain.model.Player
import com.betclic.tournament.domain.model.Score
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class GetPlayerInfoUseCaseTest {
    private val repositories = MemoryRepositories()

    @Test
    fun `player is ranked first when alone in tournament`() {
        val uc = GetPlayerInfoUseCase(repositories)
        val pierre = repositories.players().add(Player("Pierre"))

        val rank = uc.playerRank(pierre)

        assertThat( rank).isEqualTo(1)
    }

    @Test
    fun `can get player rank when several player have higher score`() {
        val uc = GetPlayerInfoUseCase(repositories)
        val pierre = repositories.players().add(Player("Pierre", score = Score(5)))
        repositories.players().add(Player("Michel", score = Score(8)))
        repositories.players().add(Player("Paul", score = Score(10)))

        val rank = uc.playerRank(pierre)

        assertThat(rank).isEqualTo(3)
    }

}
package com.betclic.tournament.domain.addPlayer

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.betclic.tournament.db.MemoryRepositories
import com.betclic.tournament.domain.model.Player
import com.betclic.tournament.domain.model.PlayerAlreadyExistsException
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class AddPlayerUseCaseTest {
    private val repositories = MemoryRepositories()

    @Test
    fun `can add new player`() {
        val uc = AddPlayerUseCase(repositories)

        val player = uc.add("Michel")

        assertThat(player.nickname).isEqualTo("Michel")
        assertThat(repositories.players().count).isEqualTo(1)
    }

    @Test
    fun `cant add player with same nickname`() {
        val uc = AddPlayerUseCase(repositories)
        repositories.players().add(Player("Paul"))

        Assertions.assertThrows(PlayerAlreadyExistsException::class.java) {
            uc.add("Paul")
        }

        assertThat(repositories.players().count).isEqualTo(1)
    }
}
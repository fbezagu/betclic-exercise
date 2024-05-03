package com.betclic.tournament.domain.addPlayer

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

        assertEquals("Michel", player.nickname)
        assertEquals(1, repositories.players().count)
    }

    @Test
    fun `cant add player with same nickname`() {
        val uc = AddPlayerUseCase(repositories)
        repositories.players().add(Player("Paul"))

        Assertions.assertThrows(PlayerAlreadyExistsException::class.java) {
            uc.add("Paul")
        }

        assertEquals(1, repositories.players().count)
    }
}
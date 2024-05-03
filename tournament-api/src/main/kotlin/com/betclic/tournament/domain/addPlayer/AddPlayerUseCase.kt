package com.betclic.tournament.domain.addPlayer

import com.betclic.tournament.domain.model.Player
import com.betclic.tournament.domain.model.PlayerAlreadyExistsException
import com.betclic.tournament.domain.model.Repositories

class AddPlayerUseCase(private val repositories: Repositories) : AddPlayer {
    override fun add(nickname: String): Player {
        if (repositories.players().getByNickname(nickname) != null) {
            throw PlayerAlreadyExistsException()
        }
        return repositories.players().add(Player(nickname))
    }
}
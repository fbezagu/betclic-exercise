package com.betclic.tournament.domain.addPlayer

import com.betclic.tournament.domain.model.Player

interface AddPlayer {
    fun add(nickname: String): Player
}
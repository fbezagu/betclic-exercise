package com.betclic.tournament.domain.getAllPlayers

import com.betclic.tournament.domain.model.Player

interface GetAllPlayers {
    fun getPlayers(): List<Player>
}
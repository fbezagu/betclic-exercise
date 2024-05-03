package com.betclic.tournament.domain.getRankings

import com.betclic.tournament.domain.model.Player

interface GetRankings {
    fun getRankings(): List<Player>
}
package com.betclic.tournament.domain.getPlayerInfo

import com.betclic.tournament.domain.model.Player

interface GetPlayerInfo {
    fun getPlayerInfo(id: String): Player?
    fun playerRank(player: Player): Int

}
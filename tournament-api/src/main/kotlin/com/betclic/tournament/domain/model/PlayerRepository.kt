package com.betclic.tournament.domain.model

interface PlayerRepository {
    val count: Int
    fun add(player: Player): Player
    fun all(): List<Player>
    fun clear()
    fun getByNickname(nickname: String): Player?
    fun update(player: Player)
    fun getById(id: String): Player?
    fun countWithScoreHigherThan(score: Score): Int
    fun allSortedByScore(): List<Player>
}
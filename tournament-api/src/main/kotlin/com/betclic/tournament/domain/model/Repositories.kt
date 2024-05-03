package com.betclic.tournament.domain.model

abstract class Repositories {
    abstract fun players(): PlayerRepository
    open fun init() {}
}


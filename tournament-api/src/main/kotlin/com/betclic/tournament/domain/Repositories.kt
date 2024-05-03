package com.betclic.tournament.domain

abstract class Repositories {
    abstract fun players(): PlayerRepository
    open fun init() {}
}


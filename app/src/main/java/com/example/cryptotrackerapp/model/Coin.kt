package com.example.cryptotrackerapp.model

data class Coin(
    val config: Config,
    val `data`: List<Data>,
    val usage: Usage,
 )
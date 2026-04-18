package com.Componentes301.tiorico.model

data class Jugador(
    val id: String = "",
    val nombre: String = "",
    var dinero: Int = 1000,
    var turnoActual: Int = 1,
    var eliminado: Boolean = false
)
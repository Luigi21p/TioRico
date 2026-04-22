package com.Componentes301.tiorico.logic

import com.Componentes301.tiorico.model.EventoAleatorio
import com.Componentes301.tiorico.model.Jugador

object GameManager {

    const val CAPITAL_INICIAL = 1000
    const val META = 5000

    private val eventosAleatorios = listOf(
        EventoAleatorio("You found money on the street!", 200),
        EventoAleatorio("Your car broke down, repair costs.", -300),
        EventoAleatorio("You received an unexpected bonus.", 400),
        EventoAleatorio("You were fined for speeding.", -150),
        EventoAleatorio("You won a local contest.", 500)
    )

    // ── Main actions ──────────────────────────────────────

    fun ahorrar(jugador: Jugador): String {
        val ganancia = 100
        jugador.dinero += ganancia
        return "You saved money successfully. +$$ganancia"
    }

    fun invertir(jugador: Jugador): String {
        val exito = (1..2).random() == 1
        return if (exito) {
            val ganancia = (200..600).random()
            jugador.dinero += ganancia
            "Successful investment! +$$ganancia"
        } else {
            val perdida = (100..400).random()
            jugador.dinero -= perdida
            "Bad investment. -$$perdida"
        }
    }

    fun gastar(jugador: Jugador): String {
        val gasto = (150..350).random()
        jugador.dinero -= gasto
        return "You spent $$gasto."
    }

    // ── Random event (occurs in 40% of turns) ──────────────

    fun verificarEventoAleatorio(jugador: Jugador): String? {
        val ocurre = (1..10).random() <= 4
        if (!ocurre) return null

        val evento = eventosAleatorios.random()
        jugador.dinero += evento.impacto
        return "🎲 Event: ${evento.descripcion} (${if (evento.impacto > 0) "+" else ""}${evento.impacto})"
    }

    // ── State checks ──────────────────────────────────

    fun verificarVictoria(jugador: Jugador): Boolean {
        return jugador.dinero >= META
    }

    fun verificarDerrota(jugador: Jugador): Boolean {
        if (jugador.dinero <= 0) {
            jugador.dinero = 0
            jugador.eliminado = true
            return true
        }
        return false
    }

    fun avanzarTurno(jugador: Jugador) {
        jugador.turnoActual++
    }

    fun crearJugador(nombre: String): Jugador {
        return Jugador(nombre = nombre, dinero = CAPITAL_INICIAL)
    }

}
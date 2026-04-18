package com.Componentes301.tiorico.logic

import com.Componentes301.tiorico.model.EventoAleatorio
import com.Componentes301.tiorico.model.Jugador

object GameManager {

    const val CAPITAL_INICIAL = 1000
    const val META = 5000

    private val eventosAleatorios = listOf(
        EventoAleatorio("¡Encontraste dinero en la calle!", 200),
        EventoAleatorio("Tu carro se dañó, gastos de taller.", -300),
        EventoAleatorio("Recibiste un bono inesperado.", 400),
        EventoAleatorio("Fuiste multado por exceso de velocidad.", -150),
        EventoAleatorio("Ganaste un concurso local.", 500)
    )

    // ── Acciones principales ──────────────────────────────────────

    fun ahorrar(jugador: Jugador): String {
        val ganancia = 100
        jugador.dinero += ganancia
        return "Ahorraste correctamente. +$$ganancia"
    }

    fun invertir(jugador: Jugador): String {
        val exito = (1..2).random() == 1
        return if (exito) {
            val ganancia = (200..600).random()
            jugador.dinero += ganancia
            "¡Inversión exitosa! +$$ganancia"
        } else {
            val perdida = (100..400).random()
            jugador.dinero -= perdida
            "Mala inversión. -$$perdida"
        }
    }

    fun gastar(jugador: Jugador): String {
        val gasto = (150..350).random()
        jugador.dinero -= gasto
        return "Gastaste $$gasto."
    }

    // ── Evento aleatorio (ocurre -40% de los turnos) ──────────────

    fun verificarEventoAleatorio(jugador: Jugador): String? {
        val ocurre = (1..10).random() <= 4
        if (!ocurre) return null

        val evento = eventosAleatorios.random()
        jugador.dinero += evento.impacto
        return "🎲 Evento: ${evento.descripcion} (${if (evento.impacto > 0) "+" else ""}${evento.impacto})"
    }

    // ── Verificaciones de estado ──────────────────────────────────

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
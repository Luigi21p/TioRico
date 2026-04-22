package com.Componentes301.tiorico.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.Componentes301.tiorico.logic.GameManager
import com.Componentes301.tiorico.model.Jugador

enum class EstadoJuego {
    JUGANDO,
    VICTORIA,
    DERROTA
}

class GameViewModel : ViewModel() {

    private val _jugador = mutableStateOf(GameManager.crearJugador("Jugador"))
    val jugador: State<Jugador> = _jugador

    private val _mensaje = mutableStateOf("¡Empieza tu camino a la fortuna!")
    val mensaje: State<String> = _mensaje

    private val _estadoJuego = mutableStateOf(EstadoJuego.JUGANDO)
    val estadoJuego: State<EstadoJuego> = _estadoJuego

    fun realizarAccion(tipo: String) {
        if (_estadoJuego.value != EstadoJuego.JUGANDO) return

        // GameManager muta el jugador y retorna String
        val msgAccion = when (tipo) {
            "ahorrar" -> GameManager.ahorrar(_jugador.value)
            "invertir" -> GameManager.invertir(_jugador.value)
            "gastar"  -> GameManager.gastar(_jugador.value)
            else      -> GameManager.gastar(_jugador.value)
        }
        _mensaje.value = msgAccion

        // Evento aleatorio
        val msgEvento = GameManager.verificarEventoAleatorio(_jugador.value)
        if (msgEvento != null) _mensaje.value += "\n$msgEvento"

        // Avanzar turno
        GameManager.avanzarTurno(_jugador.value)

        // Forzar actualización de la UI
        _jugador.value = _jugador.value.copy()

        // Verificar victoria o derrota
        when {
            GameManager.verificarVictoria(_jugador.value) -> {
                _estadoJuego.value = EstadoJuego.VICTORIA
                _mensaje.value = "🏆 ¡Alcanzaste la meta de $${GameManager.META}!"
            }
            GameManager.verificarDerrota(_jugador.value) -> {
                _estadoJuego.value = EstadoJuego.DERROTA
                _mensaje.value = "💸 Te quedaste sin dinero..."
            }
        }
    }

    fun reiniciarJuego() {
        _jugador.value = GameManager.crearJugador("Jugador")
        _mensaje.value = "¡Empieza tu camino a la fortuna!"
        _estadoJuego.value = EstadoJuego.JUGANDO
    }
}
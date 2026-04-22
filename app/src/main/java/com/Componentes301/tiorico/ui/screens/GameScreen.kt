package com.Componentes301.tiorico.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.Componentes301.tiorico.model.Jugador
import com.Componentes301.tiorico.ui.viewmodel.GameViewModel

/**
 * PANTALLA DE JUEGO
 * ─────────────────────────────────────────────────
 * TODO (Compañeros): Implementar aquí:
 *   - Diseño de la pantalla principal del juego
 *   - Botones: Ahorrar, Invertir, Gastar
 *   - Pantalla de Victoria (estadoJuego == VICTORIA)
 *   - Pantalla de Derrota  (estadoJuego == DERROTA)
 *
 * Datos disponibles del ViewModel:
 *   - jugador.dinero      → dinero actual
 *   - jugador.turnoActual → turno actual
 *   - jugador.nombre      → nombre del jugador
 *   - mensaje             → resultado de la última acción
 *   - estadoJuego         → JUGANDO / VICTORIA / DERROTA
 *
 * Funciones disponibles:
 *   - viewModel.realizarAccion("ahorrar")
 *   - viewModel.realizarAccion("invertir")
 *   - viewModel.realizarAccion("gastar")
 *   - viewModel.reiniciarJuego()
 *   - onSalir() → regresa a la pantalla de bienvenida
 * ─────────────────────────────────────────────────
 */
@Composable
fun GameScreen(viewModel: GameViewModel, onSalir: () -> Unit) {
    val jugador by viewModel.jugador
    val mensaje by viewModel.mensaje
    val estadoJuego by viewModel.estadoJuego

    // TODO: Tus compañeros reemplazan este contenido
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Dinero: $${jugador.dinero}")
        Text("Turno: ${jugador.turnoActual}")
        Text(mensaje)
        Spacer(modifier = Modifier.height(20.dp))
        Button(onClick = { viewModel.realizarAccion("ahorrar") }) { Text("Ahorrar") }
        Button(onClick = { viewModel.realizarAccion("invertir") }) { Text("Invertir") }
        Button(onClick = { viewModel.realizarAccion("gastar") }) { Text("Gastar") }
        Spacer(modifier = Modifier.height(20.dp))
        OutlinedButton(onClick = onSalir, modifier = Modifier.fillMaxWidth()) {
            Text("Salir del juego")
        }
    }
}
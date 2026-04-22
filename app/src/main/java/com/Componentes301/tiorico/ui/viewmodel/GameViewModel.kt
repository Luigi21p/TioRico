package com.Componentes301.tiorico.ui.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.Componentes301.tiorico.logic.GameManager
import com.Componentes301.tiorico.model.Jugador
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database

enum class EstadoJuego {
    JUGANDO,
    VICTORIA,
    DERROTA
}

class GameViewModel : ViewModel() {
    private val db = Firebase.database.reference
    private var salaListener: ValueEventListener? = null

    private val _jugador = mutableStateOf(GameManager.crearJugador("Jugador"))
    val jugador: State<Jugador> = _jugador

    private val _mensaje = mutableStateOf("¡Empieza tu camino a la fortuna!")
    val mensaje: State<String> = _mensaje

    private val _estadoJuego = mutableStateOf(EstadoJuego.JUGANDO)
    val estadoJuego: State<EstadoJuego> = _estadoJuego

    // Variables para guardar la sesión
    private var codigoActual: String = ""
    private var nombreActual: String = ""

    fun conectarAFirebase(codigo: String, nombre: String) {
        this.codigoActual = codigo
        this.nombreActual = nombre

        // Escuchar cambios de MI dinero en Firebase (por si otro evento me afecta)
        salaListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dineroEnNube = snapshot.child("dinero").getValue(Int::class.java) ?: 1000

                // Actualizamos la UI con lo que diga Firebase
                _jugador.value = _jugador.value.copy(
                    nombre = nombre,
                    dinero = dineroEnNube
                )
            }
            override fun onCancelled(error: DatabaseError) {}
        }

        db.child("salas").child(codigo).child("jugadores").child(nombre)
            .addValueEventListener(salaListener!!)
    }

    fun realizarAccion(tipo: String) {
        if (_estadoJuego.value != EstadoJuego.JUGANDO) return

        // 1. Ejecutar la acción lógica
        val msgAccion = when (tipo) {
            "ahorrar" -> GameManager.ahorrar(_jugador.value)
            "invertir" -> GameManager.invertir(_jugador.value)
            "gastar"  -> GameManager.gastar(_jugador.value)
            else      -> GameManager.gastar(_jugador.value)
        }
        _mensaje.value = msgAccion

        // 2. Verificar evento aleatorio
        val msgEvento = GameManager.verificarEventoAleatorio(_jugador.value)
        if (msgEvento != null) _mensaje.value += "\n$msgEvento"

        // ── CAMBIO CLAVE AQUÍ ──
        // 3. Avanzar el turno en la lógica
        GameManager.avanzarTurno(_jugador.value)

        // 4. FORZAR que Compose vea el cambio creando una COPIA del objeto
        // Esto disparará la actualización del "CURRENT TURN" en la pantalla
        _jugador.value = _jugador.value.copy()

        // 5. Actualizar Firebase
        if (codigoActual.isNotEmpty()) {
            val ref = db.child("salas").child(codigoActual).child("jugadores").child(nombreActual)
            ref.child("dinero").setValue(_jugador.value.dinero)
            // Opcional: También podrías guardar el turno en Firebase si quieres
            ref.child("turnoActual").setValue(_jugador.value.turnoActual)
        }

        verificarFinal()
    }

    private fun verificarFinal() {
        when {
            GameManager.verificarVictoria(_jugador.value) -> _estadoJuego.value = EstadoJuego.VICTORIA
            GameManager.verificarDerrota(_jugador.value) -> _estadoJuego.value = EstadoJuego.DERROTA
        }
    }

    override fun onCleared() {
        super.onCleared()
        // Limpiar el listener al cerrar el ViewModel
        salaListener?.let {
            db.child("salas").child(codigoActual).child("jugadores").child(nombreActual)
                .removeEventListener(it)
        }
    }
    fun reiniciarJuego() {
        // GameManager.crearJugador ya debería devolver un turnoActual = 1
        _jugador.value = GameManager.crearJugador(nombreActual.ifEmpty { "Jugador" })
        _mensaje.value = "¡Empieza tu camino a la fortuna!"
        _estadoJuego.value = EstadoJuego.JUGANDO

        if (codigoActual.isNotEmpty()) {
            val ref = db.child("salas").child(codigoActual).child("jugadores").child(nombreActual)
            ref.child("dinero").setValue(GameManager.CAPITAL_INICIAL)
            ref.child("turnoActual").setValue(1)
        }
    }
}
package com.Componentes301.tiorico.ui.sala

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.Componentes301.tiorico.MainActivity
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.Componentes301.tiorico.R

class SalaEsperaActivity : AppCompatActivity() {

    private val db = Firebase.database.reference
    private lateinit var salaListener: ValueEventListener
    private lateinit var codigoSala: String
    private lateinit var nombreJugador: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sala_espera)

        codigoSala = intent.getStringExtra("codigoSala") ?: ""
        nombreJugador = intent.getStringExtra("nombreJugador") ?: ""

        val tvCodigo = findViewById<TextView>(R.id.tvCodigoSala)
        val tvJugadores = findViewById<TextView>(R.id.tvJugadores)
        val tvLista = findViewById<TextView>(R.id.tvListaJugadores)

        tvCodigo.text = "Code: $codigoSala"

        // ── Listen for real-time changes ─────────────────────
        salaListener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val jugadores = snapshot.child("jugadores").children.toList()
                val cantidad = jugadores.size
                val nombres = jugadores.mapNotNull {
                    it.child("nombre").getValue(String::class.java)
                }

                tvJugadores.text = "Connected players: $cantidad/4"
                tvLista.text = nombres.joinToString("\n")

                // When there are 4 players, start the game
                if (cantidad >= 4) {
                    irAlJuego()
                }
            }

            override fun onCancelled(error: DatabaseError) {}
        }

        db.child("salas").child(codigoSala)
            .addValueEventListener(salaListener)
    }

    private fun irAlJuego() {
        // Navigate to MainActivity (it contains the Compose NavHost)
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("codigoSala", codigoSala)
        intent.putExtra("nombreJugador", nombreJugador)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) // Clear back stack to avoid returning to the room
        startActivity(intent)
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Important: remove the listener to prevent memory leaks
        db.child("salas").child(codigoSala).removeEventListener(salaListener)
    }
}
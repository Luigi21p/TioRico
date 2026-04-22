package com.Componentes301.tiorico.ui.sala

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.Componentes301.tiorico.R

class InicioActivity : AppCompatActivity() {

    // Firebase database reference
    private val db = Firebase.database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        // Top bar configuration with back button
        supportActionBar?.show()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Online Multiplayer"

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etCodigo = findViewById<EditText>(R.id.etCodigoSala)
        val btnCrear = findViewById<Button>(R.id.btnCrearSala)
        val btnUnirse = findViewById<Button>(R.id.btnUnirse)

        // ── CREATE ROOM BUTTON ────────────────────────────────────
        btnCrear.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            if (nombre.isEmpty()) {
                Toast.makeText(this, "Please enter your name", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Generate a random 5-letter code (e.g., ABCDE)
            val codigo = (1..5)
                .map { ('A'..'Z').random() }
                .joinToString("")

            crearSala(codigo, nombre)
        }

        // ── JOIN ROOM BUTTON ─────────────────────────────────
        btnUnirse.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val codigo = etCodigo.text.toString().trim().uppercase()

            if (nombre.isEmpty() || codigo.isEmpty()) {
                Toast.makeText(this, "Enter name and room code", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            unirseASala(codigo, nombre)
        }
    }

    private fun crearSala(codigo: String, nombre: String) {
        val jugador = mapOf(
            "nombre" to nombre,
            "dinero" to 1000,
            "listo" to false
        )

        // Try to save in: rooms -> [CODE] -> players -> [NAME]
        db.child("salas").child(codigo).child("jugadores").child(nombre)
            .setValue(jugador)
            .addOnSuccessListener {
                Log.d("Firebase_TioRico", "Room created successfully: $codigo")
                irASalaEspera(codigo, nombre)
            }
            .addOnFailureListener { e ->
                Log.e("Firebase_TioRico", "Error creating room", e)
                Toast.makeText(this, "Firebase error: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun unirseASala(codigo: String, nombre: String) {
        db.child("salas").child(codigo).get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.exists()) {
                    Toast.makeText(this, "Room $codigo does not exist", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                val jugadoresActuales = snapshot.child("jugadores").childrenCount
                if (jugadoresActuales >= 4) {
                    Toast.makeText(this, "Room is full (Max 4)", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                val jugador = mapOf(
                    "nombre" to nombre,
                    "dinero" to 1000,
                    "listo" to false
                )

                db.child("salas").child(codigo).child("jugadores").child(nombre)
                    .setValue(jugador)
                    .addOnSuccessListener {
                        Log.d("Firebase_TioRico", "Joined room: $codigo")
                        irASalaEspera(codigo, nombre)
                    }
            }
            .addOnFailureListener { e ->
                Log.e("Firebase_TioRico", "Error joining room", e)
                Toast.makeText(this, "Connection error", Toast.LENGTH_SHORT).show()
            }
    }

    private fun irASalaEspera(codigo: String, nombre: String) {
        val intent = Intent(this, SalaEsperaActivity::class.java).apply {
            putExtra("codigoSala", codigo)
            putExtra("nombreJugador", nombre)
        }
        startActivity(intent)
        // We don't call finish() in case the user wants to go back
    }

    // Logic for the top bar back button
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
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

    // Referencia a la base de datos de Firebase
    private val db = Firebase.database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        // Configuración de la barra superior con botón de volver
        supportActionBar?.show()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Multijugador Online"

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etCodigo = findViewById<EditText>(R.id.etCodigoSala)
        val btnCrear = findViewById<Button>(R.id.btnCrearSala)
        val btnUnirse = findViewById<Button>(R.id.btnUnirse)

        // ── BOTÓN CREAR SALA ────────────────────────────────────
        btnCrear.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            if (nombre.isEmpty()) {
                Toast.makeText(this, "Por favor, escribe tu nombre", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Genera un código de 5 letras aleatorio (ej: ABCDE)
            val codigo = (1..5)
                .map { ('A'..'Z').random() }
                .joinToString("")

            crearSala(codigo, nombre)
        }

        // ── BOTÓN UNIRSE A SALA ─────────────────────────────────
        btnUnirse.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val codigo = etCodigo.text.toString().trim().uppercase()

            if (nombre.isEmpty() || codigo.isEmpty()) {
                Toast.makeText(this, "Completa nombre y código de sala", Toast.LENGTH_SHORT).show()
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

        // Intentamos guardar en: salas -> [CÓDIGO] -> jugadores -> [NOMBRE]
        db.child("salas").child(codigo).child("jugadores").child(nombre)
            .setValue(jugador)
            .addOnSuccessListener {
                Log.d("Firebase_TioRico", "Sala creada con éxito: $codigo")
                irASalaEspera(codigo, nombre)
            }
            .addOnFailureListener { e ->
                Log.e("Firebase_TioRico", "Error al crear sala", e)
                Toast.makeText(this, "Error de Firebase: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun unirseASala(codigo: String, nombre: String) {
        db.child("salas").child(codigo).get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.exists()) {
                    Toast.makeText(this, "La sala $codigo no existe", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                val jugadoresActuales = snapshot.child("jugadores").childrenCount
                if (jugadoresActuales >= 4) {
                    Toast.makeText(this, "La sala está llena (Máx 4)", Toast.LENGTH_SHORT).show()
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
                        Log.d("Firebase_TioRico", "Unido a la sala: $codigo")
                        irASalaEspera(codigo, nombre)
                    }
            }
            .addOnFailureListener { e ->
                Log.e("Firebase_TioRico", "Error al unirse", e)
                Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
    }

    private fun irASalaEspera(codigo: String, nombre: String) {
        val intent = Intent(this, SalaEsperaActivity::class.java).apply {
            putExtra("codigoSala", codigo)
            putExtra("nombreJugador", nombre)
        }
        startActivity(intent)
        // No ponemos finish() aquí por si el usuario quiere volver atrás desde la sala
    }

    // Lógica para que el botón de "atrás" de la barra superior funcione
    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}
package com.tugrupo.tiorico.ui.sala

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.tugrupo.tiorico.R

class InicioActivity : AppCompatActivity() {

    private val db = Firebase.database.reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)

        val etNombre = findViewById<EditText>(R.id.etNombre)
        val etCodigo = findViewById<EditText>(R.id.etCodigoSala)
        val btnCrear = findViewById<Button>(R.id.btnCrearSala)
        val btnUnirse = findViewById<Button>(R.id.btnUnirse)

        // ── Crear sala ──────────────────────────────────────────
        btnCrear.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            if (nombre.isEmpty()) {
                Toast.makeText(this, "Escribe tu nombre", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Genera un código de 5 letras aleatorio
            val codigo = (1..5)
                .map { ('A'..'Z').random() }
                .joinToString("")

            crearSala(codigo, nombre)
        }

        // ── Unirse a sala ───────────────────────────────────────
        btnUnirse.setOnClickListener {
            val nombre = etNombre.text.toString().trim()
            val codigo = etCodigo.text.toString().trim().uppercase()

            if (nombre.isEmpty() || codigo.isEmpty()) {
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
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

        db.child("salas").child(codigo).child("jugadores").child(nombre)
            .setValue(jugador)
            .addOnSuccessListener {
                irASalaEspera(codigo, nombre)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al crear sala", Toast.LENGTH_SHORT).show()
            }
    }

    private fun unirseASala(codigo: String, nombre: String) {
        db.child("salas").child(codigo).get()
            .addOnSuccessListener { snapshot ->
                if (!snapshot.exists()) {
                    Toast.makeText(this, "Sala no encontrada", Toast.LENGTH_SHORT).show()
                    return@addOnSuccessListener
                }

                val jugadoresActuales = snapshot.child("jugadores").childrenCount
                if (jugadoresActuales >= 4) {
                    Toast.makeText(this, "La sala está llena", Toast.LENGTH_SHORT).show()
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
                        irASalaEspera(codigo, nombre)
                    }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
    }

    private fun irASalaEspera(codigo: String, nombre: String) {
        val intent = Intent(this, SalaEsperaActivity::class.java)
        intent.putExtra("codigoSala", codigo)
        intent.putExtra("nombreJugador", nombre)
        startActivity(intent)
    }
}
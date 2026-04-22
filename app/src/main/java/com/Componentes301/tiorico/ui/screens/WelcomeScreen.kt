package com.Componentes301.tiorico.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Componentes301.tiorico.Utils.Constantes

@Composable
fun WelcomeScreen(onStartLocalGame: () -> Unit) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "TÍO RICO",
            fontSize = 42.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color(0xFF2E7D32)
        )

        Text(text = "Modo Meta", fontSize = 18.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(40.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Capital inicial: $${Constantes.CAPITAL_INICIAL}")
                Text("Meta a alcanzar: $${Constantes.META_DINERO}")
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onStartLocalGame,
            modifier = Modifier.fillMaxWidth().height(50.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C))
        ) {
            Text("COMENZAR PARTIDA", color = Color.White, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Botón Online deshabilitado hasta merge con rama del compañero
        OutlinedButton(
            onClick = { /* TODO: conectar InicioActivity tras el merge */ },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            enabled = false
        ) {
            Text("JUGAR ONLINE (próximamente)")
        }
    }
}
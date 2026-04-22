        package com.Componentes301.tiorico.ui.screens

        import android.content.Intent
        import androidx.compose.foundation.layout.*
        import androidx.compose.material3.*
        import androidx.compose.runtime.*
        import androidx.compose.ui.Alignment
        import androidx.compose.ui.Modifier
        import androidx.compose.ui.graphics.Color
        import androidx.compose.ui.platform.LocalContext
        import androidx.compose.ui.text.font.FontWeight
        import androidx.compose.ui.unit.dp
        import androidx.compose.ui.unit.sp
        import com.Componentes301.tiorico.Utils.Constantes
        import com.Componentes301.tiorico.ui.sala.InicioActivity

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

                Text(text = "Goal Mode", fontSize = 18.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(40.dp))

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Initial capital: $${Constantes.CAPITAL_INICIAL}")
                        Text("Goal to achieve: $${Constantes.META_DINERO}")
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))

                Button(
                    onClick = onStartLocalGame,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF388E3C))
                ) {
                    Text("START GAME", color = Color.White, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))
                val context = LocalContext.current
                // Botón Online deshabilitado hasta merge con rama del compañero
                OutlinedButton(
                    onClick = {
                        val intent = Intent(context, InicioActivity::class.java)
                        context.startActivity(intent)
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("PLAY ONLINE")
                }
            }
        }
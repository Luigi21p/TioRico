package com.Componentes301.tiorico.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.Componentes301.tiorico.logic.GameManager
import com.Componentes301.tiorico.ui.viewmodel.EstadoJuego
import com.Componentes301.tiorico.ui.viewmodel.GameViewModel


// Theme colors
val DarkGreen = Color(0xFF1B5E20)
val LightGreen = Color(0xFF2E7D32)
val Gold = Color(0xFFFFD700)
val DarkGold = Color(0xFFD4AF37)
val BlackBackground = Color(0xFF121212)
val GrayBackground = Color(0xFF1E1E1E)
val Investment = Color(0xFFFF8C00)
val Expense = Color(0xFFDC143C)

// game states
@Composable
fun GameScreen(viewModel: GameViewModel, onExit: () -> Unit) {
    val player by viewModel.jugador
    val message by viewModel.mensaje
    val gameState by viewModel.estadoJuego

    when (gameState) {
        EstadoJuego.VICTORIA -> {
            VictoryScreen(
                finalMoney = player.dinero,
                turns = player.turnoActual,
                goal = GameManager.META,
                onPlayAgain = { viewModel.reiniciarJuego() },
                onExit = onExit
            )
        }
        EstadoJuego.DERROTA -> {
            YouLoseScreen(
                turns = player.turnoActual,
                goal = GameManager.META,
                onRetry = { viewModel.reiniciarJuego() },
                onExit = onExit
            )
        }
        EstadoJuego.JUGANDO -> {
            GamePlayScreen(
                player = player,
                message = message,
                onSave = { viewModel.realizarAccion("ahorrar") },
                onInvest = { viewModel.realizarAccion("invertir") },
                onSpend = { viewModel.realizarAccion("gastar") },
                onExit = onExit
            )
        }
    }
}

// Main gameplay screen
@Composable
fun GamePlayScreen(
    player: com.Componentes301.tiorico.model.Jugador,
    message: String,
    onSave: () -> Unit,
    onInvest: () -> Unit,
    onSpend: () -> Unit,
    onExit: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(BlackBackground, Color(0xFF0A0A0A))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Game title
            Text(
                text = "TÍO RICO",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                color = Gold,
                letterSpacing = 4.sp,
                modifier = Modifier.padding(top = 24.dp)
            )

            Text(
                text = "GOAL MODE",
                fontSize = 14.sp,
                color = DarkGold,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Goal display card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = DarkGreen),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "GOAL",
                        color = Gold,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                    Text(
                        text = "$${GameManager.META}",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Main money card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(8.dp, shape = RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(
                    containerColor = GrayBackground
                ),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "CURRENT CAPITAL",
                        color = DarkGold,
                        fontSize = 12.sp,
                        letterSpacing = 2.sp
                    )

                    //colors money
                    Text(
                        text = "$${player.dinero}",
                        fontSize = 52.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        color = when {
                            player.dinero >= 3000 -> Gold
                            player.dinero >= 1500 -> Color.White
                            else -> Color(0xFFFF6B6B)
                        }
                    )

                    Divider(
                        color = DarkGold.copy(alpha = 0.3f),
                        modifier = Modifier.padding(vertical = 12.dp)
                    )

                    // Turn counter
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("CURRENT TURN", color = Color.Gray, fontSize = 12.sp)
                        Text(
                            "${player.turnoActual}",
                            color = Gold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Result message card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF2A2A2A)
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = message,
                    modifier = Modifier.padding(12.dp),
                    color = Color(0xFFE0E0E0),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Action buttons section
            Text(
                text = "SELECT AN ACTION",
                color = DarkGold,
                fontSize = 12.sp,
                letterSpacing = 2.sp,
                modifier = Modifier.padding(bottom = 12.dp)
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Save button
                Button(
                    onClick = onSave,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DarkGreen
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "SAVE",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Gold
                        )
                        Text(
                            "+ $100",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                }

                // Invest button
                Button(
                    onClick = onInvest,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Investment
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "INVEST",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                        Text(
                            "+ / -",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                }

                // Spend button
                Button(
                    onClick = onSpend,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Expense
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "SPEND",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = Color.White
                        )
                        Text(
                            "- $$$",
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Exit button
            OutlinedButton(
                onClick = onExit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = DarkGold
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text("EXIT GAME", fontSize = 14.sp)
            }
        }
    }
}

// VICTORY SCREEN
@Composable
fun VictoryScreen(
    finalMoney: Int,
    turns: Int,
    goal: Int,
    onPlayAgain: () -> Unit,
    onExit: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(DarkGreen, BlackBackground)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Victory title
            Text(
                text = "VICTORY",
                fontSize = 48.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Serif,
                color = Gold,
                letterSpacing = 6.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "YOU HAVE REACHED THE GOAL",
                fontSize = 14.sp,
                color = Color.White,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Summary card with game stats
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = GrayBackground
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "FINAL SUMMARY",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Gold,
                        letterSpacing = 2.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Final money
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Final money:", color = Color.Gray)
                        Text(
                            "$$finalMoney",
                            color = Gold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Turns used
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Turns used:", color = Color.Gray)
                        Text(
                            "$turns",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Divider(color = DarkGold.copy(alpha = 0.3f))

                    Spacer(modifier = Modifier.height(16.dp))

                    // Goal amount
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Goal amount:", color = Color.Gray)
                        Text(
                            "$$goal",
                            color = Gold,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Play again button
            Button(
                onClick = onPlayAgain,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = LightGreen
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "PLAY AGAIN",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Gold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Exit button
            OutlinedButton(
                onClick = onExit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = DarkGold
                )
            ) {
                Text("EXIT")
            }
        }
    }
}

// YOU LOSE SCREEN
@Composable
fun YouLoseScreen(
    turns: Int,
    goal: Int,
    onRetry: () -> Unit,
    onExit: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(Expense.copy(alpha = 0.8f), BlackBackground)
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Defeat title
            Text(
                text = "YOU LOSE",
                fontSize = 48.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Serif,
                color = Expense,
                letterSpacing = 6.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "YOU HAVE RUN OUT OF MONEY",
                fontSize = 13.sp,
                color = Color.White,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(48.dp))

            // Summary card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = GrayBackground
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "SUMMARY",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Gold,
                        letterSpacing = 2.sp
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Turns survived
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Turns survived:", color = Color.Gray)
                        Text(
                            "$turns",
                            color = Gold,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Divider(color = DarkGold.copy(alpha = 0.3f))

                    Spacer(modifier = Modifier.height(16.dp))

                    // Goal not reached
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Goal not reached:", color = Color.Gray)
                        Text(
                            "$$goal",
                            color = Expense,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Retry button
            Button(
                onClick = onRetry,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = DarkGold
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    "RETRY",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = BlackBackground
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Exit button
            OutlinedButton(
                onClick = onExit,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = DarkGold
                )
            ) {
                Text("EXIT")
            }
        }
    }
}
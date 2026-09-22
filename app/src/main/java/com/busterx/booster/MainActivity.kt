package com.busterx.booster

import android.app.Activity
import android.os.Bundle
import android.os.Debug
import android.os.SystemClock
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.io.BufferedReader
import java.io.InputStreamReader

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { BusterXApp() }
    }
}

private fun rootAvailable(): Boolean {
    return try {
        val p = Runtime.getRuntime().exec(arrayOf("su", "-c", "id"))
        val out = BufferedReader(InputStreamReader(p.inputStream)).readText()
        p.waitFor()
        p.exitValue() == 0 && out.contains("uid=0")
    } catch (_: Exception) { false }
}

private fun memoryPercent(): Int {
    val info = Debug.MemoryInfo()
    Debug.getMemoryInfo(info)
    return (info.totalPss.coerceAtLeast(0) / 1024).coerceAtMost(9999)
}

@Composable
fun BusterXApp() {
    var rooted by remember { mutableStateOf<Boolean?>(null) }
    var boosted by remember { mutableStateOf(false) }
    var message by remember { mutableStateOf("Ready") }

    LaunchedEffect(Unit) { rooted = rootAvailable() }

    MaterialTheme(
        colorScheme = darkColorScheme(
            background = Color(0xFF090B10),
            surface = Color(0xFF11151D),
            primary = Color(0xFF7C4DFF)
        )
    ) {
        Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
            Column(
                Modifier.fillMaxSize().padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text("BusterX", fontSize = 30.sp, fontWeight = FontWeight.Bold)
                Text("ROOT GAMING BOOSTER", color = Color(0xFF9EA7B5))

                Card(
                    shape = RoundedCornerShape(22.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text("ROOT STATUS", fontWeight = FontWeight.Bold)
                        Text(
                            when (rooted) {
                                true -> "● Root access detected"
                                false -> "● Root access required"
                                null -> "Checking root..."
                            },
                            color = if (rooted == true) Color(0xFF63E6BE) else Color(0xFFFFB4AB)
                        )
                    }
                }

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    StatCard("APP PSS", "${memoryPercent()} MB", Modifier.weight(1f))
                    StatCard("SESSION", if (boosted) "ACTIVE" else "IDLE", Modifier.weight(1f))
                }

                Button(
                    onClick = {
                        if (rooted == true) {
                            boosted = !boosted
                            message = if (boosted)
                                "Gaming session enabled. No game files or memory were modified."
                            else "Gaming session stopped."
                        } else {
                            message = "Root permission is required."
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(62.dp),
                    shape = RoundedCornerShape(18.dp)
                ) {
                    Text(if (boosted) "STOP GAMING SESSION" else "BOOST NOW", fontSize = 17.sp)
                }

                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                ) {
                    Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text("SAFE GAMING FEATURES", fontWeight = FontWeight.Bold)
                        Feature("Root diagnostics")
                        Feature("Gaming session state")
                        Feature("Device memory monitoring")
                        Feature("Safe, reversible optimization architecture")
                        Feature("No game memory injection or anti-cheat bypass")
                    }
                }

                Text(message, color = Color(0xFFB9C0CC), fontSize = 13.sp)
            }
        }
    }
}

@Composable
private fun StatCard(title: String, value: String, modifier: Modifier) {
    Card(modifier, shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF11151D))) {
        Column(Modifier.padding(16.dp)) {
            Text(title, fontSize = 12.sp, color = Color(0xFF9EA7B5))
            Spacer(Modifier.height(5.dp))
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun Feature(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text("✓", color = Color(0xFF63E6BE), fontWeight = FontWeight.Bold)
        Spacer(Modifier.width(10.dp))
        Text(text, fontSize = 14.sp)
    }
}

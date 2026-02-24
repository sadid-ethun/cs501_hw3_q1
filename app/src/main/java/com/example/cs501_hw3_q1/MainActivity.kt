package com.example.cs501_hw3_q1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.draw.shadow
import kotlinx.coroutines.launch
import com.example.cs501_hw3_q1.ui.theme.Cs501_hw3_q1Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { Cs501_hw3_q1Theme { SettingsApp() } }
    }
}

@Composable
fun SettingsApp() {
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { inner ->
        SettingsScreen(
            modifier = Modifier.padding(inner),
            snackbarHostState = snackbarHostState
        )
    }
}

@Composable
fun SettingsScreen(modifier: Modifier = Modifier, snackbarHostState: SnackbarHostState) {
    val scope = rememberCoroutineScope()

    var notifications by rememberSaveable { mutableStateOf(true) }
    var publicProfile by rememberSaveable { mutableStateOf(false) }
    var brightness by rememberSaveable { mutableStateOf(0.35f) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineSmall)

        Divider()

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                SettingRow(
                    title = "Notifications",
                    onRowClick = { notifications = !notifications },
                    control = { Checkbox(checked = notifications, onCheckedChange = { notifications = it }) }
                )

                SettingRow(
                    title = "Brightness",
                    trailingText = "${(brightness * 100).toInt()}%",
                    onRowClick = {},
                    control = {
                        Slider(
                            value = brightness,
                            onValueChange = { brightness = it },
                            modifier = Modifier
                                .sizeIn(minWidth = 160.dp, maxWidth = 200.dp)
                                .align(Alignment.CenterVertically)
                        )
                    }
                )

                SettingRow(
                    title = "Public Profile",
                    onRowClick = { publicProfile = !publicProfile },
                    control = { Switch(checked = publicProfile, onCheckedChange = { publicProfile = it }) }
                )

                SettingRow(
                    title = "Sign Out",
                    onRowClick = {},
                    control = {
                        Button(onClick = {
                            scope.launch { snackbarHostState.showSnackbar("Signed out") }
                        }) { Text("Sign out") }
                    }
                )
            }
        }
    }
}

@Composable
private fun SettingRow(
    title: String,
    trailingText: String? = null,
    onRowClick: () -> Unit,
    control: @Composable RowScope.() -> Unit
) {
    val shape = RoundedCornerShape(14.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(5.dp, shape, clip = false)
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onRowClick)
            .heightIn(min = 64.dp)
            .padding(horizontal = 14.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(title, style = MaterialTheme.typography.titleSmall)

            if (trailingText != null) {
                Spacer(Modifier.width(10.dp))
                Text(
                    trailingText,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        control()
    }
}
package com.example.analisisestructural

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.analisisestructural.ui.theme.PorticoScreen
import com.example.analisisestructural.ui.theme.AnalisisEstructuralTheme
import com.example.analisisestructural.ui.theme.PorticoScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AnalisisEstructuralTheme {
                PorticoScreen()
            }
        }
    }
}
@Preview(
    showBackground = true,
    showSystemUi = true,
    device = "spec:width=411dp,height=891dp"
)
@Composable
fun PorticoScreenPreview() {
    MaterialTheme {
        PorticoScreen()
    }
}

package com.example.analisisestructural.ui.theme


import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.analisisestructural.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.foundation.gestures.detectTapGestures


@Composable
fun PorticoScreen() {

    val scope = rememberCoroutineScope()

    var pisos by remember { mutableStateOf("") }
    var vanos by remember { mutableStateOf("") }
    var altura by remember { mutableStateOf("") }
    var longitud by remember { mutableStateOf("") }

    var status by remember { mutableStateOf("") }
    var imageBitmap by remember { mutableStateOf<android.graphics.Bitmap?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("Análisis de Pórtico 2D", fontSize = 24.sp)

        Spacer(Modifier.height(16.dp))

        Campo("Número de pisos", pisos) { pisos = it }
        Campo("Número de vanos", vanos) { vanos = it }
        Campo("Altura de piso (m)", altura) { altura = it }
        Campo("Longitud de vano (m)", longitud) { longitud = it }

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                scope.launch {
                    try {
                        status = "Analizando..."
                        imageBitmap = null

                        val bytes = withContext(Dispatchers.IO) {
                            ApiClient.analizarPortico(
                                pisos.toInt(),
                                vanos.toInt(),
                                altura.toFloat(),
                                longitud.toFloat()
                            )
                        }

                        imageBitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                        status = "Resultado"

                    } catch (e: Exception) {
                        status = "Error: ${e.message}"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Analizar estructura")
        }

        Spacer(Modifier.height(12.dp))

        Text(status)

        Spacer(Modifier.height(12.dp))

        imageBitmap?.let { bitmap ->

            var scale by remember { mutableStateOf(1f) }
            var offsetX by remember { mutableStateOf(0f) }
            var offsetY by remember { mutableStateOf(0f) }

            val state = rememberTransformableState { zoomChange, offsetChange, _ ->
                scale *= zoomChange
                offsetX += offsetChange.x
                offsetY += offsetChange.y
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    bitmap = bitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize()
                        .graphicsLayer(
                            scaleX = scale,
                            scaleY = scale,
                            translationX = offsetX,
                            translationY = offsetY
                        )
                        .transformable(state)
                        .pointerInput(Unit) {
                            awaitPointerEventScope {
                                while (true) {
                                    val event = awaitPointerEvent()
                                    if (event.changes.all { !it.pressed }) {
                                        // Cuando se sueltan los dedos → reset
                                        scale = 1f
                                        offsetX = 0f
                                        offsetY = 0f
                                    }
                                }
                            }
                        }
                )
            }
        }



    }
}

@Composable
fun Campo(label: String, value: String, onChange: (String) -> Unit) {
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    )
}

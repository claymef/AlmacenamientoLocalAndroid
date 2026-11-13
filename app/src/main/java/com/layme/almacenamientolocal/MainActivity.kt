package com.layme.almacenamientolocal

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import java.io.FileNotFoundException

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    PantallaBorrador()
                }
            }
        }
    }
}

@Composable
fun PantallaBorrador() {
    // Obtenemos el contexto para poder acceder a los archivos del sistema
    val contexto = LocalContext.current
    val nombreArchivo = "borrador_anuncio.txt"

    // Variable de estado que guarda lo que el usuario escribe
    var textoAnuncio by remember { mutableStateOf("") }

    // REQUISITO 3: Recuperar el contenido del archivo al abrir la pantalla
    // LaunchedEffect(Unit) hace que esto se ejecute solo una vez al iniciar la pantalla
    LaunchedEffect(Unit) {
        try {
            contexto.openFileInput(nombreArchivo).bufferedReader().use { lector ->
                textoAnuncio = lector.readText()
            }
            if (textoAnuncio.isNotEmpty()) {
                Toast.makeText(contexto, "Borrador recuperado", Toast.LENGTH_SHORT).show()
            }
        } catch (e: FileNotFoundException) {
            // Si el archivo no existe (primera vez), no hacemos nada
        }
    }

    // Diseño de la interfaz
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Redactar Anuncio",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        // REQUISITO 1: Redactar el contenido de un anuncio
        OutlinedTextField(
            value = textoAnuncio,
            onValueChange = { textoAnuncio = it }, // Actualiza el texto mientras se escribe
            label = { Text("Escribe el borrador de tu anuncio aquí...") },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f) // Hace que el cuadro de texto ocupe todo el espacio disponible
        )

        Spacer(modifier = Modifier.height(16.dp))

        // REQUISITO 2: Guardar el borrador en el almacenamiento interno privado
        Button(
            onClick = {
                try {
                    // Context.MODE_PRIVATE asegura que es almacenamiento interno privado
                    contexto.openFileOutput(nombreArchivo, Context.MODE_PRIVATE).use { salida ->
                        salida.write(textoAnuncio.toByteArray())
                    }
                    Toast.makeText(contexto, "Borrador guardado exitosamente", Toast.LENGTH_SHORT).show()
                } catch (e: Exception) {
                    Toast.makeText(contexto, "Error al guardar", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Guardar Borrador")
        }
    }
}
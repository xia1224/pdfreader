package com.example.pdfreader

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.github.barteksc.pdfviewer.PDFView
import androidx.compose.ui.viewinterop.AndroidView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PdfApp()
        }
    }
}

@Composable
fun PdfApp() {
    var pdfUri by remember { mutableStateOf<Uri?>(null) }
    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        pdfUri = uri
    }

    MaterialTheme(
        colorScheme = dynamicLightColorScheme(context)
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Material 3 PDF Reader") }
                )
            },
            floatingActionButton = {
                FloatingActionButton(onClick = {
                    launcher.launch(arrayOf("application/pdf"))
                }) {
                    Text("+")
                }
            }
        ) { padding ->
            Box(modifier = Modifier.padding(padding)) {
                pdfUri?.let { uri ->
                    AndroidView(
                        factory = { ctx ->
                            PDFView(ctx, null).apply {
                                fromUri(uri)
                                    .enableSwipe(true)
                                    .swipeHorizontal(false)
                                    .spacing(8)
                                    .load()
                            }
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                } ?: run {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Text("请选择一个 PDF 文件")
                    }
                }
            }
        }
    }
}

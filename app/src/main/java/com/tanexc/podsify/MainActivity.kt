package com.tanexc.podsify

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.stack.Children
import com.arkivanov.decompose.retainedComponent
import com.tanexc.podsify.presentation.navigation.RootComponent
import com.tanexc.podsify.presentation.theme.PodsifyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val root = retainedComponent { context ->
            RootComponent(context)
        }
        setContent {
            PodsifyTheme {
                Scaffold { paddingValues ->
                    Children(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .padding(horizontal = 16.dp),
                        stack = root.childStack,
                        content = { child -> child.instance.Content() }
                    )
                }
            }
        }
    }
}
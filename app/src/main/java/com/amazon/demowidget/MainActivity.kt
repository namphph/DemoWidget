package com.amazon.demowidget

import android.appwidget.AppWidgetProviderInfo.WIDGET_CATEGORY_HOME_SCREEN
import android.appwidget.AppWidgetProviderInfo.WIDGET_CATEGORY_KEYGUARD
import android.appwidget.AppWidgetProviderInfo.WIDGET_CATEGORY_SEARCHBOX
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.collection.intSetOf
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.lifecycle.lifecycleScope
import com.amazon.demowidget.ui.theme.DemoWidgetTheme
import com.amazon.demowidget.widget.MyAppWidget
import com.amazon.demowidget.widget.MyAppWidgetReceiver
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            lifecycleScope.launch {
                val manager = GlanceAppWidgetManager(this@MainActivity)
                manager.setWidgetPreviews(
                    MyAppWidgetReceiver::class,
                    intSetOf(
                        WIDGET_CATEGORY_HOME_SCREEN,
                    )
                )
            }
        }
        setContent {
            DemoWidgetTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    setUpWidget(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun setUpWidget(name: String, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Button(
        onClick = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM){
                scope.launch {
                    val manager = GlanceAppWidgetManager(context)
                    manager.setWidgetPreviews(MyAppWidgetReceiver::class, intSetOf(
                        WIDGET_CATEGORY_HOME_SCREEN
                    ))
                }
            }
        },
        modifier = modifier
    ) {
        Text(text = "Set up widget for $name")
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DemoWidgetTheme {
        setUpWidget("Android")
    }
}


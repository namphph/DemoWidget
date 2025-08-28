package com.amazon.demowidget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProviderInfo.WIDGET_CATEGORY_HOME_SCREEN
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
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
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                        setupWidgetButton(
                            name = "Android",
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun setupWidgetButton(modifier: Modifier = Modifier, name: String) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val isPinningSupported = remember {
        AppWidgetManager.getInstance(context).isRequestPinAppWidgetSupported
    }

    if (isPinningSupported) {
        Button(
            onClick = {
                coroutineScope.launch {
                    GlanceAppWidgetManager(context).requestPinGlanceAppWidget(
                        receiver = MyAppWidgetReceiver::class.java,
                        preview = MyAppWidget(),
                        previewState = DpSize(245.dp, 115.dp)
                    )
                }
            },
            modifier = modifier
        ) {
            Text(text = "Set up widget for $name")
        }
    } else {
        // Không hiển thị gì hoặc có thể hiển thị thông báo nếu muốn
        // Text("Widget pinning not supported on this device")
    }
}


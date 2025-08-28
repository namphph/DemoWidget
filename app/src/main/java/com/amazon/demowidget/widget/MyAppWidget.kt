package com.amazon.demowidget.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import androidx.core.content.edit
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.glance.Button
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.action.ActionParameters
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.appwidget.action.actionRunCallback
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.state.updateAppWidgetState
import androidx.glance.appwidget.updateAll
import androidx.glance.appwidget.updateIf
import androidx.glance.currentState
import androidx.glance.layout.Alignment
import androidx.glance.layout.Column
import androidx.glance.layout.Row
import androidx.glance.layout.fillMaxSize
import androidx.glance.layout.padding
import androidx.glance.text.Text
import com.amazon.demowidget.MainActivity
import com.amazon.demowidget.R

val KEY_MODE = booleanPreferencesKey("mode")

class MyAppWidget: GlanceAppWidget() {
    override suspend fun provideGlance(
        context: Context,
        id: GlanceId
    ) {

        provideContent {
            MyWidgetContent(false, context)
        }
    }

    override suspend fun providePreview(context: Context, widgetCategory: Int) {
        provideContent {
            Text(
                text = "🏠 Home",
                modifier = GlanceModifier.padding(end = 12.dp)
            )
        }
    }

    override fun onCompositionError(
        context: Context,
        glanceId: GlanceId,
        appWidgetId: Int,
        throwable: Throwable
    ) {
        val rv = RemoteViews(context.packageName, R.layout.error_layout)
        rv.setTextViewText(
            R.id.error_text_view,
            "Error was thrown. \nThis is a custom view \nError Message: `${throwable.message}`"
        )
        rv.setOnClickPendingIntent(R.id.error_icon, getErrorIntent(context, throwable))
        AppWidgetManager.getInstance(context).updateAppWidget(appWidgetId, rv)
    }

    @Composable
    fun MyWidgetContent(isPreview: Boolean, context: Context) {
//        val prefs = context.getSharedPreferences("widget_prefs", Context.MODE_PRIVATE)
//        val isDarkMode = prefs.getBoolean("dark_mode", false)
        var isDarkMode = currentState(KEY_MODE)?:false
        Column(
            modifier = GlanceModifier.fillMaxSize(),
            verticalAlignment = Alignment.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Where to?",
                modifier = GlanceModifier.padding(12.dp)
            )

            Row(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = GlanceModifier.padding(8.dp)
            ) {
                if (isPreview) {
                    Text(
                        text = "🏠 Home",
                        modifier = GlanceModifier.padding(end = 12.dp)
                    )
                    Text(
                        text = "🏢 Work"
                    )
                } else {
                    Log.d("112233","Vao day")
                    Button(
                        text = "🏠 Home",
                        onClick = actionStartActivity<MainActivity>(),
                    )
                    Button(
                        text = if (isDarkMode) "🌙 Dark Mode" else "☀️ Light Mode",
                        onClick = actionRunCallback<ToggleThemeAction>()
                    )
                }
            }
        }
    }
}

private fun getErrorIntent(context: Context, throwable: Throwable): PendingIntent {
    val intent = Intent(context, MyAppWidgetReceiver::class.java)
    intent.action = "widgetError"
    return PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_IMMUTABLE)
}

class ToggleThemeAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        updateAppWidgetState(context, glanceId) { prefs ->
            prefs[KEY_MODE] = prefs[KEY_MODE] != true
        }
        MyAppWidget().update(context, glanceId)
    }
}

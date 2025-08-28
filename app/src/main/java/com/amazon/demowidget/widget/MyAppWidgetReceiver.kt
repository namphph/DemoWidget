package com.amazon.demowidget.widget

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class MyAppWidgetReceiver() : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget =  MyAppWidget()

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        Log.d("112233","A")
    }
}

class MyAppWidgetReceiver2() : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget =  MyAppWidget()
}

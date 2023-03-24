package com.example.cft_test

import android.app.Application

class AppInit: Application() {
    val database by lazy {
        AppDatabase.getInstance(this)
    }
}
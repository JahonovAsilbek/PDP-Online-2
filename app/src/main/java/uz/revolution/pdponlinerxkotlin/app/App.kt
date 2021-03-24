package uz.revolution.pdponlinerxkotlin.app

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import uz.revolution.pdponlinerxkotlin.database.AppDatabase

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        AppDatabase.initDatabase(this)

        // disable night mode settings
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}
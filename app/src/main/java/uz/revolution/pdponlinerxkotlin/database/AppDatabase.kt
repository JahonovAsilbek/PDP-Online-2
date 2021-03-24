package uz.revolution.pdponlinerxkotlin.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.revolution.pdponlinerxkotlin.daos.AllDaos
import uz.revolution.pdponlinerxkotlin.entities.Course
import uz.revolution.pdponlinerxkotlin.entities.Lesson
import uz.revolution.pdponlinerxkotlin.entities.Module

@Database(
    entities = [Course::class, Module::class, Lesson::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun getDao(): AllDaos

    companion object {
        @Volatile
        private var database: AppDatabase? = null

        fun initDatabase(context: Context) {
            synchronized(this) {
                if (database == null) {
                    database = Room.databaseBuilder(
                        context.applicationContext,
                        AppDatabase::class.java,
                        "pdpuz.db"
                    )
                        .fallbackToDestructiveMigration()
                        .allowMainThreadQueries()
                        .build()
                }
            }
        }
    }

    object get {
        fun getDatabase(): AppDatabase {
            return database!!
        }
    }
}
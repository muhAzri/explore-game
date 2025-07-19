package com.zrifapps.exploregame.core.data.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SecureDatabaseFactory @Inject constructor(
    private val context: Context
) {
    
    /**
     * Creates a secure Room database with Write-Ahead Logging (WAL) enabled.
     * Uses Room's built-in security features instead of manual PRAGMA execution
     * to avoid SQLite transaction conflicts.
     */
    fun createAppDatabase(): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            AppDatabase.DATABASE_NAME
        )
            .setJournalMode(RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING)
            .fallbackToDestructiveMigration(false)
        .build()
    }
}
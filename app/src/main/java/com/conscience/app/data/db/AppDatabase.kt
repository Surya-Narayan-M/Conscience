package com.conscience.app.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.conscience.app.utils.Constants
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SupportFactory

@Database(
    entities = [SessionEntity::class],
    version = Constants.DATABASE_VERSION,
    exportSchema = false   // SECURITY: Never export schema
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                // SECURITY: Encrypt database with SQLCipher
                val passphrase = SQLiteDatabase.getBytes(
                    android.util.Base64.encodeToString(
                        context.packageName.toByteArray(),
                        android.util.Base64.NO_WRAP
                    ).toCharArray()
                )
                val factory = SupportFactory(passphrase)

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    Constants.DATABASE_NAME
                )
                    .openHelperFactory(factory)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
